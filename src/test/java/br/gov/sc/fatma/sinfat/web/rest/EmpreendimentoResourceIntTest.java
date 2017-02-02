package br.gov.sc.fatma.sinfat.web.rest;

import br.gov.sc.fatma.sinfat.SmApp;

import br.gov.sc.fatma.sinfat.domain.Empreendimento;
import br.gov.sc.fatma.sinfat.repository.EmpreendimentoRepository;
import br.gov.sc.fatma.sinfat.repository.search.EmpreendimentoSearchRepository;

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
/**
 * Test class for the EmpreendimentoResource REST controller.
 *
 * @see EmpreendimentoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmApp.class)
public class EmpreendimentoResourceIntTest {

    private static final String DEFAULT_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBB";

    private static final String DEFAULT_RAZAO_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAZAO_SOCIAL = "BBBBBBBBBB";

    private static final Double DEFAULT_X = 1D;
    private static final Double UPDATED_X = 2D;

    private static final Double DEFAULT_Y = 1D;
    private static final Double UPDATED_Y = 2D;

    private static final LocalDate DEFAULT_DT_CADASTRO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DT_CADASTRO = LocalDate.now(ZoneId.systemDefault());

    private static final StatusGeralEnum DEFAULT_STATUS = StatusGeralEnum.ATIVO;
    private static final StatusGeralEnum UPDATED_STATUS = StatusGeralEnum.CANCELADO;

    @Inject
    private EmpreendimentoRepository empreendimentoRepository;

    @Inject
    private EmpreendimentoSearchRepository empreendimentoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEmpreendimentoMockMvc;

    private Empreendimento empreendimento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmpreendimentoResource empreendimentoResource = new EmpreendimentoResource();
        ReflectionTestUtils.setField(empreendimentoResource, "empreendimentoSearchRepository", empreendimentoSearchRepository);
        ReflectionTestUtils.setField(empreendimentoResource, "empreendimentoRepository", empreendimentoRepository);
        this.restEmpreendimentoMockMvc = MockMvcBuilders.standaloneSetup(empreendimentoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empreendimento createEntity(EntityManager em) {
        Empreendimento empreendimento = new Empreendimento()
                .cnpj(DEFAULT_CNPJ)
                .razaoSocial(DEFAULT_RAZAO_SOCIAL)
                .x(DEFAULT_X)
                .y(DEFAULT_Y)
                .dtCadastro(DEFAULT_DT_CADASTRO)
                .status(DEFAULT_STATUS);
        return empreendimento;
    }

    @Before
    public void initTest() {
        empreendimentoSearchRepository.deleteAll();
        empreendimento = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmpreendimento() throws Exception {
        int databaseSizeBeforeCreate = empreendimentoRepository.findAll().size();

        // Create the Empreendimento

        restEmpreendimentoMockMvc.perform(post("/api/empreendimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empreendimento)))
            .andExpect(status().isCreated());

        // Validate the Empreendimento in the database
        List<Empreendimento> empreendimentoList = empreendimentoRepository.findAll();
        assertThat(empreendimentoList).hasSize(databaseSizeBeforeCreate + 1);
        Empreendimento testEmpreendimento = empreendimentoList.get(empreendimentoList.size() - 1);
        assertThat(testEmpreendimento.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testEmpreendimento.getRazaoSocial()).isEqualTo(DEFAULT_RAZAO_SOCIAL);
        assertThat(testEmpreendimento.getX()).isEqualTo(DEFAULT_X);
        assertThat(testEmpreendimento.getY()).isEqualTo(DEFAULT_Y);
        assertThat(testEmpreendimento.getDtCadastro()).isEqualTo(DEFAULT_DT_CADASTRO);
        assertThat(testEmpreendimento.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Empreendimento in ElasticSearch
        Empreendimento empreendimentoEs = empreendimentoSearchRepository.findOne(testEmpreendimento.getId());
        assertThat(empreendimentoEs).isEqualToComparingFieldByField(testEmpreendimento);
    }

    @Test
    @Transactional
    public void createEmpreendimentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = empreendimentoRepository.findAll().size();

        // Create the Empreendimento with an existing ID
        Empreendimento existingEmpreendimento = new Empreendimento();
        existingEmpreendimento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpreendimentoMockMvc.perform(post("/api/empreendimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEmpreendimento)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Empreendimento> empreendimentoList = empreendimentoRepository.findAll();
        assertThat(empreendimentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCnpjIsRequired() throws Exception {
        int databaseSizeBeforeTest = empreendimentoRepository.findAll().size();
        // set the field null
        empreendimento.setCnpj(null);

        // Create the Empreendimento, which fails.

        restEmpreendimentoMockMvc.perform(post("/api/empreendimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empreendimento)))
            .andExpect(status().isBadRequest());

        List<Empreendimento> empreendimentoList = empreendimentoRepository.findAll();
        assertThat(empreendimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkXIsRequired() throws Exception {
        int databaseSizeBeforeTest = empreendimentoRepository.findAll().size();
        // set the field null
        empreendimento.setX(null);

        // Create the Empreendimento, which fails.

        restEmpreendimentoMockMvc.perform(post("/api/empreendimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empreendimento)))
            .andExpect(status().isBadRequest());

        List<Empreendimento> empreendimentoList = empreendimentoRepository.findAll();
        assertThat(empreendimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYIsRequired() throws Exception {
        int databaseSizeBeforeTest = empreendimentoRepository.findAll().size();
        // set the field null
        empreendimento.setY(null);

        // Create the Empreendimento, which fails.

        restEmpreendimentoMockMvc.perform(post("/api/empreendimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empreendimento)))
            .andExpect(status().isBadRequest());

        List<Empreendimento> empreendimentoList = empreendimentoRepository.findAll();
        assertThat(empreendimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDtCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = empreendimentoRepository.findAll().size();
        // set the field null
        empreendimento.setDtCadastro(null);

        // Create the Empreendimento, which fails.

        restEmpreendimentoMockMvc.perform(post("/api/empreendimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empreendimento)))
            .andExpect(status().isBadRequest());

        List<Empreendimento> empreendimentoList = empreendimentoRepository.findAll();
        assertThat(empreendimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = empreendimentoRepository.findAll().size();
        // set the field null
        empreendimento.setStatus(null);

        // Create the Empreendimento, which fails.

        restEmpreendimentoMockMvc.perform(post("/api/empreendimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empreendimento)))
            .andExpect(status().isBadRequest());

        List<Empreendimento> empreendimentoList = empreendimentoRepository.findAll();
        assertThat(empreendimentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmpreendimentos() throws Exception {
        // Initialize the database
        empreendimentoRepository.saveAndFlush(empreendimento);

        // Get all the empreendimentoList
        restEmpreendimentoMockMvc.perform(get("/api/empreendimentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empreendimento.getId().intValue())))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ.toString())))
            .andExpect(jsonPath("$.[*].razaoSocial").value(hasItem(DEFAULT_RAZAO_SOCIAL.toString())))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].dtCadastro").value(hasItem(DEFAULT_DT_CADASTRO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getEmpreendimento() throws Exception {
        // Initialize the database
        empreendimentoRepository.saveAndFlush(empreendimento);

        // Get the empreendimento
        restEmpreendimentoMockMvc.perform(get("/api/empreendimentos/{id}", empreendimento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(empreendimento.getId().intValue()))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ.toString()))
            .andExpect(jsonPath("$.razaoSocial").value(DEFAULT_RAZAO_SOCIAL.toString()))
            .andExpect(jsonPath("$.x").value(DEFAULT_X.doubleValue()))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y.doubleValue()))
            .andExpect(jsonPath("$.dtCadastro").value(DEFAULT_DT_CADASTRO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmpreendimento() throws Exception {
        // Get the empreendimento
        restEmpreendimentoMockMvc.perform(get("/api/empreendimentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmpreendimento() throws Exception {
        // Initialize the database
        empreendimentoRepository.saveAndFlush(empreendimento);
        empreendimentoSearchRepository.save(empreendimento);
        int databaseSizeBeforeUpdate = empreendimentoRepository.findAll().size();

        // Update the empreendimento
        Empreendimento updatedEmpreendimento = empreendimentoRepository.findOne(empreendimento.getId());
        updatedEmpreendimento
                .cnpj(UPDATED_CNPJ)
                .razaoSocial(UPDATED_RAZAO_SOCIAL)
                .x(UPDATED_X)
                .y(UPDATED_Y)
                .dtCadastro(UPDATED_DT_CADASTRO)
                .status(UPDATED_STATUS);

        restEmpreendimentoMockMvc.perform(put("/api/empreendimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmpreendimento)))
            .andExpect(status().isOk());

        // Validate the Empreendimento in the database
        List<Empreendimento> empreendimentoList = empreendimentoRepository.findAll();
        assertThat(empreendimentoList).hasSize(databaseSizeBeforeUpdate);
        Empreendimento testEmpreendimento = empreendimentoList.get(empreendimentoList.size() - 1);
        assertThat(testEmpreendimento.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testEmpreendimento.getRazaoSocial()).isEqualTo(UPDATED_RAZAO_SOCIAL);
        assertThat(testEmpreendimento.getX()).isEqualTo(UPDATED_X);
        assertThat(testEmpreendimento.getY()).isEqualTo(UPDATED_Y);
        assertThat(testEmpreendimento.getDtCadastro()).isEqualTo(UPDATED_DT_CADASTRO);
        assertThat(testEmpreendimento.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Empreendimento in ElasticSearch
        Empreendimento empreendimentoEs = empreendimentoSearchRepository.findOne(testEmpreendimento.getId());
        assertThat(empreendimentoEs).isEqualToComparingFieldByField(testEmpreendimento);
    }

    @Test
    @Transactional
    public void updateNonExistingEmpreendimento() throws Exception {
        int databaseSizeBeforeUpdate = empreendimentoRepository.findAll().size();

        // Create the Empreendimento

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmpreendimentoMockMvc.perform(put("/api/empreendimentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(empreendimento)))
            .andExpect(status().isCreated());

        // Validate the Empreendimento in the database
        List<Empreendimento> empreendimentoList = empreendimentoRepository.findAll();
        assertThat(empreendimentoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEmpreendimento() throws Exception {
        // Initialize the database
        empreendimentoRepository.saveAndFlush(empreendimento);
        empreendimentoSearchRepository.save(empreendimento);
        int databaseSizeBeforeDelete = empreendimentoRepository.findAll().size();

        // Get the empreendimento
        restEmpreendimentoMockMvc.perform(delete("/api/empreendimentos/{id}", empreendimento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean empreendimentoExistsInEs = empreendimentoSearchRepository.exists(empreendimento.getId());
        assertThat(empreendimentoExistsInEs).isFalse();

        // Validate the database is empty
        List<Empreendimento> empreendimentoList = empreendimentoRepository.findAll();
        assertThat(empreendimentoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEmpreendimento() throws Exception {
        // Initialize the database
        empreendimentoRepository.saveAndFlush(empreendimento);
        empreendimentoSearchRepository.save(empreendimento);

        // Search the empreendimento
        restEmpreendimentoMockMvc.perform(get("/api/_search/empreendimentos?query=id:" + empreendimento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empreendimento.getId().intValue())))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ.toString())))
            .andExpect(jsonPath("$.[*].razaoSocial").value(hasItem(DEFAULT_RAZAO_SOCIAL.toString())))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].dtCadastro").value(hasItem(DEFAULT_DT_CADASTRO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
}
