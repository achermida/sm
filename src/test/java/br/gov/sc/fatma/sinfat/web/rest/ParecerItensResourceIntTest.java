package br.gov.sc.fatma.sinfat.web.rest;

import br.gov.sc.fatma.sinfat.SmApp;

import br.gov.sc.fatma.sinfat.domain.ParecerItens;
import br.gov.sc.fatma.sinfat.repository.ParecerItensRepository;
import br.gov.sc.fatma.sinfat.repository.search.ParecerItensSearchRepository;

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

import br.gov.sc.fatma.sinfat.domain.enumeration.TipoParecerItensEnum;
/**
 * Test class for the ParecerItensResource REST controller.
 *
 * @see ParecerItensResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmApp.class)
public class ParecerItensResourceIntTest {

    private static final TipoParecerItensEnum DEFAULT_TIPO = TipoParecerItensEnum.FENOMENOS;
    private static final TipoParecerItensEnum UPDATED_TIPO = TipoParecerItensEnum.FENOMENOS;

    private static final String DEFAULT_PARIT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_PARIT_DESCRICAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_PARIT_SEQUENCIA = 1;
    private static final Integer UPDATED_PARIT_SEQUENCIA = 2;

    @Inject
    private ParecerItensRepository parecerItensRepository;

    @Inject
    private ParecerItensSearchRepository parecerItensSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restParecerItensMockMvc;

    private ParecerItens parecerItens;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParecerItensResource parecerItensResource = new ParecerItensResource();
        ReflectionTestUtils.setField(parecerItensResource, "parecerItensSearchRepository", parecerItensSearchRepository);
        ReflectionTestUtils.setField(parecerItensResource, "parecerItensRepository", parecerItensRepository);
        this.restParecerItensMockMvc = MockMvcBuilders.standaloneSetup(parecerItensResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParecerItens createEntity(EntityManager em) {
        ParecerItens parecerItens = new ParecerItens()
                .tipo(DEFAULT_TIPO)
                .paritDescricao(DEFAULT_PARIT_DESCRICAO)
                .paritSequencia(DEFAULT_PARIT_SEQUENCIA);
        return parecerItens;
    }

    @Before
    public void initTest() {
        parecerItensSearchRepository.deleteAll();
        parecerItens = createEntity(em);
    }

    @Test
    @Transactional
    public void createParecerItens() throws Exception {
        int databaseSizeBeforeCreate = parecerItensRepository.findAll().size();

        // Create the ParecerItens

        restParecerItensMockMvc.perform(post("/api/parecer-itens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parecerItens)))
            .andExpect(status().isCreated());

        // Validate the ParecerItens in the database
        List<ParecerItens> parecerItensList = parecerItensRepository.findAll();
        assertThat(parecerItensList).hasSize(databaseSizeBeforeCreate + 1);
        ParecerItens testParecerItens = parecerItensList.get(parecerItensList.size() - 1);
        assertThat(testParecerItens.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testParecerItens.getParitDescricao()).isEqualTo(DEFAULT_PARIT_DESCRICAO);
        assertThat(testParecerItens.getParitSequencia()).isEqualTo(DEFAULT_PARIT_SEQUENCIA);

        // Validate the ParecerItens in ElasticSearch
        ParecerItens parecerItensEs = parecerItensSearchRepository.findOne(testParecerItens.getId());
        assertThat(parecerItensEs).isEqualToComparingFieldByField(testParecerItens);
    }

    @Test
    @Transactional
    public void createParecerItensWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parecerItensRepository.findAll().size();

        // Create the ParecerItens with an existing ID
        ParecerItens existingParecerItens = new ParecerItens();
        existingParecerItens.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParecerItensMockMvc.perform(post("/api/parecer-itens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingParecerItens)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ParecerItens> parecerItensList = parecerItensRepository.findAll();
        assertThat(parecerItensList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = parecerItensRepository.findAll().size();
        // set the field null
        parecerItens.setTipo(null);

        // Create the ParecerItens, which fails.

        restParecerItensMockMvc.perform(post("/api/parecer-itens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parecerItens)))
            .andExpect(status().isBadRequest());

        List<ParecerItens> parecerItensList = parecerItensRepository.findAll();
        assertThat(parecerItensList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParecerItens() throws Exception {
        // Initialize the database
        parecerItensRepository.saveAndFlush(parecerItens);

        // Get all the parecerItensList
        restParecerItensMockMvc.perform(get("/api/parecer-itens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parecerItens.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].paritDescricao").value(hasItem(DEFAULT_PARIT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].paritSequencia").value(hasItem(DEFAULT_PARIT_SEQUENCIA)));
    }

    @Test
    @Transactional
    public void getParecerItens() throws Exception {
        // Initialize the database
        parecerItensRepository.saveAndFlush(parecerItens);

        // Get the parecerItens
        restParecerItensMockMvc.perform(get("/api/parecer-itens/{id}", parecerItens.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(parecerItens.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.paritDescricao").value(DEFAULT_PARIT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.paritSequencia").value(DEFAULT_PARIT_SEQUENCIA));
    }

    @Test
    @Transactional
    public void getNonExistingParecerItens() throws Exception {
        // Get the parecerItens
        restParecerItensMockMvc.perform(get("/api/parecer-itens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParecerItens() throws Exception {
        // Initialize the database
        parecerItensRepository.saveAndFlush(parecerItens);
        parecerItensSearchRepository.save(parecerItens);
        int databaseSizeBeforeUpdate = parecerItensRepository.findAll().size();

        // Update the parecerItens
        ParecerItens updatedParecerItens = parecerItensRepository.findOne(parecerItens.getId());
        updatedParecerItens
                .tipo(UPDATED_TIPO)
                .paritDescricao(UPDATED_PARIT_DESCRICAO)
                .paritSequencia(UPDATED_PARIT_SEQUENCIA);

        restParecerItensMockMvc.perform(put("/api/parecer-itens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParecerItens)))
            .andExpect(status().isOk());

        // Validate the ParecerItens in the database
        List<ParecerItens> parecerItensList = parecerItensRepository.findAll();
        assertThat(parecerItensList).hasSize(databaseSizeBeforeUpdate);
        ParecerItens testParecerItens = parecerItensList.get(parecerItensList.size() - 1);
        assertThat(testParecerItens.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testParecerItens.getParitDescricao()).isEqualTo(UPDATED_PARIT_DESCRICAO);
        assertThat(testParecerItens.getParitSequencia()).isEqualTo(UPDATED_PARIT_SEQUENCIA);

        // Validate the ParecerItens in ElasticSearch
        ParecerItens parecerItensEs = parecerItensSearchRepository.findOne(testParecerItens.getId());
        assertThat(parecerItensEs).isEqualToComparingFieldByField(testParecerItens);
    }

    @Test
    @Transactional
    public void updateNonExistingParecerItens() throws Exception {
        int databaseSizeBeforeUpdate = parecerItensRepository.findAll().size();

        // Create the ParecerItens

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restParecerItensMockMvc.perform(put("/api/parecer-itens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parecerItens)))
            .andExpect(status().isCreated());

        // Validate the ParecerItens in the database
        List<ParecerItens> parecerItensList = parecerItensRepository.findAll();
        assertThat(parecerItensList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteParecerItens() throws Exception {
        // Initialize the database
        parecerItensRepository.saveAndFlush(parecerItens);
        parecerItensSearchRepository.save(parecerItens);
        int databaseSizeBeforeDelete = parecerItensRepository.findAll().size();

        // Get the parecerItens
        restParecerItensMockMvc.perform(delete("/api/parecer-itens/{id}", parecerItens.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean parecerItensExistsInEs = parecerItensSearchRepository.exists(parecerItens.getId());
        assertThat(parecerItensExistsInEs).isFalse();

        // Validate the database is empty
        List<ParecerItens> parecerItensList = parecerItensRepository.findAll();
        assertThat(parecerItensList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchParecerItens() throws Exception {
        // Initialize the database
        parecerItensRepository.saveAndFlush(parecerItens);
        parecerItensSearchRepository.save(parecerItens);

        // Search the parecerItens
        restParecerItensMockMvc.perform(get("/api/_search/parecer-itens?query=id:" + parecerItens.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parecerItens.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].paritDescricao").value(hasItem(DEFAULT_PARIT_DESCRICAO.toString())))
            .andExpect(jsonPath("$.[*].paritSequencia").value(hasItem(DEFAULT_PARIT_SEQUENCIA)));
    }
}
