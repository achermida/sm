package br.gov.sc.fatma.sinfat.web.rest;

import br.gov.sc.fatma.sinfat.SmApp;

import br.gov.sc.fatma.sinfat.domain.Endereco;
import br.gov.sc.fatma.sinfat.repository.EnderecoRepository;
import br.gov.sc.fatma.sinfat.repository.search.EnderecoSearchRepository;

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

/**
 * Test class for the EnderecoResource REST controller.
 *
 * @see EnderecoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmApp.class)
public class EnderecoResourceIntTest {

    private static final String DEFAULT_LOGRADOURO = "AAAAAAAAAA";
    private static final String UPDATED_LOGRADOURO = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLEMENTO = "AAAAAAAAAA";
    private static final String UPDATED_COMPLEMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    @Inject
    private EnderecoRepository enderecoRepository;

    @Inject
    private EnderecoSearchRepository enderecoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEnderecoMockMvc;

    private Endereco endereco;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnderecoResource enderecoResource = new EnderecoResource();
        ReflectionTestUtils.setField(enderecoResource, "enderecoSearchRepository", enderecoSearchRepository);
        ReflectionTestUtils.setField(enderecoResource, "enderecoRepository", enderecoRepository);
        this.restEnderecoMockMvc = MockMvcBuilders.standaloneSetup(enderecoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endereco createEntity(EntityManager em) {
        Endereco endereco = new Endereco()
                .logradouro(DEFAULT_LOGRADOURO)
                .numero(DEFAULT_NUMERO)
                .bairro(DEFAULT_BAIRRO)
                .complemento(DEFAULT_COMPLEMENTO)
                .cep(DEFAULT_CEP);
        return endereco;
    }

    @Before
    public void initTest() {
        enderecoSearchRepository.deleteAll();
        endereco = createEntity(em);
    }

    @Test
    @Transactional
    public void createEndereco() throws Exception {
        int databaseSizeBeforeCreate = enderecoRepository.findAll().size();

        // Create the Endereco

        restEnderecoMockMvc.perform(post("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endereco)))
            .andExpect(status().isCreated());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeCreate + 1);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getLogradouro()).isEqualTo(DEFAULT_LOGRADOURO);
        assertThat(testEndereco.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testEndereco.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testEndereco.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testEndereco.getCep()).isEqualTo(DEFAULT_CEP);

        // Validate the Endereco in ElasticSearch
        Endereco enderecoEs = enderecoSearchRepository.findOne(testEndereco.getId());
        assertThat(enderecoEs).isEqualToComparingFieldByField(testEndereco);
    }

    @Test
    @Transactional
    public void createEnderecoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = enderecoRepository.findAll().size();

        // Create the Endereco with an existing ID
        Endereco existingEndereco = new Endereco();
        existingEndereco.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnderecoMockMvc.perform(post("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingEndereco)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLogradouroIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        // set the field null
        endereco.setLogradouro(null);

        // Create the Endereco, which fails.

        restEnderecoMockMvc.perform(post("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endereco)))
            .andExpect(status().isBadRequest());

        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBairroIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        // set the field null
        endereco.setBairro(null);

        // Create the Endereco, which fails.

        restEnderecoMockMvc.perform(post("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endereco)))
            .andExpect(status().isBadRequest());

        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCepIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        // set the field null
        endereco.setCep(null);

        // Create the Endereco, which fails.

        restEnderecoMockMvc.perform(post("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endereco)))
            .andExpect(status().isBadRequest());

        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEnderecos() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList
        restEnderecoMockMvc.perform(get("/api/enderecos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
            .andExpect(jsonPath("$.[*].logradouro").value(hasItem(DEFAULT_LOGRADOURO.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())));
    }

    @Test
    @Transactional
    public void getEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get the endereco
        restEnderecoMockMvc.perform(get("/api/enderecos/{id}", endereco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(endereco.getId().intValue()))
            .andExpect(jsonPath("$.logradouro").value(DEFAULT_LOGRADOURO.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO.toString()))
            .andExpect(jsonPath("$.complemento").value(DEFAULT_COMPLEMENTO.toString()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEndereco() throws Exception {
        // Get the endereco
        restEnderecoMockMvc.perform(get("/api/enderecos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);
        enderecoSearchRepository.save(endereco);
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();

        // Update the endereco
        Endereco updatedEndereco = enderecoRepository.findOne(endereco.getId());
        updatedEndereco
                .logradouro(UPDATED_LOGRADOURO)
                .numero(UPDATED_NUMERO)
                .bairro(UPDATED_BAIRRO)
                .complemento(UPDATED_COMPLEMENTO)
                .cep(UPDATED_CEP);

        restEnderecoMockMvc.perform(put("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEndereco)))
            .andExpect(status().isOk());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testEndereco.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEndereco.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testEndereco.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testEndereco.getCep()).isEqualTo(UPDATED_CEP);

        // Validate the Endereco in ElasticSearch
        Endereco enderecoEs = enderecoSearchRepository.findOne(testEndereco.getId());
        assertThat(enderecoEs).isEqualToComparingFieldByField(testEndereco);
    }

    @Test
    @Transactional
    public void updateNonExistingEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();

        // Create the Endereco

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEnderecoMockMvc.perform(put("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endereco)))
            .andExpect(status().isCreated());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);
        enderecoSearchRepository.save(endereco);
        int databaseSizeBeforeDelete = enderecoRepository.findAll().size();

        // Get the endereco
        restEnderecoMockMvc.perform(delete("/api/enderecos/{id}", endereco.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean enderecoExistsInEs = enderecoSearchRepository.exists(endereco.getId());
        assertThat(enderecoExistsInEs).isFalse();

        // Validate the database is empty
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);
        enderecoSearchRepository.save(endereco);

        // Search the endereco
        restEnderecoMockMvc.perform(get("/api/_search/enderecos?query=id:" + endereco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
            .andExpect(jsonPath("$.[*].logradouro").value(hasItem(DEFAULT_LOGRADOURO.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO.toString())))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())));
    }
}
