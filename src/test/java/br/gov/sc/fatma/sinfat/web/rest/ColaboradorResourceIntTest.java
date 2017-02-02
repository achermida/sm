package br.gov.sc.fatma.sinfat.web.rest;

import br.gov.sc.fatma.sinfat.SmApp;

import br.gov.sc.fatma.sinfat.domain.Colaborador;
import br.gov.sc.fatma.sinfat.repository.ColaboradorRepository;
import br.gov.sc.fatma.sinfat.repository.search.ColaboradorSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.gov.sc.fatma.sinfat.domain.enumeration.CargoEnum;
/**
 * Test class for the ColaboradorResource REST controller.
 *
 * @see ColaboradorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmApp.class)
public class ColaboradorResourceIntTest {

    private static final CargoEnum DEFAULT_CARGO = CargoEnum.ADMINISTRADOR;
    private static final CargoEnum UPDATED_CARGO = CargoEnum.AUDITOR;

    @Inject
    private ColaboradorRepository colaboradorRepository;

    @Inject
    private ColaboradorSearchRepository colaboradorSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restColaboradorMockMvc;

    private Colaborador colaborador;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ColaboradorResource colaboradorResource = new ColaboradorResource();
        ReflectionTestUtils.setField(colaboradorResource, "colaboradorSearchRepository", colaboradorSearchRepository);
        ReflectionTestUtils.setField(colaboradorResource, "colaboradorRepository", colaboradorRepository);
        this.restColaboradorMockMvc = MockMvcBuilders.standaloneSetup(colaboradorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Colaborador createEntity(EntityManager em) {
        Colaborador colaborador = new Colaborador()
                .cargo(DEFAULT_CARGO);
        return colaborador;
    }

    @Before
    public void initTest() {
        colaboradorSearchRepository.deleteAll();
        colaborador = createEntity(em);
    }

    @Test
    @Transactional
    public void createColaborador() throws Exception {
        int databaseSizeBeforeCreate = colaboradorRepository.findAll().size();

        // Create the Colaborador

        restColaboradorMockMvc.perform(post("/api/colaboradors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(colaborador)))
            .andExpect(status().isCreated());

        // Validate the Colaborador in the database
        List<Colaborador> colaboradorList = colaboradorRepository.findAll();
        assertThat(colaboradorList).hasSize(databaseSizeBeforeCreate + 1);
        Colaborador testColaborador = colaboradorList.get(colaboradorList.size() - 1);
        assertThat(testColaborador.getCargo()).isEqualTo(DEFAULT_CARGO);

        // Validate the Colaborador in ElasticSearch
        Colaborador colaboradorEs = colaboradorSearchRepository.findOne(testColaborador.getId());
        assertThat(colaboradorEs).isEqualToComparingFieldByField(testColaborador);
    }

    @Test
    @Transactional
    public void createColaboradorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = colaboradorRepository.findAll().size();

        // Create the Colaborador with an existing ID
        Colaborador existingColaborador = new Colaborador();
        existingColaborador.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restColaboradorMockMvc.perform(post("/api/colaboradors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingColaborador)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Colaborador> colaboradorList = colaboradorRepository.findAll();
        assertThat(colaboradorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCargoIsRequired() throws Exception {
        int databaseSizeBeforeTest = colaboradorRepository.findAll().size();
        // set the field null
        colaborador.setCargo(null);

        // Create the Colaborador, which fails.

        restColaboradorMockMvc.perform(post("/api/colaboradors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(colaborador)))
            .andExpect(status().isBadRequest());

        List<Colaborador> colaboradorList = colaboradorRepository.findAll();
        assertThat(colaboradorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllColaboradors() throws Exception {
        // Initialize the database
        colaboradorRepository.saveAndFlush(colaborador);

        // Get all the colaboradorList
        restColaboradorMockMvc.perform(get("/api/colaboradors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(colaborador.getId().intValue())))
            .andExpect(jsonPath("$.[*].cargo").value(hasItem(DEFAULT_CARGO.toString())));
    }

    @Test
    @Transactional
    public void getColaborador() throws Exception {
        // Initialize the database
        colaboradorRepository.saveAndFlush(colaborador);

        // Get the colaborador
        restColaboradorMockMvc.perform(get("/api/colaboradors/{id}", colaborador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(colaborador.getId().intValue()))
            .andExpect(jsonPath("$.cargo").value(DEFAULT_CARGO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingColaborador() throws Exception {
        // Get the colaborador
        restColaboradorMockMvc.perform(get("/api/colaboradors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateColaborador() throws Exception {
        // Initialize the database
        colaboradorRepository.saveAndFlush(colaborador);
        colaboradorSearchRepository.save(colaborador);
        int databaseSizeBeforeUpdate = colaboradorRepository.findAll().size();

        // Update the colaborador
        Colaborador updatedColaborador = colaboradorRepository.findOne(colaborador.getId());
        updatedColaborador
                .cargo(UPDATED_CARGO);

        restColaboradorMockMvc.perform(put("/api/colaboradors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedColaborador)))
            .andExpect(status().isOk());

        // Validate the Colaborador in the database
        List<Colaborador> colaboradorList = colaboradorRepository.findAll();
        assertThat(colaboradorList).hasSize(databaseSizeBeforeUpdate);
        Colaborador testColaborador = colaboradorList.get(colaboradorList.size() - 1);
        assertThat(testColaborador.getCargo()).isEqualTo(UPDATED_CARGO);

        // Validate the Colaborador in ElasticSearch
        Colaborador colaboradorEs = colaboradorSearchRepository.findOne(testColaborador.getId());
        assertThat(colaboradorEs).isEqualToComparingFieldByField(testColaborador);
    }

    @Test
    @Transactional
    public void updateNonExistingColaborador() throws Exception {
        int databaseSizeBeforeUpdate = colaboradorRepository.findAll().size();

        // Create the Colaborador

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restColaboradorMockMvc.perform(put("/api/colaboradors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(colaborador)))
            .andExpect(status().isCreated());

        // Validate the Colaborador in the database
        List<Colaborador> colaboradorList = colaboradorRepository.findAll();
        assertThat(colaboradorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteColaborador() throws Exception {
        // Initialize the database
        colaboradorRepository.saveAndFlush(colaborador);
        colaboradorSearchRepository.save(colaborador);
        int databaseSizeBeforeDelete = colaboradorRepository.findAll().size();

        // Get the colaborador
        restColaboradorMockMvc.perform(delete("/api/colaboradors/{id}", colaborador.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean colaboradorExistsInEs = colaboradorSearchRepository.exists(colaborador.getId());
        assertThat(colaboradorExistsInEs).isFalse();

        // Validate the database is empty
        List<Colaborador> colaboradorList = colaboradorRepository.findAll();
        assertThat(colaboradorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchColaborador() throws Exception {
        // Initialize the database
        colaboradorRepository.saveAndFlush(colaborador);
        colaboradorSearchRepository.save(colaborador);

        // Search the colaborador
        restColaboradorMockMvc.perform(get("/api/_search/colaboradors?query=id:" + colaborador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(colaborador.getId().intValue())))
            .andExpect(jsonPath("$.[*].cargo").value(hasItem(DEFAULT_CARGO.toString())));
    }
}
