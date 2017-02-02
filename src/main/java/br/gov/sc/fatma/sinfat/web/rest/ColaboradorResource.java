package br.gov.sc.fatma.sinfat.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.gov.sc.fatma.sinfat.domain.Colaborador;

import br.gov.sc.fatma.sinfat.repository.ColaboradorRepository;
import br.gov.sc.fatma.sinfat.repository.search.ColaboradorSearchRepository;
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
 * REST controller for managing Colaborador.
 */
@RestController
@RequestMapping("/api")
public class ColaboradorResource {

    private final Logger log = LoggerFactory.getLogger(ColaboradorResource.class);
        
    @Inject
    private ColaboradorRepository colaboradorRepository;

    @Inject
    private ColaboradorSearchRepository colaboradorSearchRepository;

    /**
     * POST  /colaboradors : Create a new colaborador.
     *
     * @param colaborador the colaborador to create
     * @return the ResponseEntity with status 201 (Created) and with body the new colaborador, or with status 400 (Bad Request) if the colaborador has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/colaboradors")
    @Timed
    public ResponseEntity<Colaborador> createColaborador(@Valid @RequestBody Colaborador colaborador) throws URISyntaxException {
        log.debug("REST request to save Colaborador : {}", colaborador);
        if (colaborador.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("colaborador", "idexists", "A new colaborador cannot already have an ID")).body(null);
        }
        Colaborador result = colaboradorRepository.save(colaborador);
        colaboradorSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/colaboradors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("colaborador", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /colaboradors : Updates an existing colaborador.
     *
     * @param colaborador the colaborador to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated colaborador,
     * or with status 400 (Bad Request) if the colaborador is not valid,
     * or with status 500 (Internal Server Error) if the colaborador couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/colaboradors")
    @Timed
    public ResponseEntity<Colaborador> updateColaborador(@Valid @RequestBody Colaborador colaborador) throws URISyntaxException {
        log.debug("REST request to update Colaborador : {}", colaborador);
        if (colaborador.getId() == null) {
            return createColaborador(colaborador);
        }
        Colaborador result = colaboradorRepository.save(colaborador);
        colaboradorSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("colaborador", colaborador.getId().toString()))
            .body(result);
    }

    /**
     * GET  /colaboradors : get all the colaboradors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of colaboradors in body
     */
    @GetMapping("/colaboradors")
    @Timed
    public List<Colaborador> getAllColaboradors() {
        log.debug("REST request to get all Colaboradors");
        List<Colaborador> colaboradors = colaboradorRepository.findAll();
        return colaboradors;
    }

    /**
     * GET  /colaboradors/:id : get the "id" colaborador.
     *
     * @param id the id of the colaborador to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the colaborador, or with status 404 (Not Found)
     */
    @GetMapping("/colaboradors/{id}")
    @Timed
    public ResponseEntity<Colaborador> getColaborador(@PathVariable Long id) {
        log.debug("REST request to get Colaborador : {}", id);
        Colaborador colaborador = colaboradorRepository.findOne(id);
        return Optional.ofNullable(colaborador)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /colaboradors/:id : delete the "id" colaborador.
     *
     * @param id the id of the colaborador to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/colaboradors/{id}")
    @Timed
    public ResponseEntity<Void> deleteColaborador(@PathVariable Long id) {
        log.debug("REST request to delete Colaborador : {}", id);
        colaboradorRepository.delete(id);
        colaboradorSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("colaborador", id.toString())).build();
    }

    /**
     * SEARCH  /_search/colaboradors?query=:query : search for the colaborador corresponding
     * to the query.
     *
     * @param query the query of the colaborador search 
     * @return the result of the search
     */
    @GetMapping("/_search/colaboradors")
    @Timed
    public List<Colaborador> searchColaboradors(@RequestParam String query) {
        log.debug("REST request to search Colaboradors for query {}", query);
        return StreamSupport
            .stream(colaboradorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
