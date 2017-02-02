package br.gov.sc.fatma.sinfat.web.rest;

import br.gov.sc.fatma.sinfat.SmApp;

import br.gov.sc.fatma.sinfat.domain.Processo;
import br.gov.sc.fatma.sinfat.repository.ProcessoRepository;
import br.gov.sc.fatma.sinfat.service.ProcessoService;
import br.gov.sc.fatma.sinfat.repository.search.ProcessoSearchRepository;

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
 * Test class for the ProcessoResource REST controller.
 *
 * @see ProcessoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmApp.class)
public class ProcessoResourceIntTest {

    private static final StatusGeralEnum DEFAULT_STATUS = StatusGeralEnum.ATIVO;
    private static final StatusGeralEnum UPDATED_STATUS = StatusGeralEnum.CANCELADO;

    private static final String DEFAULT_PROC_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_PROC_NUMERO = "BBBBBBBBBB";

    @Inject
    private ProcessoRepository processoRepository;

    @Inject
    private ProcessoService processoService;

    @Inject
    private ProcessoSearchRepository processoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProcessoMockMvc;

    private Processo processo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProcessoResource processoResource = new ProcessoResource();
        ReflectionTestUtils.setField(processoResource, "processoService", processoService);
        this.restProcessoMockMvc = MockMvcBuilders.standaloneSetup(processoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Processo createEntity(EntityManager em) {
        Processo processo = new Processo()
                .status(DEFAULT_STATUS)
                .procNumero(DEFAULT_PROC_NUMERO);
        return processo;
    }

    @Before
    public void initTest() {
        processoSearchRepository.deleteAll();
        processo = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcesso() throws Exception {
        int databaseSizeBeforeCreate = processoRepository.findAll().size();

        // Create the Processo

        restProcessoMockMvc.perform(post("/api/processos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processo)))
            .andExpect(status().isCreated());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeCreate + 1);
        Processo testProcesso = processoList.get(processoList.size() - 1);
        assertThat(testProcesso.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProcesso.getProcNumero()).isEqualTo(DEFAULT_PROC_NUMERO);

        // Validate the Processo in ElasticSearch
        Processo processoEs = processoSearchRepository.findOne(testProcesso.getId());
        assertThat(processoEs).isEqualToComparingFieldByField(testProcesso);
    }

    @Test
    @Transactional
    public void createProcessoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = processoRepository.findAll().size();

        // Create the Processo with an existing ID
        Processo existingProcesso = new Processo();
        existingProcesso.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessoMockMvc.perform(post("/api/processos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingProcesso)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = processoRepository.findAll().size();
        // set the field null
        processo.setStatus(null);

        // Create the Processo, which fails.

        restProcessoMockMvc.perform(post("/api/processos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processo)))
            .andExpect(status().isBadRequest());

        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProcNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = processoRepository.findAll().size();
        // set the field null
        processo.setProcNumero(null);

        // Create the Processo, which fails.

        restProcessoMockMvc.perform(post("/api/processos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processo)))
            .andExpect(status().isBadRequest());

        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProcessos() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get all the processoList
        restProcessoMockMvc.perform(get("/api/processos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processo.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].procNumero").value(hasItem(DEFAULT_PROC_NUMERO.toString())));
    }

    @Test
    @Transactional
    public void getProcesso() throws Exception {
        // Initialize the database
        processoRepository.saveAndFlush(processo);

        // Get the processo
        restProcessoMockMvc.perform(get("/api/processos/{id}", processo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(processo.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.procNumero").value(DEFAULT_PROC_NUMERO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProcesso() throws Exception {
        // Get the processo
        restProcessoMockMvc.perform(get("/api/processos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcesso() throws Exception {
        // Initialize the database
        processoService.save(processo);

        int databaseSizeBeforeUpdate = processoRepository.findAll().size();

        // Update the processo
        Processo updatedProcesso = processoRepository.findOne(processo.getId());
        updatedProcesso
                .status(UPDATED_STATUS)
                .procNumero(UPDATED_PROC_NUMERO);

        restProcessoMockMvc.perform(put("/api/processos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProcesso)))
            .andExpect(status().isOk());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate);
        Processo testProcesso = processoList.get(processoList.size() - 1);
        assertThat(testProcesso.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProcesso.getProcNumero()).isEqualTo(UPDATED_PROC_NUMERO);

        // Validate the Processo in ElasticSearch
        Processo processoEs = processoSearchRepository.findOne(testProcesso.getId());
        assertThat(processoEs).isEqualToComparingFieldByField(testProcesso);
    }

    @Test
    @Transactional
    public void updateNonExistingProcesso() throws Exception {
        int databaseSizeBeforeUpdate = processoRepository.findAll().size();

        // Create the Processo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcessoMockMvc.perform(put("/api/processos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processo)))
            .andExpect(status().isCreated());

        // Validate the Processo in the database
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcesso() throws Exception {
        // Initialize the database
        processoService.save(processo);

        int databaseSizeBeforeDelete = processoRepository.findAll().size();

        // Get the processo
        restProcessoMockMvc.perform(delete("/api/processos/{id}", processo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean processoExistsInEs = processoSearchRepository.exists(processo.getId());
        assertThat(processoExistsInEs).isFalse();

        // Validate the database is empty
        List<Processo> processoList = processoRepository.findAll();
        assertThat(processoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProcesso() throws Exception {
        // Initialize the database
        processoService.save(processo);

        // Search the processo
        restProcessoMockMvc.perform(get("/api/_search/processos?query=id:" + processo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processo.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].procNumero").value(hasItem(DEFAULT_PROC_NUMERO.toString())));
    }
}
