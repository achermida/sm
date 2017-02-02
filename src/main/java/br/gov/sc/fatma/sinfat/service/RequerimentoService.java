package br.gov.sc.fatma.sinfat.service;

import br.gov.sc.fatma.sinfat.domain.Requerimento;
import br.gov.sc.fatma.sinfat.repository.RequerimentoRepository;
import br.gov.sc.fatma.sinfat.repository.search.RequerimentoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Requerimento.
 */
@Service
@Transactional
public class RequerimentoService {

    private final Logger log = LoggerFactory.getLogger(RequerimentoService.class);
    
    @Inject
    private RequerimentoRepository requerimentoRepository;

    @Inject
    private RequerimentoSearchRepository requerimentoSearchRepository;

    /**
     * Save a requerimento.
     *
     * @param requerimento the entity to save
     * @return the persisted entity
     */
    public Requerimento save(Requerimento requerimento) {
        log.debug("Request to save Requerimento : {}", requerimento);
        Requerimento result = requerimentoRepository.save(requerimento);
        requerimentoSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the requerimentos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Requerimento> findAll(Pageable pageable) {
        log.debug("Request to get all Requerimentos");
        Page<Requerimento> result = requerimentoRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one requerimento by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Requerimento findOne(Long id) {
        log.debug("Request to get Requerimento : {}", id);
        Requerimento requerimento = requerimentoRepository.findOne(id);
        return requerimento;
    }

    /**
     *  Delete the  requerimento by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Requerimento : {}", id);
        requerimentoRepository.delete(id);
        requerimentoSearchRepository.delete(id);
    }

    /**
     * Search for the requerimento corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Requerimento> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Requerimentos for query {}", query);
        Page<Requerimento> result = requerimentoSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
