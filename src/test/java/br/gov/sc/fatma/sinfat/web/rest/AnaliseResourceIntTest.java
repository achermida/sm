package br.gov.sc.fatma.sinfat.web.rest;

import br.gov.sc.fatma.sinfat.SmApp;

import br.gov.sc.fatma.sinfat.domain.Analise;
import br.gov.sc.fatma.sinfat.repository.AnaliseRepository;
import br.gov.sc.fatma.sinfat.repository.search.AnaliseSearchRepository;

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

import br.gov.sc.fatma.sinfat.domain.enumeration.StatusGeralEnum;
/**
 * Test class for the AnaliseResource REST controller.
 *
 * @see AnaliseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmApp.class)
public class AnaliseResourceIntTest {

    private static final StatusGeralEnum DEFAULT_STATUS = StatusGeralEnum.ATIVO;
    private static final StatusGeralEnum UPDATED_STATUS = StatusGeralEnum.CANCELADO;

    @Inject
    private AnaliseRepository analiseRepository;

    @Inject
    private AnaliseSearchRepository analiseSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAnaliseMockMvc;

    private Analise analise;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AnaliseResource analiseResource = new AnaliseResource();
        ReflectionTestUtils.setField(analiseResource, "analiseSearchRepository", analiseSearchRepository);
        ReflectionTestUtils.setField(analiseResource, "analiseRepository", analiseRepository);
        this.restAnaliseMockMvc = MockMvcBuilders.standaloneSetup(analiseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Analise createEntity(EntityManager em) {
        Analise analise = new Analise()
                .status(DEFAULT_STATUS);
        return analise;
    }

    @Before
    public void initTest() {
        analiseSearchRepository.deleteAll();
        analise = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnalise() throws Exception {
        int databaseSizeBeforeCreate = analiseRepository.findAll().size();

        // Create the Analise

        restAnaliseMockMvc.perform(post("/api/analises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analise)))
            .andExpect(status().isCreated());

        // Validate the Analise in the database
        List<Analise> analiseList = analiseRepository.findAll();
        assertThat(analiseList).hasSize(databaseSizeBeforeCreate + 1);
        Analise testAnalise = analiseList.get(analiseList.size() - 1);
        assertThat(testAnalise.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Analise in ElasticSearch
        Analise analiseEs = analiseSearchRepository.findOne(testAnalise.getId());
        assertThat(analiseEs).isEqualToComparingFieldByField(testAnalise);
    }

    @Test
    @Transactional
    public void createAnaliseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = analiseRepository.findAll().size();

        // Create the Analise with an existing ID
        Analise existingAnalise = new Analise();
        existingAnalise.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnaliseMockMvc.perform(post("/api/analises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingAnalise)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Analise> analiseList = analiseRepository.findAll();
        assertThat(analiseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = analiseRepository.findAll().size();
        // set the field null
        analise.setStatus(null);

        // Create the Analise, which fails.

        restAnaliseMockMvc.perform(post("/api/analises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analise)))
            .andExpect(status().isBadRequest());

        List<Analise> analiseList = analiseRepository.findAll();
        assertThat(analiseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnalises() throws Exception {
        // Initialize the database
        analiseRepository.saveAndFlush(analise);

        // Get all the analiseList
        restAnaliseMockMvc.perform(get("/api/analises?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analise.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getAnalise() throws Exception {
        // Initialize the database
        analiseRepository.saveAndFlush(analise);

        // Get the analise
        restAnaliseMockMvc.perform(get("/api/analises/{id}", analise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(analise.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAnalise() throws Exception {
        // Get the analise
        restAnaliseMockMvc.perform(get("/api/analises/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnalise() throws Exception {
        // Initialize the database
        analiseRepository.saveAndFlush(analise);
        analiseSearchRepository.save(analise);
        int databaseSizeBeforeUpdate = analiseRepository.findAll().size();

        // Update the analise
        Analise updatedAnalise = analiseRepository.findOne(analise.getId());
        updatedAnalise
                .status(UPDATED_STATUS);

        restAnaliseMockMvc.perform(put("/api/analises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnalise)))
            .andExpect(status().isOk());

        // Validate the Analise in the database
        List<Analise> analiseList = analiseRepository.findAll();
        assertThat(analiseList).hasSize(databaseSizeBeforeUpdate);
        Analise testAnalise = analiseList.get(analiseList.size() - 1);
        assertThat(testAnalise.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Analise in ElasticSearch
        Analise analiseEs = analiseSearchRepository.findOne(testAnalise.getId());
        assertThat(analiseEs).isEqualToComparingFieldByField(testAnalise);
    }

    @Test
    @Transactional
    public void updateNonExistingAnalise() throws Exception {
        int databaseSizeBeforeUpdate = analiseRepository.findAll().size();

        // Create the Analise

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAnaliseMockMvc.perform(put("/api/analises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analise)))
            .andExpect(status().isCreated());

        // Validate the Analise in the database
        List<Analise> analiseList = analiseRepository.findAll();
        assertThat(analiseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAnalise() throws Exception {
        // Initialize the database
        analiseRepository.saveAndFlush(analise);
        analiseSearchRepository.save(analise);
        int databaseSizeBeforeDelete = analiseRepository.findAll().size();

        // Get the analise
        restAnaliseMockMvc.perform(delete("/api/analises/{id}", analise.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean analiseExistsInEs = analiseSearchRepository.exists(analise.getId());
        assertThat(analiseExistsInEs).isFalse();

        // Validate the database is empty
        List<Analise> analiseList = analiseRepository.findAll();
        assertThat(analiseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAnalise() throws Exception {
        // Initialize the database
        analiseRepository.saveAndFlush(analise);
        analiseSearchRepository.save(analise);

        // Search the analise
        restAnaliseMockMvc.perform(get("/api/_search/analises?query=id:" + analise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analise.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
