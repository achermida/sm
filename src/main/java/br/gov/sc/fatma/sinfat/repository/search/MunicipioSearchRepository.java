package br.gov.sc.fatma.sinfat.repository.search;

import br.gov.sc.fatma.sinfat.domain.Municipio;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Municipio entity.
 */
public interface MunicipioSearchRepository extends ElasticsearchRepository<Municipio, Long> {
}
