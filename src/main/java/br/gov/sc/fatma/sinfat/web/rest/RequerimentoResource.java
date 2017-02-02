package br.gov.sc.fatma.sinfat.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.gov.sc.fatma.sinfat.domain.Requerimento;
import br.gov.sc.fatma.sinfat.service.RequerimentoService;
import br.gov.sc.fatma.sinfat.web.rest.util.HeaderUtil;
import br.gov.sc.fatma.sinfat.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing Requerimento.
 */
@RestController
@RequestMapping("/api")
public class RequerimentoResource {

    private final Logger log = LoggerFactory.getLogger(RequerimentoResource.class);
        
    @Inject
    private RequerimentoService requerimentoService;

    /**
     * POST  /requerimentos : Create a new requerimento.
     *
     * @param requerimento the requerimento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new requerimento, or with status 400 (Bad Request) if the requerimento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/requerimentos")
    @Timed
    public ResponseEntity<Requerimento> createRequerimento(@Valid @RequestBody Requerimento requerimento) throws URISyntaxException {
        log.debug("REST request to save Requerimento : {}", requerimento);
        if (requerimento.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("requerimento", "idexists", "A new requerimento cannot already have an ID")).body(null);
        }
        Requerimento result = requerimentoService.save(requerimento);
        return ResponseEntity.created(new URI("/api/requerimentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("requerimento", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /requerimentos : Updates an existing requerimento.
     *
     * @param requerimento the requerimento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated requerimento,
     * or with status 400 (Bad Request) if the requerimento is not valid,
     * or with status 500 (Internal Server Error) if the requerimento couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/requerimentos")
    @Timed
    public ResponseEntity<Requerimento> updateRequerimento(@Valid @RequestBody Requerimento requerimento) throws URISyntaxException {
        log.debug("REST request to update Requerimento : {}", requerimento);
        if (requerimento.getId() == null) {
            return createRequerimento(requerimento);
        }
        Requerimento result = requerimentoService.save(requerimento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("requerimento", requerimento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /requerimentos : get all the requerimentos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of requerimentos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/requerimentos")
    @Timed
    public ResponseEntity<List<Requerimento>> getAllRequerimentos(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Requerimentos");
        Page<Requerimento> page = requerimentoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/requerimentos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /requerimentos/:id : get the "id" requerimento.
     *
     * @param id the id of the requerimento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the requerimento, or with status 404 (Not Found)
     */
    @GetMapping("/requerimentos/{id}")
    @Timed
    public ResponseEntity<Requerimento> getRequerimento(@PathVariable Long id) {
        log.debug("REST request to get Requerimento : {}", id);
        Requerimento requerimento = requerimentoService.findOne(id);
        return Optional.ofNullable(requerimento)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /requerimentos/:id : delete the "id" requerimento.
     *
     * @param id the id of the requerimento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/requerimentos/{id}")
    @Timed
    public ResponseEntity<Void> deleteRequerimento(@PathVariable Long id) {
        log.debug("REST request to delete Requerimento : {}", id);
        requerimentoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("requerimento", id.toString())).build();
    }

    /**
     * SEARCH  /_search/requerimentos?query=:query : search for the requerimento corresponding
     * to the query.
     *
     * @param query the query of the requerimento search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/requerimentos")
    @Timed
    public ResponseEntity<List<Requerimento>> searchRequerimentos(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Requerimentos for query {}", query);
        Page<Requerimento> page = requerimentoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/requerimentos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
