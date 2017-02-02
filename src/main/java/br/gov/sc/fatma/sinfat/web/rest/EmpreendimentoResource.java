package br.gov.sc.fatma.sinfat.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.gov.sc.fatma.sinfat.domain.Empreendimento;

import br.gov.sc.fatma.sinfat.repository.EmpreendimentoRepository;
import br.gov.sc.fatma.sinfat.repository.search.EmpreendimentoSearchRepository;
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
 * REST controller for managing Empreendimento.
 */
@RestController
@RequestMapping("/api")
public class EmpreendimentoResource {

    private final Logger log = LoggerFactory.getLogger(EmpreendimentoResource.class);
        
    @Inject
    private EmpreendimentoRepository empreendimentoRepository;

    @Inject
    private EmpreendimentoSearchRepository empreendimentoSearchRepository;

    /**
     * POST  /empreendimentos : Create a new empreendimento.
     *
     * @param empreendimento the empreendimento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new empreendimento, or with status 400 (Bad Request) if the empreendimento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/empreendimentos")
    @Timed
    public ResponseEntity<Empreendimento> createEmpreendimento(@Valid @RequestBody Empreendimento empreendimento) throws URISyntaxException {
        log.debug("REST request to save Empreendimento : {}", empreendimento);
        if (empreendimento.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("empreendimento", "idexists", "A new empreendimento cannot already have an ID")).body(null);
        }
        Empreendimento result = empreendimentoRepository.save(empreendimento);
        empreendimentoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/empreendimentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("empreendimento", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /empreendimentos : Updates an existing empreendimento.
     *
     * @param empreendimento the empreendimento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated empreendimento,
     * or with status 400 (Bad Request) if the empreendimento is not valid,
     * or with status 500 (Internal Server Error) if the empreendimento couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/empreendimentos")
    @Timed
    public ResponseEntity<Empreendimento> updateEmpreendimento(@Valid @RequestBody Empreendimento empreendimento) throws URISyntaxException {
        log.debug("REST request to update Empreendimento : {}", empreendimento);
        if (empreendimento.getId() == null) {
            return createEmpreendimento(empreendimento);
        }
        Empreendimento result = empreendimentoRepository.save(empreendimento);
        empreendimentoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("empreendimento", empreendimento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /empreendimentos : get all the empreendimentos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of empreendimentos in body
     */
    @GetMapping("/empreendimentos")
    @Timed
    public List<Empreendimento> getAllEmpreendimentos() {
        log.debug("REST request to get all Empreendimentos");
        List<Empreendimento> empreendimentos = empreendimentoRepository.findAllWithEagerRelationships();
        return empreendimentos;
    }

    /**
     * GET  /empreendimentos/:id : get the "id" empreendimento.
     *
     * @param id the id of the empreendimento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the empreendimento, or with status 404 (Not Found)
     */
    @GetMapping("/empreendimentos/{id}")
    @Timed
    public ResponseEntity<Empreendimento> getEmpreendimento(@PathVariable Long id) {
        log.debug("REST request to get Empreendimento : {}", id);
        Empreendimento empreendimento = empreendimentoRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(empreendimento)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /empreendimentos/:id : delete the "id" empreendimento.
     *
     * @param id the id of the empreendimento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/empreendimentos/{id}")
    @Timed
    public ResponseEntity<Void> deleteEmpreendimento(@PathVariable Long id) {
        log.debug("REST request to delete Empreendimento : {}", id);
        empreendimentoRepository.delete(id);
        empreendimentoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("empreendimento", id.toString())).build();
    }

    /**
     * SEARCH  /_search/empreendimentos?query=:query : search for the empreendimento corresponding
     * to the query.
     *
     * @param query the query of the empreendimento search 
     * @return the result of the search
     */
    @GetMapping("/_search/empreendimentos")
    @Timed
    public List<Empreendimento> searchEmpreendimentos(@RequestParam String query) {
        log.debug("REST request to search Empreendimentos for query {}", query);
        return StreamSupport
            .stream(empreendimentoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
