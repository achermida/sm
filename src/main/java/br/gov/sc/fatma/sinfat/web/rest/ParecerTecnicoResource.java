package br.gov.sc.fatma.sinfat.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.gov.sc.fatma.sinfat.domain.ParecerTecnico;

import br.gov.sc.fatma.sinfat.repository.ParecerTecnicoRepository;
import br.gov.sc.fatma.sinfat.repository.search.ParecerTecnicoSearchRepository;
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
 * REST controller for managing ParecerTecnico.
 */
@RestController
@RequestMapping("/api")
public class ParecerTecnicoResource {

    private final Logger log = LoggerFactory.getLogger(ParecerTecnicoResource.class);
        
    @Inject
    private ParecerTecnicoRepository parecerTecnicoRepository;

    @Inject
    private ParecerTecnicoSearchRepository parecerTecnicoSearchRepository;

    /**
     * POST  /parecer-tecnicos : Create a new parecerTecnico.
     *
     * @param parecerTecnico the parecerTecnico to create
     * @return the ResponseEntity with status 201 (Created) and with body the new parecerTecnico, or with status 400 (Bad Request) if the parecerTecnico has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/parecer-tecnicos")
    @Timed
    public ResponseEntity<ParecerTecnico> createParecerTecnico(@Valid @RequestBody ParecerTecnico parecerTecnico) throws URISyntaxException {
        log.debug("REST request to save ParecerTecnico : {}", parecerTecnico);
        if (parecerTecnico.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("parecerTecnico", "idexists", "A new parecerTecnico cannot already have an ID")).body(null);
        }
        ParecerTecnico result = parecerTecnicoRepository.save(parecerTecnico);
        parecerTecnicoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/parecer-tecnicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("parecerTecnico", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /parecer-tecnicos : Updates an existing parecerTecnico.
     *
     * @param parecerTecnico the parecerTecnico to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated parecerTecnico,
     * or with status 400 (Bad Request) if the parecerTecnico is not valid,
     * or with status 500 (Internal Server Error) if the parecerTecnico couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/parecer-tecnicos")
    @Timed
    public ResponseEntity<ParecerTecnico> updateParecerTecnico(@Valid @RequestBody ParecerTecnico parecerTecnico) throws URISyntaxException {
        log.debug("REST request to update ParecerTecnico : {}", parecerTecnico);
        if (parecerTecnico.getId() == null) {
            return createParecerTecnico(parecerTecnico);
        }
        ParecerTecnico result = parecerTecnicoRepository.save(parecerTecnico);
        parecerTecnicoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("parecerTecnico", parecerTecnico.getId().toString()))
            .body(result);
    }

    /**
     * GET  /parecer-tecnicos : get all the parecerTecnicos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of parecerTecnicos in body
     */
    @GetMapping("/parecer-tecnicos")
    @Timed
    public List<ParecerTecnico> getAllParecerTecnicos() {
        log.debug("REST request to get all ParecerTecnicos");
        List<ParecerTecnico> parecerTecnicos = parecerTecnicoRepository.findAll();
        return parecerTecnicos;
    }

    /**
     * GET  /parecer-tecnicos/:id : get the "id" parecerTecnico.
     *
     * @param id the id of the parecerTecnico to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the parecerTecnico, or with status 404 (Not Found)
     */
    @GetMapping("/parecer-tecnicos/{id}")
    @Timed
    public ResponseEntity<ParecerTecnico> getParecerTecnico(@PathVariable Long id) {
        log.debug("REST request to get ParecerTecnico : {}", id);
        ParecerTecnico parecerTecnico = parecerTecnicoRepository.findOne(id);
        return Optional.ofNullable(parecerTecnico)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /parecer-tecnicos/:id : delete the "id" parecerTecnico.
     *
     * @param id the id of the parecerTecnico to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/parecer-tecnicos/{id}")
    @Timed
    public ResponseEntity<Void> deleteParecerTecnico(@PathVariable Long id) {
        log.debug("REST request to delete ParecerTecnico : {}", id);
        parecerTecnicoRepository.delete(id);
        parecerTecnicoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("parecerTecnico", id.toString())).build();
    }

    /**
     * SEARCH  /_search/parecer-tecnicos?query=:query : search for the parecerTecnico corresponding
     * to the query.
     *
     * @param query the query of the parecerTecnico search 
     * @return the result of the search
     */
    @GetMapping("/_search/parecer-tecnicos")
    @Timed
    public List<ParecerTecnico> searchParecerTecnicos(@RequestParam String query) {
        log.debug("REST request to search ParecerTecnicos for query {}", query);
        return StreamSupport
            .stream(parecerTecnicoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
