package br.gov.sc.fatma.sinfat.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.gov.sc.fatma.sinfat.domain.Contato;

import br.gov.sc.fatma.sinfat.repository.ContatoRepository;
import br.gov.sc.fatma.sinfat.repository.search.ContatoSearchRepository;
import br.gov.sc.fatma.sinfat.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Contato.
 */
@RestController
@RequestMapping("/api")
public class ContatoResource {

    private final Logger log = LoggerFactory.getLogger(ContatoResource.class);
        
    @Inject
    private ContatoRepository contatoRepository;

    @Inject
    private ContatoSearchRepository contatoSearchRepository;

    /**
     * POST  /contatoes : Create a new contato.
     *
     * @param contato the contato to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contato, or with status 400 (Bad Request) if the contato has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contatoes")
    @Timed
    public ResponseEntity<Contato> createContato(@Valid @RequestBody Contato contato) throws URISyntaxException {
        log.debug("REST request to save Contato : {}", contato);
        if (contato.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contato", "idexists", "A new contato cannot already have an ID")).body(null);
        }
        Contato result = contatoRepository.save(contato);
        contatoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/contatoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contato", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contatoes : Updates an existing contato.
     *
     * @param contato the contato to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contato,
     * or with status 400 (Bad Request) if the contato is not valid,
     * or with status 500 (Internal Server Error) if the contato couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contatoes")
    @Timed
    public ResponseEntity<Contato> updateContato(@Valid @RequestBody Contato contato) throws URISyntaxException {
        log.debug("REST request to update Contato : {}", contato);
        if (contato.getId() == null) {
            return createContato(contato);
        }
        Contato result = contatoRepository.save(contato);
        contatoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contato", contato.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contatoes : get all the contatoes.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of contatoes in body
     */
    @GetMapping("/contatoes")
    @Timed
    public List<Contato> getAllContatoes(@RequestParam(required = false) String filter) {
        if ("usuario-is-null".equals(filter)) {
            log.debug("REST request to get all Contatos where usuario is null");
            return StreamSupport
                .stream(contatoRepository.findAll().spliterator(), false)
                .filter(contato -> contato.getUsuario() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Contatoes");
        List<Contato> contatoes = contatoRepository.findAll();
        return contatoes;
    }

    /**
     * GET  /contatoes/:id : get the "id" contato.
     *
     * @param id the id of the contato to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contato, or with status 404 (Not Found)
     */
    @GetMapping("/contatoes/{id}")
    @Timed
    public ResponseEntity<Contato> getContato(@PathVariable Long id) {
        log.debug("REST request to get Contato : {}", id);
        Contato contato = contatoRepository.findOne(id);
        return Optional.ofNullable(contato)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contatoes/:id : delete the "id" contato.
     *
     * @param id the id of the contato to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contatoes/{id}")
    @Timed
    public ResponseEntity<Void> deleteContato(@PathVariable Long id) {
        log.debug("REST request to delete Contato : {}", id);
        contatoRepository.delete(id);
        contatoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contato", id.toString())).build();
    }

    /**
     * SEARCH  /_search/contatoes?query=:query : search for the contato corresponding
     * to the query.
     *
     * @param query the query of the contato search 
     * @return the result of the search
     */
    @GetMapping("/_search/contatoes")
    @Timed
    public List<Contato> searchContatoes(@RequestParam String query) {
        log.debug("REST request to search Contatoes for query {}", query);
        return StreamSupport
            .stream(contatoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
