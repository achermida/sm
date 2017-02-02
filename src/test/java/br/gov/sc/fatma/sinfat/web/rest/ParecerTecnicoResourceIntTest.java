package br.gov.sc.fatma.sinfat.web.rest;

import br.gov.sc.fatma.sinfat.SmApp;

import br.gov.sc.fatma.sinfat.domain.ParecerTecnico;
import br.gov.sc.fatma.sinfat.repository.ParecerTecnicoRepository;
import br.gov.sc.fatma.sinfat.repository.search.ParecerTecnicoSearchRepository;

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
 * Test class for the ParecerTecnicoResource REST controller.
 *
 * @see ParecerTecnicoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmApp.class)
public class ParecerTecnicoResourceIntTest {

    private static final StatusGeralEnum DEFAULT_STATUS = StatusGeralEnum.ATIVO;
    private static final StatusGeralEnum UPDATED_STATUS = StatusGeralEnum.CANCELADO;

    private static final String DEFAULT_PAREC_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_PAREC_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_PAREC_OBJETIVO = "AAAAAAAAAA";
    private static final String UPDATED_PAREC_OBJETIVO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PAREC_ATENDIMENTO_IN = false;
    private static final Boolean UPDATED_PAREC_ATENDIMENTO_IN = true;

    private static final String DEFAULT_PAREC_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_PAREC_OBSERVACAO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PAREC_DATA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAREC_DATA_INICIO = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ParecerTecnicoRepository parecerTecnicoRepository;

    @Inject
    private ParecerTecnicoSearchRepository parecerTecnicoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restParecerTecnicoMockMvc;

    private ParecerTecnico parecerTecnico;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ParecerTecnicoResource parecerTecnicoResource = new ParecerTecnicoResource();
        ReflectionTestUtils.setField(parecerTecnicoResource, "parecerTecnicoSearchRepository", parecerTecnicoSearchRepository);
        ReflectionTestUtils.setField(parecerTecnicoResource, "parecerTecnicoRepository", parecerTecnicoRepository);
        this.restParecerTecnicoMockMvc = MockMvcBuilders.standaloneSetup(parecerTecnicoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParecerTecnico createEntity(EntityManager em) {
        ParecerTecnico parecerTecnico = new ParecerTecnico()
                .status(DEFAULT_STATUS)
                .parecNumero(DEFAULT_PAREC_NUMERO)
                .parecObjetivo(DEFAULT_PAREC_OBJETIVO)
                .parecAtendimentoIn(DEFAULT_PAREC_ATENDIMENTO_IN)
                .parecObservacao(DEFAULT_PAREC_OBSERVACAO)
                .parecDataInicio(DEFAULT_PAREC_DATA_INICIO);
        return parecerTecnico;
    }

    @Before
    public void initTest() {
        parecerTecnicoSearchRepository.deleteAll();
        parecerTecnico = createEntity(em);
    }

    @Test
    @Transactional
    public void createParecerTecnico() throws Exception {
        int databaseSizeBeforeCreate = parecerTecnicoRepository.findAll().size();

        // Create the ParecerTecnico

        restParecerTecnicoMockMvc.perform(post("/api/parecer-tecnicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parecerTecnico)))
            .andExpect(status().isCreated());

        // Validate the ParecerTecnico in the database
        List<ParecerTecnico> parecerTecnicoList = parecerTecnicoRepository.findAll();
        assertThat(parecerTecnicoList).hasSize(databaseSizeBeforeCreate + 1);
        ParecerTecnico testParecerTecnico = parecerTecnicoList.get(parecerTecnicoList.size() - 1);
        assertThat(testParecerTecnico.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testParecerTecnico.getParecNumero()).isEqualTo(DEFAULT_PAREC_NUMERO);
        assertThat(testParecerTecnico.getParecObjetivo()).isEqualTo(DEFAULT_PAREC_OBJETIVO);
        assertThat(testParecerTecnico.isParecAtendimentoIn()).isEqualTo(DEFAULT_PAREC_ATENDIMENTO_IN);
        assertThat(testParecerTecnico.getParecObservacao()).isEqualTo(DEFAULT_PAREC_OBSERVACAO);
        assertThat(testParecerTecnico.getParecDataInicio()).isEqualTo(DEFAULT_PAREC_DATA_INICIO);

        // Validate the ParecerTecnico in ElasticSearch
        ParecerTecnico parecerTecnicoEs = parecerTecnicoSearchRepository.findOne(testParecerTecnico.getId());
        assertThat(parecerTecnicoEs).isEqualToComparingFieldByField(testParecerTecnico);
    }

    @Test
    @Transactional
    public void createParecerTecnicoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parecerTecnicoRepository.findAll().size();

        // Create the ParecerTecnico with an existing ID
        ParecerTecnico existingParecerTecnico = new ParecerTecnico();
        existingParecerTecnico.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParecerTecnicoMockMvc.perform(post("/api/parecer-tecnicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingParecerTecnico)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ParecerTecnico> parecerTecnicoList = parecerTecnicoRepository.findAll();
        assertThat(parecerTecnicoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = parecerTecnicoRepository.findAll().size();
        // set the field null
        parecerTecnico.setStatus(null);

        // Create the ParecerTecnico, which fails.

        restParecerTecnicoMockMvc.perform(post("/api/parecer-tecnicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parecerTecnico)))
            .andExpect(status().isBadRequest());

        List<ParecerTecnico> parecerTecnicoList = parecerTecnicoRepository.findAll();
        assertThat(parecerTecnicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkParecNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = parecerTecnicoRepository.findAll().size();
        // set the field null
        parecerTecnico.setParecNumero(null);

        // Create the ParecerTecnico, which fails.

        restParecerTecnicoMockMvc.perform(post("/api/parecer-tecnicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parecerTecnico)))
            .andExpect(status().isBadRequest());

        List<ParecerTecnico> parecerTecnicoList = parecerTecnicoRepository.findAll();
        assertThat(parecerTecnicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkParecObjetivoIsRequired() throws Exception {
        int databaseSizeBeforeTest = parecerTecnicoRepository.findAll().size();
        // set the field null
        parecerTecnico.setParecObjetivo(null);

        // Create the ParecerTecnico, which fails.

        restParecerTecnicoMockMvc.perform(post("/api/parecer-tecnicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parecerTecnico)))
            .andExpect(status().isBadRequest());

        List<ParecerTecnico> parecerTecnicoList = parecerTecnicoRepository.findAll();
        assertThat(parecerTecnicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkParecObservacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = parecerTecnicoRepository.findAll().size();
        // set the field null
        parecerTecnico.setParecObservacao(null);

        // Create the ParecerTecnico, which fails.

        restParecerTecnicoMockMvc.perform(post("/api/parecer-tecnicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parecerTecnico)))
            .andExpect(status().isBadRequest());

        List<ParecerTecnico> parecerTecnicoList = parecerTecnicoRepository.findAll();
        assertThat(parecerTecnicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkParecDataInicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = parecerTecnicoRepository.findAll().size();
        // set the field null
        parecerTecnico.setParecDataInicio(null);

        // Create the ParecerTecnico, which fails.

        restParecerTecnicoMockMvc.perform(post("/api/parecer-tecnicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parecerTecnico)))
            .andExpect(status().isBadRequest());

        List<ParecerTecnico> parecerTecnicoList = parecerTecnicoRepository.findAll();
        assertThat(parecerTecnicoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParecerTecnicos() throws Exception {
        // Initialize the database
        parecerTecnicoRepository.saveAndFlush(parecerTecnico);

        // Get all the parecerTecnicoList
        restParecerTecnicoMockMvc.perform(get("/api/parecer-tecnicos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parecerTecnico.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].parecNumero").value(hasItem(DEFAULT_PAREC_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].parecObjetivo").value(hasItem(DEFAULT_PAREC_OBJETIVO.toString())))
            .andExpect(jsonPath("$.[*].parecAtendimentoIn").value(hasItem(DEFAULT_PAREC_ATENDIMENTO_IN.booleanValue())))
            .andExpect(jsonPath("$.[*].parecObservacao").value(hasItem(DEFAULT_PAREC_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].parecDataInicio").value(hasItem(DEFAULT_PAREC_DATA_INICIO.toString())));
    }

    @Test
    @Transactional
    public void getParecerTecnico() throws Exception {
        // Initialize the database
        parecerTecnicoRepository.saveAndFlush(parecerTecnico);

        // Get the parecerTecnico
        restParecerTecnicoMockMvc.perform(get("/api/parecer-tecnicos/{id}", parecerTecnico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(parecerTecnico.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.parecNumero").value(DEFAULT_PAREC_NUMERO.toString()))
            .andExpect(jsonPath("$.parecObjetivo").value(DEFAULT_PAREC_OBJETIVO.toString()))
            .andExpect(jsonPath("$.parecAtendimentoIn").value(DEFAULT_PAREC_ATENDIMENTO_IN.booleanValue()))
            .andExpect(jsonPath("$.parecObservacao").value(DEFAULT_PAREC_OBSERVACAO.toString()))
            .andExpect(jsonPath("$.parecDataInicio").value(DEFAULT_PAREC_DATA_INICIO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParecerTecnico() throws Exception {
        // Get the parecerTecnico
        restParecerTecnicoMockMvc.perform(get("/api/parecer-tecnicos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParecerTecnico() throws Exception {
        // Initialize the database
        parecerTecnicoRepository.saveAndFlush(parecerTecnico);
        parecerTecnicoSearchRepository.save(parecerTecnico);
        int databaseSizeBeforeUpdate = parecerTecnicoRepository.findAll().size();

        // Update the parecerTecnico
        ParecerTecnico updatedParecerTecnico = parecerTecnicoRepository.findOne(parecerTecnico.getId());
        updatedParecerTecnico
                .status(UPDATED_STATUS)
                .parecNumero(UPDATED_PAREC_NUMERO)
                .parecObjetivo(UPDATED_PAREC_OBJETIVO)
                .parecAtendimentoIn(UPDATED_PAREC_ATENDIMENTO_IN)
                .parecObservacao(UPDATED_PAREC_OBSERVACAO)
                .parecDataInicio(UPDATED_PAREC_DATA_INICIO);

        restParecerTecnicoMockMvc.perform(put("/api/parecer-tecnicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedParecerTecnico)))
            .andExpect(status().isOk());

        // Validate the ParecerTecnico in the database
        List<ParecerTecnico> parecerTecnicoList = parecerTecnicoRepository.findAll();
        assertThat(parecerTecnicoList).hasSize(databaseSizeBeforeUpdate);
        ParecerTecnico testParecerTecnico = parecerTecnicoList.get(parecerTecnicoList.size() - 1);
        assertThat(testParecerTecnico.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testParecerTecnico.getParecNumero()).isEqualTo(UPDATED_PAREC_NUMERO);
        assertThat(testParecerTecnico.getParecObjetivo()).isEqualTo(UPDATED_PAREC_OBJETIVO);
        assertThat(testParecerTecnico.isParecAtendimentoIn()).isEqualTo(UPDATED_PAREC_ATENDIMENTO_IN);
        assertThat(testParecerTecnico.getParecObservacao()).isEqualTo(UPDATED_PAREC_OBSERVACAO);
        assertThat(testParecerTecnico.getParecDataInicio()).isEqualTo(UPDATED_PAREC_DATA_INICIO);

        // Validate the ParecerTecnico in ElasticSearch
        ParecerTecnico parecerTecnicoEs = parecerTecnicoSearchRepository.findOne(testParecerTecnico.getId());
        assertThat(parecerTecnicoEs).isEqualToComparingFieldByField(testParecerTecnico);
    }

    @Test
    @Transactional
    public void updateNonExistingParecerTecnico() throws Exception {
        int databaseSizeBeforeUpdate = parecerTecnicoRepository.findAll().size();

        // Create the ParecerTecnico

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restParecerTecnicoMockMvc.perform(put("/api/parecer-tecnicos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parecerTecnico)))
            .andExpect(status().isCreated());

        // Validate the ParecerTecnico in the database
        List<ParecerTecnico> parecerTecnicoList = parecerTecnicoRepository.findAll();
        assertThat(parecerTecnicoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteParecerTecnico() throws Exception {
        // Initialize the database
        parecerTecnicoRepository.saveAndFlush(parecerTecnico);
        parecerTecnicoSearchRepository.save(parecerTecnico);
        int databaseSizeBeforeDelete = parecerTecnicoRepository.findAll().size();

        // Get the parecerTecnico
        restParecerTecnicoMockMvc.perform(delete("/api/parecer-tecnicos/{id}", parecerTecnico.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean parecerTecnicoExistsInEs = parecerTecnicoSearchRepository.exists(parecerTecnico.getId());
        assertThat(parecerTecnicoExistsInEs).isFalse();

        // Validate the database is empty
        List<ParecerTecnico> parecerTecnicoList = parecerTecnicoRepository.findAll();
        assertThat(parecerTecnicoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchParecerTecnico() throws Exception {
        // Initialize the database
        parecerTecnicoRepository.saveAndFlush(parecerTecnico);
        parecerTecnicoSearchRepository.save(parecerTecnico);

        // Search the parecerTecnico
        restParecerTecnicoMockMvc.perform(get("/api/_search/parecer-tecnicos?query=id:" + parecerTecnico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parecerTecnico.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].parecNumero").value(hasItem(DEFAULT_PAREC_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].parecObjetivo").value(hasItem(DEFAULT_PAREC_OBJETIVO.toString())))
            .andExpect(jsonPath("$.[*].parecAtendimentoIn").value(hasItem(DEFAULT_PAREC_ATENDIMENTO_IN.booleanValue())))
            .andExpect(jsonPath("$.[*].parecObservacao").value(hasItem(DEFAULT_PAREC_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].parecDataInicio").value(hasItem(DEFAULT_PAREC_DATA_INICIO.toString())));
    }
}
