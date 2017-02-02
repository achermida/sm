package br.gov.sc.fatma.sinfat.web.rest;

import br.gov.sc.fatma.sinfat.SmApp;

import br.gov.sc.fatma.sinfat.domain.Licenca;
import br.gov.sc.fatma.sinfat.repository.LicencaRepository;
import br.gov.sc.fatma.sinfat.service.LicencaService;
import br.gov.sc.fatma.sinfat.repository.search.LicencaSearchRepository;

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
import br.gov.sc.fatma.sinfat.domain.enumeration.TipoLicencaEnum;
/**
 * Test class for the LicencaResource REST controller.
 *
 * @see LicencaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmApp.class)
public class LicencaResourceIntTest {

    private static final StatusGeralEnum DEFAULT_STATUS = StatusGeralEnum.ATIVO;
    private static final StatusGeralEnum UPDATED_STATUS = StatusGeralEnum.CANCELADO;

    private static final String DEFAULT_LICEN_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_LICEN_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_LICEN_CONDICOES_VALIDADE = "AAAAAAAAAA";
    private static final String UPDATED_LICEN_CONDICOES_VALIDADE = "BBBBBBBBBB";

    private static final Integer DEFAULT_LICEN_VALIDADE = 1;
    private static final Integer UPDATED_LICEN_VALIDADE = 2;

    private static final String DEFAULT_LICEN_CARACTERISTICA = "AAAAAAAAAA";
    private static final String UPDATED_LICEN_CARACTERISTICA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LICEN_DATA_EMISSAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LICEN_DATA_EMISSAO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LICEN_DATA_ENTREGA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LICEN_DATA_ENTREGA = LocalDate.now(ZoneId.systemDefault());

    private static final TipoLicencaEnum DEFAULT_TIPO = TipoLicencaEnum.NP;
    private static final TipoLicencaEnum UPDATED_TIPO = TipoLicencaEnum.LAP;

    @Inject
    private LicencaRepository licencaRepository;

    @Inject
    private LicencaService licencaService;

    @Inject
    private LicencaSearchRepository licencaSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLicencaMockMvc;

    private Licenca licenca;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LicencaResource licencaResource = new LicencaResource();
        ReflectionTestUtils.setField(licencaResource, "licencaService", licencaService);
        this.restLicencaMockMvc = MockMvcBuilders.standaloneSetup(licencaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Licenca createEntity(EntityManager em) {
        Licenca licenca = new Licenca()
                .status(DEFAULT_STATUS)
                .licenNumero(DEFAULT_LICEN_NUMERO)
                .licenCondicoesValidade(DEFAULT_LICEN_CONDICOES_VALIDADE)
                .licenValidade(DEFAULT_LICEN_VALIDADE)
                .licenCaracteristica(DEFAULT_LICEN_CARACTERISTICA)
                .licenDataEmissao(DEFAULT_LICEN_DATA_EMISSAO)
                .licenDataEntrega(DEFAULT_LICEN_DATA_ENTREGA)
                .tipo(DEFAULT_TIPO);
        return licenca;
    }

    @Before
    public void initTest() {
        licencaSearchRepository.deleteAll();
        licenca = createEntity(em);
    }

    @Test
    @Transactional
    public void createLicenca() throws Exception {
        int databaseSizeBeforeCreate = licencaRepository.findAll().size();

        // Create the Licenca

        restLicencaMockMvc.perform(post("/api/licencas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licenca)))
            .andExpect(status().isCreated());

        // Validate the Licenca in the database
        List<Licenca> licencaList = licencaRepository.findAll();
        assertThat(licencaList).hasSize(databaseSizeBeforeCreate + 1);
        Licenca testLicenca = licencaList.get(licencaList.size() - 1);
        assertThat(testLicenca.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLicenca.getLicenNumero()).isEqualTo(DEFAULT_LICEN_NUMERO);
        assertThat(testLicenca.getLicenCondicoesValidade()).isEqualTo(DEFAULT_LICEN_CONDICOES_VALIDADE);
        assertThat(testLicenca.getLicenValidade()).isEqualTo(DEFAULT_LICEN_VALIDADE);
        assertThat(testLicenca.getLicenCaracteristica()).isEqualTo(DEFAULT_LICEN_CARACTERISTICA);
        assertThat(testLicenca.getLicenDataEmissao()).isEqualTo(DEFAULT_LICEN_DATA_EMISSAO);
        assertThat(testLicenca.getLicenDataEntrega()).isEqualTo(DEFAULT_LICEN_DATA_ENTREGA);
        assertThat(testLicenca.getTipo()).isEqualTo(DEFAULT_TIPO);

        // Validate the Licenca in ElasticSearch
        Licenca licencaEs = licencaSearchRepository.findOne(testLicenca.getId());
        assertThat(licencaEs).isEqualToComparingFieldByField(testLicenca);
    }

    @Test
    @Transactional
    public void createLicencaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = licencaRepository.findAll().size();

        // Create the Licenca with an existing ID
        Licenca existingLicenca = new Licenca();
        existingLicenca.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLicencaMockMvc.perform(post("/api/licencas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingLicenca)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Licenca> licencaList = licencaRepository.findAll();
        assertThat(licencaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = licencaRepository.findAll().size();
        // set the field null
        licenca.setStatus(null);

        // Create the Licenca, which fails.

        restLicencaMockMvc.perform(post("/api/licencas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licenca)))
            .andExpect(status().isBadRequest());

        List<Licenca> licencaList = licencaRepository.findAll();
        assertThat(licencaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLicenNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = licencaRepository.findAll().size();
        // set the field null
        licenca.setLicenNumero(null);

        // Create the Licenca, which fails.

        restLicencaMockMvc.perform(post("/api/licencas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licenca)))
            .andExpect(status().isBadRequest());

        List<Licenca> licencaList = licencaRepository.findAll();
        assertThat(licencaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLicenCondicoesValidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = licencaRepository.findAll().size();
        // set the field null
        licenca.setLicenCondicoesValidade(null);

        // Create the Licenca, which fails.

        restLicencaMockMvc.perform(post("/api/licencas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licenca)))
            .andExpect(status().isBadRequest());

        List<Licenca> licencaList = licencaRepository.findAll();
        assertThat(licencaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLicenValidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = licencaRepository.findAll().size();
        // set the field null
        licenca.setLicenValidade(null);

        // Create the Licenca, which fails.

        restLicencaMockMvc.perform(post("/api/licencas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licenca)))
            .andExpect(status().isBadRequest());

        List<Licenca> licencaList = licencaRepository.findAll();
        assertThat(licencaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLicenCaracteristicaIsRequired() throws Exception {
        int databaseSizeBeforeTest = licencaRepository.findAll().size();
        // set the field null
        licenca.setLicenCaracteristica(null);

        // Create the Licenca, which fails.

        restLicencaMockMvc.perform(post("/api/licencas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licenca)))
            .andExpect(status().isBadRequest());

        List<Licenca> licencaList = licencaRepository.findAll();
        assertThat(licencaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLicenDataEmissaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = licencaRepository.findAll().size();
        // set the field null
        licenca.setLicenDataEmissao(null);

        // Create the Licenca, which fails.

        restLicencaMockMvc.perform(post("/api/licencas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licenca)))
            .andExpect(status().isBadRequest());

        List<Licenca> licencaList = licencaRepository.findAll();
        assertThat(licencaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = licencaRepository.findAll().size();
        // set the field null
        licenca.setTipo(null);

        // Create the Licenca, which fails.

        restLicencaMockMvc.perform(post("/api/licencas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licenca)))
            .andExpect(status().isBadRequest());

        List<Licenca> licencaList = licencaRepository.findAll();
        assertThat(licencaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLicencas() throws Exception {
        // Initialize the database
        licencaRepository.saveAndFlush(licenca);

        // Get all the licencaList
        restLicencaMockMvc.perform(get("/api/licencas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(licenca.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].licenNumero").value(hasItem(DEFAULT_LICEN_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].licenCondicoesValidade").value(hasItem(DEFAULT_LICEN_CONDICOES_VALIDADE.toString())))
            .andExpect(jsonPath("$.[*].licenValidade").value(hasItem(DEFAULT_LICEN_VALIDADE)))
            .andExpect(jsonPath("$.[*].licenCaracteristica").value(hasItem(DEFAULT_LICEN_CARACTERISTICA.toString())))
            .andExpect(jsonPath("$.[*].licenDataEmissao").value(hasItem(DEFAULT_LICEN_DATA_EMISSAO.toString())))
            .andExpect(jsonPath("$.[*].licenDataEntrega").value(hasItem(DEFAULT_LICEN_DATA_ENTREGA.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }

    @Test
    @Transactional
    public void getLicenca() throws Exception {
        // Initialize the database
        licencaRepository.saveAndFlush(licenca);

        // Get the licenca
        restLicencaMockMvc.perform(get("/api/licencas/{id}", licenca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(licenca.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.licenNumero").value(DEFAULT_LICEN_NUMERO.toString()))
            .andExpect(jsonPath("$.licenCondicoesValidade").value(DEFAULT_LICEN_CONDICOES_VALIDADE.toString()))
            .andExpect(jsonPath("$.licenValidade").value(DEFAULT_LICEN_VALIDADE))
            .andExpect(jsonPath("$.licenCaracteristica").value(DEFAULT_LICEN_CARACTERISTICA.toString()))
            .andExpect(jsonPath("$.licenDataEmissao").value(DEFAULT_LICEN_DATA_EMISSAO.toString()))
            .andExpect(jsonPath("$.licenDataEntrega").value(DEFAULT_LICEN_DATA_ENTREGA.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLicenca() throws Exception {
        // Get the licenca
        restLicencaMockMvc.perform(get("/api/licencas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLicenca() throws Exception {
        // Initialize the database
        licencaService.save(licenca);

        int databaseSizeBeforeUpdate = licencaRepository.findAll().size();

        // Update the licenca
        Licenca updatedLicenca = licencaRepository.findOne(licenca.getId());
        updatedLicenca
                .status(UPDATED_STATUS)
                .licenNumero(UPDATED_LICEN_NUMERO)
                .licenCondicoesValidade(UPDATED_LICEN_CONDICOES_VALIDADE)
                .licenValidade(UPDATED_LICEN_VALIDADE)
                .licenCaracteristica(UPDATED_LICEN_CARACTERISTICA)
                .licenDataEmissao(UPDATED_LICEN_DATA_EMISSAO)
                .licenDataEntrega(UPDATED_LICEN_DATA_ENTREGA)
                .tipo(UPDATED_TIPO);

        restLicencaMockMvc.perform(put("/api/licencas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLicenca)))
            .andExpect(status().isOk());

        // Validate the Licenca in the database
        List<Licenca> licencaList = licencaRepository.findAll();
        assertThat(licencaList).hasSize(databaseSizeBeforeUpdate);
        Licenca testLicenca = licencaList.get(licencaList.size() - 1);
        assertThat(testLicenca.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLicenca.getLicenNumero()).isEqualTo(UPDATED_LICEN_NUMERO);
        assertThat(testLicenca.getLicenCondicoesValidade()).isEqualTo(UPDATED_LICEN_CONDICOES_VALIDADE);
        assertThat(testLicenca.getLicenValidade()).isEqualTo(UPDATED_LICEN_VALIDADE);
        assertThat(testLicenca.getLicenCaracteristica()).isEqualTo(UPDATED_LICEN_CARACTERISTICA);
        assertThat(testLicenca.getLicenDataEmissao()).isEqualTo(UPDATED_LICEN_DATA_EMISSAO);
        assertThat(testLicenca.getLicenDataEntrega()).isEqualTo(UPDATED_LICEN_DATA_ENTREGA);
        assertThat(testLicenca.getTipo()).isEqualTo(UPDATED_TIPO);

        // Validate the Licenca in ElasticSearch
        Licenca licencaEs = licencaSearchRepository.findOne(testLicenca.getId());
        assertThat(licencaEs).isEqualToComparingFieldByField(testLicenca);
    }

    @Test
    @Transactional
    public void updateNonExistingLicenca() throws Exception {
        int databaseSizeBeforeUpdate = licencaRepository.findAll().size();

        // Create the Licenca

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLicencaMockMvc.perform(put("/api/licencas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(licenca)))
            .andExpect(status().isCreated());

        // Validate the Licenca in the database
        List<Licenca> licencaList = licencaRepository.findAll();
        assertThat(licencaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLicenca() throws Exception {
        // Initialize the database
        licencaService.save(licenca);

        int databaseSizeBeforeDelete = licencaRepository.findAll().size();

        // Get the licenca
        restLicencaMockMvc.perform(delete("/api/licencas/{id}", licenca.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean licencaExistsInEs = licencaSearchRepository.exists(licenca.getId());
        assertThat(licencaExistsInEs).isFalse();

        // Validate the database is empty
        List<Licenca> licencaList = licencaRepository.findAll();
        assertThat(licencaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLicenca() throws Exception {
        // Initialize the database
        licencaService.save(licenca);

        // Search the licenca
        restLicencaMockMvc.perform(get("/api/_search/licencas?query=id:" + licenca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(licenca.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].licenNumero").value(hasItem(DEFAULT_LICEN_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].licenCondicoesValidade").value(hasItem(DEFAULT_LICEN_CONDICOES_VALIDADE.toString())))
            .andExpect(jsonPath("$.[*].licenValidade").value(hasItem(DEFAULT_LICEN_VALIDADE)))
            .andExpect(jsonPath("$.[*].licenCaracteristica").value(hasItem(DEFAULT_LICEN_CARACTERISTICA.toString())))
            .andExpect(jsonPath("$.[*].licenDataEmissao").value(hasItem(DEFAULT_LICEN_DATA_EMISSAO.toString())))
            .andExpect(jsonPath("$.[*].licenDataEntrega").value(hasItem(DEFAULT_LICEN_DATA_ENTREGA.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }
}
