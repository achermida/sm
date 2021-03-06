package br.gov.sc.fatma.sinfat.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.gov.sc.fatma.sinfat.domain.Analise;

import br.gov.sc.fatma.sinfat.repository.AnaliseRepository;
import br.gov.sc.fatma.sinfat.repository.search.AnaliseSearchRepository;
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
 * REST controller for managing Analise.
 */
@RestController
@RequestMapping("/api")
public class AnaliseResource {

    private final Logger log = LoggerFactory.getLogger(AnaliseResource.class);
        
    @Inject
    private AnaliseRepository analiseRepository;

    @Inject
    private AnaliseSearchRepository analiseSearchRepository;

    /**
     * POST  /analises : Create a new analise.
     *
     * @param analise the analise to create
     * @return the ResponseEntity with status 201 (Created) and with body the new analise, or with status 400 (Bad Request) if the analise has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/analises")
    @Timed
    public ResponseEntity<Analise> createAnalise(@Valid @RequestBody Analise analise) throws URISyntaxException {
        log.debug("REST request to save Analise : {}", analise);
        if (analise.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("analise", "idexists", "A new analise cannot already have an ID")).body(null);
        }
        Analise result = analiseRepository.save(analise);
        analiseSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/analises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("analise", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /analises : Updates an existing analise.
     *
     * @param analise the analise to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated analise,
     * or with status 400 (Bad Request) if the analise is not valid,
     * or with status 500 (Internal Server Error) if the analise couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/analises")
    @Timed
    public ResponseEntity<Analise> updateAnalise(@Valid @RequestBody Analise analise) throws URISyntaxException {
        log.debug("REST request to update Analise : {}", analise);
        if (analise.getId() == null) {
            return createAnalise(analise);
        }
        Analise result = analiseRepository.save(analise);
        analiseSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("analise", analise.getId().toString()))
            .body(result);
    }

    /**
     * GET  /analises : get all the analises.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of analises in body
     */
    @GetMapping("/analises")
    @Timed
    public List<Analise> getAllAnalises() {
        log.debug("REST request to get all Analises");
        List<Analise> analises = analiseRepository.findAll();
        return analises;
    }

    /**
     * GET  /analises/:id : get the "id" analise.
     *
     * @param id the id of the analise to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the analise, or with status 404 (Not Found)
     */
    @GetMapping("/analises/{id}")
    @Timed
    public ResponseEntity<Analise> getAnalise(@PathVariable Long id) {
        log.debug("REST request to get Analise : {}", id);
        Analise analise = analiseRepository.findOne(id);
        return Optional.ofNullable(analise)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /analises/:id : delete the "id" analise.
     *
     * @param id the id of the analise to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/analises/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnalise(@PathVariable Long id) {
        log.debug("REST request to delete Analise : {}", id);
        analiseRepository.delete(id);
        analiseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("analise", id.toString())).build();
    }

    /**
     * SEARCH  /_search/analises?query=:query : search for the analise corresponding
     * to the query.
     *
     * @param query the query of the analise search 
     * @return the result of the search
     */
    @GetMapping("/_search/analises")
    @Timed
    public List<Analise> searchAnalises(@RequestParam String query) {
        log.debug("REST request to search Analises for query {}", query);
        return StreamSupport
            .stream(analiseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
