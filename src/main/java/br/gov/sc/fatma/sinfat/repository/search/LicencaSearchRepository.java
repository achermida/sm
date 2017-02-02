package br.gov.sc.fatma.sinfat.repository.search;

import br.gov.sc.fatma.sinfat.domain.Licenca;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Licenca entity.
 */
public interface LicencaSearchRepository extends ElasticsearchRepository<Licenca, Long> {
}
