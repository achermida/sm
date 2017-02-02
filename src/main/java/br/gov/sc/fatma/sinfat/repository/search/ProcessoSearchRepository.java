package br.gov.sc.fatma.sinfat.repository.search;

import br.gov.sc.fatma.sinfat.domain.Processo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Processo entity.
 */
public interface ProcessoSearchRepository extends ElasticsearchRepository<Processo, Long> {
}
