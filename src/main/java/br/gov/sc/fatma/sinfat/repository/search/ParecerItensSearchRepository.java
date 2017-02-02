package br.gov.sc.fatma.sinfat.repository.search;

import br.gov.sc.fatma.sinfat.domain.ParecerItens;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ParecerItens entity.
 */
public interface ParecerItensSearchRepository extends ElasticsearchRepository<ParecerItens, Long> {
}
