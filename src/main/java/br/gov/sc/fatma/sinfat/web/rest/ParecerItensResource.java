package br.gov.sc.fatma.sinfat.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.gov.sc.fatma.sinfat.domain.ParecerItens;

import br.gov.sc.fatma.sinfat.repository.ParecerItensRepository;
import br.gov.sc.fatma.sinfat.repository.search.ParecerItensSearchRepository;
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
 * REST controller for managing ParecerItens.
 */
@RestController
@RequestMapping("/api")
public class ParecerItensResource {

    private final Logger log = LoggerFactory.getLogger(ParecerItensResource.class);
        
    @Inject
    private ParecerItensRepository parecerItensRepository;

    @Inject
    private ParecerItensSearchRepository parecerItensSearchRepository;

    /**
     * POST  /parecer-itens : Create a new parecerItens.
     *
     * @param parecerItens the parecerItens to create
     * @return the ResponseEntity with status 201 (Created) and with body the new parecerItens, or with status 400 (Bad Request) if the parecerItens has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/parecer-itens")
    @Timed
    public ResponseEntity<ParecerItens> createParecerItens(@Valid @RequestBody ParecerItens parecerItens) throws URISyntaxException {
        log.debug("REST request to save ParecerItens : {}", parecerItens);
        if (parecerItens.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("parecerItens", "idexists", "A new parecerItens cannot already have an ID")).body(null);
        }
        ParecerItens result = parecerItensRepository.save(parecerItens);
        parecerItensSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/parecer-itens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("parecerItens", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /parecer-itens : Updates an existing parecerItens.
     *
     * @param parecerItens the parecerItens to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated parecerItens,
     * or with status 400 (Bad Request) if the parecerItens is not valid,
     * or with status 500 (Internal Server Error) if the parecerItens couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/parecer-itens")
    @Timed
    public ResponseEntity<ParecerItens> updateParecerItens(@Valid @RequestBody ParecerItens parecerItens) throws URISyntaxException {
        log.debug("REST request to update ParecerItens : {}", parecerItens);
        if (parecerItens.getId() == null) {
            return createParecerItens(parecerItens);
        }
        ParecerItens result = parecerItensRepository.save(parecerItens);
        parecerItensSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("parecerItens", parecerItens.getId().toString()))
            .body(result);
    }

    /**
     * GET  /parecer-itens : get all the parecerItens.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of parecerItens in body
     */
    @GetMapping("/parecer-itens")
    @Timed
    public List<ParecerItens> getAllParecerItens() {
        log.debug("REST request to get all ParecerItens");
        List<ParecerItens> parecerItens = parecerItensRepository.findAll();
        return parecerItens;
    }

    /**
     * GET  /parecer-itens/:id : get the "id" parecerItens.
     *
     * @param id the id of the parecerItens to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the parecerItens, or with status 404 (Not Found)
     */
    @GetMapping("/parecer-itens/{id}")
    @Timed
    public ResponseEntity<ParecerItens> getParecerItens(@PathVariable Long id) {
        log.debug("REST request to get ParecerItens : {}", id);
        ParecerItens parecerItens = parecerItensRepository.findOne(id);
        return Optional.ofNullable(parecerItens)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /parecer-itens/:id : delete the "id" parecerItens.
     *
     * @param id the id of the parecerItens to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/parecer-itens/{id}")
    @Timed
    public ResponseEntity<Void> deleteParecerItens(@PathVariable Long id) {
        log.debug("REST request to delete ParecerItens : {}", id);
        parecerItensRepository.delete(id);
        parecerItensSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("parecerItens", id.toString())).build();
    }

    /**
     * SEARCH  /_search/parecer-itens?query=:query : search for the parecerItens corresponding
     * to the query.
     *
     * @param query the query of the parecerItens search 
     * @return the result of the search
     */
    @GetMapping("/_search/parecer-itens")
    @Timed
    public List<ParecerItens> searchParecerItens(@RequestParam String query) {
        log.debug("REST request to search ParecerItens for query {}", query);
        return StreamSupport
            .stream(parecerItensSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
