package br.gov.sc.fatma.sinfat.service;

import br.gov.sc.fatma.sinfat.domain.Processo;
import br.gov.sc.fatma.sinfat.repository.ProcessoRepository;
import br.gov.sc.fatma.sinfat.repository.search.ProcessoSearchRepository;
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
 * Service Implementation for managing Processo.
 */
@Service
@Transactional
public class ProcessoService {

    private final Logger log = LoggerFactory.getLogger(ProcessoService.class);
    
    @Inject
    private ProcessoRepository processoRepository;

    @Inject
    private ProcessoSearchRepository processoSearchRepository;

    /**
     * Save a processo.
     *
     * @param processo the entity to save
     * @return the persisted entity
     */
    public Processo save(Processo processo) {
        log.debug("Request to save Processo : {}", processo);
        Processo result = processoRepository.save(processo);
        processoSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the processos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Processo> findAll(Pageable pageable) {
        log.debug("Request to get all Processos");
        Page<Processo> result = processoRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one processo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Processo findOne(Long id) {
        log.debug("Request to get Processo : {}", id);
        Processo processo = processoRepository.findOne(id);
        return processo;
    }

    /**
     *  Delete the  processo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Processo : {}", id);
        processoRepository.delete(id);
        processoSearchRepository.delete(id);
    }

    /**
     * Search for the processo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Processo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Processos for query {}", query);
        Page<Processo> result = processoSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
