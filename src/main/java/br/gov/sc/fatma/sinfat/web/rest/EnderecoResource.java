package br.gov.sc.fatma.sinfat.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.gov.sc.fatma.sinfat.domain.Endereco;

import br.gov.sc.fatma.sinfat.repository.EnderecoRepository;
import br.gov.sc.fatma.sinfat.repository.search.EnderecoSearchRepository;
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
 * REST controller for managing Endereco.
 */
@RestController
@RequestMapping("/api")
public class EnderecoResource {

    private final Logger log = LoggerFactory.getLogger(EnderecoResource.class);
        
    @Inject
    private EnderecoRepository enderecoRepository;

    @Inject
    private EnderecoSearchRepository enderecoSearchRepository;

    /**
     * POST  /enderecos : Create a new endereco.
     *
     * @param endereco the endereco to create
     * @return the ResponseEntity with status 201 (Created) and with body the new endereco, or with status 400 (Bad Request) if the endereco has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/enderecos")
    @Timed
    public ResponseEntity<Endereco> createEndereco(@Valid @RequestBody Endereco endereco) throws URISyntaxException {
        log.debug("REST request to save Endereco : {}", endereco);
        if (endereco.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("endereco", "idexists", "A new endereco cannot already have an ID")).body(null);
        }
        Endereco result = enderecoRepository.save(endereco);
        enderecoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/enderecos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("endereco", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /enderecos : Updates an existing endereco.
     *
     * @param endereco the endereco to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated endereco,
     * or with status 400 (Bad Request) if the endereco is not valid,
     * or with status 500 (Internal Server Error) if the endereco couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/enderecos")
    @Timed
    public ResponseEntity<Endereco> updateEndereco(@Valid @RequestBody Endereco endereco) throws URISyntaxException {
        log.debug("REST request to update Endereco : {}", endereco);
        if (endereco.getId() == null) {
            return createEndereco(endereco);
        }
        Endereco result = enderecoRepository.save(endereco);
        enderecoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("endereco", endereco.getId().toString()))
            .body(result);
    }

    /**
     * GET  /enderecos : get all the enderecos.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of enderecos in body
     */
    @GetMapping("/enderecos")
    @Timed
    public List<Endereco> getAllEnderecos(@RequestParam(required = false) String filter) {
        if ("empreendimento-is-null".equals(filter)) {
            log.debug("REST request to get all Enderecos where empreendimento is null");
            return StreamSupport
                .stream(enderecoRepository.findAll().spliterator(), false)
                .filter(endereco -> endereco.getEmpreendimento() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Enderecos");
        List<Endereco> enderecos = enderecoRepository.findAll();
        return enderecos;
    }

    /**
     * GET  /enderecos/:id : get the "id" endereco.
     *
     * @param id the id of the endereco to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the endereco, or with status 404 (Not Found)
     */
    @GetMapping("/enderecos/{id}")
    @Timed
    public ResponseEntity<Endereco> getEndereco(@PathVariable Long id) {
        log.debug("REST request to get Endereco : {}", id);
        Endereco endereco = enderecoRepository.findOne(id);
        return Optional.ofNullable(endereco)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /enderecos/:id : delete the "id" endereco.
     *
     * @param id the id of the endereco to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/enderecos/{id}")
    @Timed
    public ResponseEntity<Void> deleteEndereco(@PathVariable Long id) {
        log.debug("REST request to delete Endereco : {}", id);
        enderecoRepository.delete(id);
        enderecoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("endereco", id.toString())).build();
    }

    /**
     * SEARCH  /_search/enderecos?query=:query : search for the endereco corresponding
     * to the query.
     *
     * @param query the query of the endereco search 
     * @return the result of the search
     */
    @GetMapping("/_search/enderecos")
    @Timed
    public List<Endereco> searchEnderecos(@RequestParam String query) {
        log.debug("REST request to search Enderecos for query {}", query);
        return StreamSupport
            .stream(enderecoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
