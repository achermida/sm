package br.gov.sc.fatma.sinfat.service;

import br.gov.sc.fatma.sinfat.domain.Empreendimento;
import br.gov.sc.fatma.sinfat.repository.EmpreendimentoRepository;
import br.gov.sc.fatma.sinfat.repository.search.EmpreendimentoSearchRepository;
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
 * Service Implementation for managing Empreendimento.
 */
@Service
@Transactional
public class EmpreendimentoService {

    private final Logger log = LoggerFactory.getLogger(EmpreendimentoService.class);
    
    @Inject
    private EmpreendimentoRepository empreendimentoRepository;

    @Inject
    private EmpreendimentoSearchRepository empreendimentoSearchRepository;

    /**
     * Save a empreendimento.
     *
     * @param empreendimento the entity to save
     * @return the persisted entity
     */
    public Empreendimento save(Empreendimento empreendimento) {
        log.debug("Request to save Empreendimento : {}", empreendimento);
        Empreendimento result = empreendimentoRepository.save(empreendimento);
        empreendimentoSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the empreendimentos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Empreendimento> findAll(Pageable pageable) {
        log.debug("Request to get all Empreendimentos");
        Page<Empreendimento> result = empreendimentoRepository.findAll(pageable);
        return result;
    }


    /**
     *  get all the empreendimentos where Responsavel is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Empreendimento> findAllWhereResponsavelIsNull() {
        log.debug("Request to get all empreendimentos where Responsavel is null");
        return StreamSupport
            .stream(empreendimentoRepository.findAll().spliterator(), false)
            .filter(empreendimento -> empreendimento.getResponsavel() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one empreendimento by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Empreendimento findOne(Long id) {
        log.debug("Request to get Empreendimento : {}", id);
        Empreendimento empreendimento = empreendimentoRepository.findOne(id);
        return empreendimento;
    }

    /**
     *  Delete the  empreendimento by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Empreendimento : {}", id);
        empreendimentoRepository.delete(id);
        empreendimentoSearchRepository.delete(id);
    }

    /**
     * Search for the empreendimento corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Empreendimento> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Empreendimentos for query {}", query);
        Page<Empreendimento> result = empreendimentoSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
