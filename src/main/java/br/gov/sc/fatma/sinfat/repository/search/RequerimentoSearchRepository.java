package br.gov.sc.fatma.sinfat.repository.search;

import br.gov.sc.fatma.sinfat.domain.Requerimento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Requerimento entity.
 */
public interface RequerimentoSearchRepository extends ElasticsearchRepository<Requerimento, Long> {
}
