package br.gov.sc.fatma.sinfat.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.gov.sc.fatma.sinfat.domain.Processo;
import br.gov.sc.fatma.sinfat.service.ProcessoService;
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
 * REST controller for managing Processo.
 */
@RestController
@RequestMapping("/api")
public class ProcessoResource {

    private final Logger log = LoggerFactory.getLogger(ProcessoResource.class);
        
    @Inject
    private ProcessoService processoService;

    /**
     * POST  /processos : Create a new processo.
     *
     * @param processo the processo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new processo, or with status 400 (Bad Request) if the processo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/processos")
    @Timed
    public ResponseEntity<Processo> createProcesso(@Valid @RequestBody Processo processo) throws URISyntaxException {
        log.debug("REST request to save Processo : {}", processo);
        if (processo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("processo", "idexists", "A new processo cannot already have an ID")).body(null);
        }
        Processo result = processoService.save(processo);
        return ResponseEntity.created(new URI("/api/processos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("processo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /processos : Updates an existing processo.
     *
     * @param processo the processo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated processo,
     * or with status 400 (Bad Request) if the processo is not valid,
     * or with status 500 (Internal Server Error) if the processo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/processos")
    @Timed
    public ResponseEntity<Processo> updateProcesso(@Valid @RequestBody Processo processo) throws URISyntaxException {
        log.debug("REST request to update Processo : {}", processo);
        if (processo.getId() == null) {
            return createProcesso(processo);
        }
        Processo result = processoService.save(processo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("processo", processo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /processos : get all the processos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of processos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/processos")
    @Timed
    public ResponseEntity<List<Processo>> getAllProcessos(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Processos");
        Page<Processo> page = processoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/processos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /processos/:id : get the "id" processo.
     *
     * @param id the id of the processo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the processo, or with status 404 (Not Found)
     */
    @GetMapping("/processos/{id}")
    @Timed
    public ResponseEntity<Processo> getProcesso(@PathVariable Long id) {
        log.debug("REST request to get Processo : {}", id);
        Processo processo = processoService.findOne(id);
        return Optional.ofNullable(processo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /processos/:id : delete the "id" processo.
     *
     * @param id the id of the processo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/processos/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcesso(@PathVariable Long id) {
        log.debug("REST request to delete Processo : {}", id);
        processoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("processo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/processos?query=:query : search for the processo corresponding
     * to the query.
     *
     * @param query the query of the processo search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/processos")
    @Timed
    public ResponseEntity<List<Processo>> searchProcessos(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Processos for query {}", query);
        Page<Processo> page = processoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/processos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
