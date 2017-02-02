package br.gov.sc.fatma.sinfat.repository.search;

import br.gov.sc.fatma.sinfat.domain.Analise;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Analise entity.
 */
public interface AnaliseSearchRepository extends ElasticsearchRepository<Analise, Long> {
}
