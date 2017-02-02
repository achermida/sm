package br.gov.sc.fatma.sinfat.web.rest;

import br.gov.sc.fatma.sinfat.SmApp;

import br.gov.sc.fatma.sinfat.domain.Contato;
import br.gov.sc.fatma.sinfat.repository.ContatoRepository;
import br.gov.sc.fatma.sinfat.repository.search.ContatoSearchRepository;

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
 * Test class for the ContatoResource REST controller.
 *
 * @see ContatoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmApp.class)
public class ContatoResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    @Inject
    private ContatoRepository contatoRepository;

    @Inject
    private ContatoSearchRepository contatoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restContatoMockMvc;

    private Contato contato;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContatoResource contatoResource = new ContatoResource();
        ReflectionTestUtils.setField(contatoResource, "contatoSearchRepository", contatoSearchRepository);
        ReflectionTestUtils.setField(contatoResource, "contatoRepository", contatoRepository);
        this.restContatoMockMvc = MockMvcBuilders.standaloneSetup(contatoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contato createEntity(EntityManager em) {
        Contato contato = new Contato()
                .email(DEFAULT_EMAIL)
                .telefone(DEFAULT_TELEFONE);
        return contato;
    }

    @Before
    public void initTest() {
        contatoSearchRepository.deleteAll();
        contato = createEntity(em);
    }

    @Test
    @Transactional
    public void createContato() throws Exception {
        int databaseSizeBeforeCreate = contatoRepository.findAll().size();

        // Create the Contato

        restContatoMockMvc.perform(post("/api/contatoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contato)))
            .andExpect(status().isCreated());

        // Validate the Contato in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeCreate + 1);
        Contato testContato = contatoList.get(contatoList.size() - 1);
        assertThat(testContato.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContato.getTelefone()).isEqualTo(DEFAULT_TELEFONE);

        // Validate the Contato in ElasticSearch
        Contato contatoEs = contatoSearchRepository.findOne(testContato.getId());
        assertThat(contatoEs).isEqualToComparingFieldByField(testContato);
    }

    @Test
    @Transactional
    public void createContatoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contatoRepository.findAll().size();

        // Create the Contato with an existing ID
        Contato existingContato = new Contato();
        existingContato.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContatoMockMvc.perform(post("/api/contatoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingContato)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = contatoRepository.findAll().size();
        // set the field null
        contato.setEmail(null);

        // Create the Contato, which fails.

        restContatoMockMvc.perform(post("/api/contatoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contato)))
            .andExpect(status().isBadRequest());

        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContatoes() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get all the contatoList
        restContatoMockMvc.perform(get("/api/contatoes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contato.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE.toString())));
    }

    @Test
    @Transactional
    public void getContato() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);

        // Get the contato
        restContatoMockMvc.perform(get("/api/contatoes/{id}", contato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contato.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContato() throws Exception {
        // Get the contato
        restContatoMockMvc.perform(get("/api/contatoes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContato() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);
        contatoSearchRepository.save(contato);
        int databaseSizeBeforeUpdate = contatoRepository.findAll().size();

        // Update the contato
        Contato updatedContato = contatoRepository.findOne(contato.getId());
        updatedContato
                .email(UPDATED_EMAIL)
                .telefone(UPDATED_TELEFONE);

        restContatoMockMvc.perform(put("/api/contatoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContato)))
            .andExpect(status().isOk());

        // Validate the Contato in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeUpdate);
        Contato testContato = contatoList.get(contatoList.size() - 1);
        assertThat(testContato.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContato.getTelefone()).isEqualTo(UPDATED_TELEFONE);

        // Validate the Contato in ElasticSearch
        Contato contatoEs = contatoSearchRepository.findOne(testContato.getId());
        assertThat(contatoEs).isEqualToComparingFieldByField(testContato);
    }

    @Test
    @Transactional
    public void updateNonExistingContato() throws Exception {
        int databaseSizeBeforeUpdate = contatoRepository.findAll().size();

        // Create the Contato

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContatoMockMvc.perform(put("/api/contatoes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contato)))
            .andExpect(status().isCreated());

        // Validate the Contato in the database
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContato() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);
        contatoSearchRepository.save(contato);
        int databaseSizeBeforeDelete = contatoRepository.findAll().size();

        // Get the contato
        restContatoMockMvc.perform(delete("/api/contatoes/{id}", contato.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean contatoExistsInEs = contatoSearchRepository.exists(contato.getId());
        assertThat(contatoExistsInEs).isFalse();

        // Validate the database is empty
        List<Contato> contatoList = contatoRepository.findAll();
        assertThat(contatoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContato() throws Exception {
        // Initialize the database
        contatoRepository.saveAndFlush(contato);
        contatoSearchRepository.save(contato);

        // Search the contato
        restContatoMockMvc.perform(get("/api/_search/contatoes?query=id:" + contato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contato.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE.toString())));
    }
}
