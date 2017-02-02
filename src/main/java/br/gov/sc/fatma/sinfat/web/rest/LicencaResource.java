package br.gov.sc.fatma.sinfat.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.gov.sc.fatma.sinfat.domain.Licenca;
import br.gov.sc.fatma.sinfat.service.LicencaService;
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
 * REST controller for managing Licenca.
 */
@RestController
@RequestMapping("/api")
public class LicencaResource {

    private final Logger log = LoggerFactory.getLogger(LicencaResource.class);
        
    @Inject
    private LicencaService licencaService;

    /**
     * POST  /licencas : Create a new licenca.
     *
     * @param licenca the licenca to create
     * @return the ResponseEntity with status 201 (Created) and with body the new licenca, or with status 400 (Bad Request) if the licenca has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/licencas")
    @Timed
    public ResponseEntity<Licenca> createLicenca(@Valid @RequestBody Licenca licenca) throws URISyntaxException {
        log.debug("REST request to save Licenca : {}", licenca);
        if (licenca.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("licenca", "idexists", "A new licenca cannot already have an ID")).body(null);
        }
        Licenca result = licencaService.save(licenca);
        return ResponseEntity.created(new URI("/api/licencas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("licenca", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /licencas : Updates an existing licenca.
     *
     * @param licenca the licenca to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated licenca,
     * or with status 400 (Bad Request) if the licenca is not valid,
     * or with status 500 (Internal Server Error) if the licenca couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/licencas")
    @Timed
    public ResponseEntity<Licenca> updateLicenca(@Valid @RequestBody Licenca licenca) throws URISyntaxException {
        log.debug("REST request to update Licenca : {}", licenca);
        if (licenca.getId() == null) {
            return createLicenca(licenca);
        }
        Licenca result = licencaService.save(licenca);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("licenca", licenca.getId().toString()))
            .body(result);
    }

    /**
     * GET  /licencas : get all the licencas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of licencas in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/licencas")
    @Timed
    public ResponseEntity<List<Licenca>> getAllLicencas(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Licencas");
        Page<Licenca> page = licencaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/licencas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /licencas/:id : get the "id" licenca.
     *
     * @param id the id of the licenca to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the licenca, or with status 404 (Not Found)
     */
    @GetMapping("/licencas/{id}")
    @Timed
    public ResponseEntity<Licenca> getLicenca(@PathVariable Long id) {
        log.debug("REST request to get Licenca : {}", id);
        Licenca licenca = licencaService.findOne(id);
        return Optional.ofNullable(licenca)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /licencas/:id : delete the "id" licenca.
     *
     * @param id the id of the licenca to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/licencas/{id}")
    @Timed
    public ResponseEntity<Void> deleteLicenca(@PathVariable Long id) {
        log.debug("REST request to delete Licenca : {}", id);
        licencaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("licenca", id.toString())).build();
    }

    /**
     * SEARCH  /_search/licencas?query=:query : search for the licenca corresponding
     * to the query.
     *
     * @param query the query of the licenca search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/licencas")
    @Timed
    public ResponseEntity<List<Licenca>> searchLicencas(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Licencas for query {}", query);
        Page<Licenca> page = licencaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/licencas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
