package br.gov.sc.fatma.sinfat.web.rest;

import br.gov.sc.fatma.sinfat.SmApp;

import br.gov.sc.fatma.sinfat.domain.Requerimento;
import br.gov.sc.fatma.sinfat.repository.RequerimentoRepository;
import br.gov.sc.fatma.sinfat.service.RequerimentoService;
import br.gov.sc.fatma.sinfat.repository.search.RequerimentoSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.gov.sc.fatma.sinfat.domain.enumeration.StatusGeralEnum;
import br.gov.sc.fatma.sinfat.domain.enumeration.ReqFaseEnum;
/**
 * Test class for the RequerimentoResource REST controller.
 *
 * @see RequerimentoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmApp.class)
public class RequerimentoResourceIntTest {

    private static final LocalDate DEFAULT_DT_CADASTRO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DT_CADASTRO = LocalDate.now(ZoneId.systemDefault());

    private static final StatusGeralEnum DEFAULT_STATUS = StatusGeralEnum.ATIVO;
    private static final StatusGeralEnum UPDATED_STATUS = StatusGeralEnum.CANCELADO;

    private static final ReqFaseEnum DEFAULT_FASE = ReqFaseEnum.CADASTRADO;
    private static final ReqFaseEnum UPDATED_FASE = ReqFaseEnum.FORMALIZADO;

    private static final String DEFAULT_OBS = "AAAAAAAAAA";
    private static final String UPDATED_OBS = "BBBBBBBBBB";

    @Inject
    private RequerimentoRepository requerimentoRepository;

    @Inject
    private RequerimentoService requerimentoService;

    @Inject
    private RequerimentoSearchRepository requerimentoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRequerimentoMockMvc;

    private Requerimento requerimento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RequerimentoResource requerimentoResource = new RequerimentoResource();
        ReflectionTestUtils.setField(requerimentoResource, "requerimentoService", requerimentoService);
        this.restRequerimentoMockMvc = MockMvcBuilders.standaloneSetup(requerimentoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requerimento createEntity(EntityManager em) {
        Requerimento requerimento = new Requerimento()
                .dtCadastro(DEFAULT_DT_CADASTRO)
                .status(DEFAULT_STATUS)
                .fase(DEFAULT_FASE)
                .obs(DEFAULT_OBS);
        return requerimento;
    }

    @Before
    public void initTest() {
        requerimentoSearchRepository.deleteAll();
        requerimento = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequerimento() throws Exception {
        int databaseSizeBeforeCreate = requerimentoRepository.findAll().size();

        // Create the Requerimento

        restRequerimentoMockMvc.perform(post("/api/requerimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requerimento)))
            .andExpect(status().isCreated());

        // Validate the Requerimento in the database
        List<Requerimento> requerimentoList = requerimentoRepository.findAll();
        assertThat(requerimentoList).hasSize(databaseSizeBeforeCreate + 1);
        Requerimento testRequerimento = requerimentoList.get(requerimentoList.size() - 1);
        assertThat(testRequerimento.getDtCadastro()).isEqualTo(DEFAULT_DT_CADASTRO);
        assertThat(testRequerimento.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRequerimento.getFase()).isEqualTo(DEFAULT_FASE);
        assertThat(testRequerimento.getObs()).isEqualTo(DEFAULT_OBS);

        // Validate the Requerimento in ElasticSearch
        Requerimento requerimentoEs = requerimentoSearchRepository.findOne(testRequerimento.getId());
        assertThat(requerimentoEs).isEqualToComparingFieldByField(testRequerimento);
    }

    @Test
    @Transactional
    public void createRequerimentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requerimentoRepository.findAll().size();

        // Create the Requerimento with an existing ID
        Requerimento existingRequerimento = new Requerimento();
        existingRequerimento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequerimentoMockMvc.perform(post("/api/requerimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingRequerimento)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Requerimento> requerimentoList = requerimentoRepository.findAll();
        assertThat(requerimentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDtCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = requerimentoRepository.findAll().size();
        // set the field null
        requerimento.setDtCadastro(null);

        // Create the Requerimento, which fails.

        restRequerimentoMockMvc.perform(post("/api/requerimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requerimento)))
            .andExpect(status().isBadRequest());

        List<Requerimento> requerimentoList = requerimentoRepository.findAll();
        assertThat(requerimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = requerimentoRepository.findAll().size();
        // set the field null
        requerimento.setStatus(null);

        // Create the Requerimento, which fails.

        restRequerimentoMockMvc.perform(post("/api/requerimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requerimento)))
            .andExpect(status().isBadRequest());

        List<Requerimento> requerimentoList = requerimentoRepository.findAll();
        assertThat(requerimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFaseIsRequired() throws Exception {
        int databaseSizeBeforeTest = requerimentoRepository.findAll().size();
        // set the field null
        requerimento.setFase(null);

        // Create the Requerimento, which fails.

        restRequerimentoMockMvc.perform(post("/api/requerimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requerimento)))
            .andExpect(status().isBadRequest());

        List<Requerimento> requerimentoList = requerimentoRepository.findAll();
        assertThat(requerimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRequerimentos() throws Exception {
        // Initialize the database
        requerimentoRepository.saveAndFlush(requerimento);

        // Get all the requerimentoList
        restRequerimentoMockMvc.perform(get("/api/requerimentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requerimento.getId().intValue())))
            .andExpect(jsonPath("$.[*].dtCadastro").value(hasItem(DEFAULT_DT_CADASTRO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].fase").value(hasItem(DEFAULT_FASE.toString())))
            .andExpect(jsonPath("$.[*].obs").value(hasItem(DEFAULT_OBS.toString())));
    }

    @Test
    @Transactional
    public void getRequerimento() throws Exception {
        // Initialize the database
        requerimentoRepository.saveAndFlush(requerimento);

        // Get the requerimento
        restRequerimentoMockMvc.perform(get("/api/requerimentos/{id}", requerimento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(requerimento.getId().intValue()))
            .andExpect(jsonPath("$.dtCadastro").value(DEFAULT_DT_CADASTRO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.fase").value(DEFAULT_FASE.toString()))
            .andExpect(jsonPath("$.obs").value(DEFAULT_OBS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRequerimento() throws Exception {
        // Get the requerimento
        restRequerimentoMockMvc.perform(get("/api/requerimentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequerimento() throws Exception {
        // Initialize the database
        requerimentoService.save(requerimento);

        int databaseSizeBeforeUpdate = requerimentoRepository.findAll().size();

        // Update the requerimento
        Requerimento updatedRequerimento = requerimentoRepository.findOne(requerimento.getId());
        updatedRequerimento
                .dtCadastro(UPDATED_DT_CADASTRO)
                .status(UPDATED_STATUS)
                .fase(UPDATED_FASE)
                .obs(UPDATED_OBS);

        restRequerimentoMockMvc.perform(put("/api/requerimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequerimento)))
            .andExpect(status().isOk());

        // Validate the Requerimento in the database
        List<Requerimento> requerimentoList = requerimentoRepository.findAll();
        assertThat(requerimentoList).hasSize(databaseSizeBeforeUpdate);
        Requerimento testRequerimento = requerimentoList.get(requerimentoList.size() - 1);
        assertThat(testRequerimento.getDtCadastro()).isEqualTo(UPDATED_DT_CADASTRO);
        assertThat(testRequerimento.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRequerimento.getFase()).isEqualTo(UPDATED_FASE);
        assertThat(testRequerimento.getObs()).isEqualTo(UPDATED_OBS);

        // Validate the Requerimento in ElasticSearch
        Requerimento requerimentoEs = requerimentoSearchRepository.findOne(testRequerimento.getId());
        assertThat(requerimentoEs).isEqualToComparingFieldByField(testRequerimento);
    }

    @Test
    @Transactional
    public void updateNonExistingRequerimento() throws Exception {
        int databaseSizeBeforeUpdate = requerimentoRepository.findAll().size();

        // Create the Requerimento

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRequerimentoMockMvc.perform(put("/api/requerimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requerimento)))
            .andExpect(status().isCreated());

        // Validate the Requerimento in the database
        List<Requerimento> requerimentoList = requerimentoRepository.findAll();
        assertThat(requerimentoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRequerimento() throws Exception {
        // Initialize the database
        requerimentoService.save(requerimento);

        int databaseSizeBeforeDelete = requerimentoRepository.findAll().size();

        // Get the requerimento
        restRequerimentoMockMvc.perform(delete("/api/requerimentos/{id}", requerimento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean requerimentoExistsInEs = requerimentoSearchRepository.exists(requerimento.getId());
        assertThat(requerimentoExistsInEs).isFalse();

        // Validate the database is empty
        List<Requerimento> requerimentoList = requerimentoRepository.findAll();
        assertThat(requerimentoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRequerimento() throws Exception {
        // Initialize the database
        requerimentoService.save(requerimento);

        // Search the requerimento
        restRequerimentoMockMvc.perform(get("/api/_search/requerimentos?query=id:" + requerimento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requerimento.getId().intValue())))
            .andExpect(jsonPath("$.[*].dtCadastro").value(hasItem(DEFAULT_DT_CADASTRO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].fase").value(hasItem(DEFAULT_FASE.toString())))
            .andExpect(jsonPath("$.[*].obs").value(hasItem(DEFAULT_OBS.toString())));
    }
}
