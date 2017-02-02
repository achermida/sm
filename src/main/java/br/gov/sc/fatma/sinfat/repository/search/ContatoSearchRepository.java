package br.gov.sc.fatma.sinfat.repository.search;

import br.gov.sc.fatma.sinfat.domain.Contato;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Contato entity.
 */
public interface ContatoSearchRepository extends ElasticsearchRepository<Contato, Long> {
}
