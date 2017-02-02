package br.gov.sc.fatma.sinfat.repository.search;

import br.gov.sc.fatma.sinfat.domain.Colaborador;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Colaborador entity.
 */
public interface ColaboradorSearchRepository extends ElasticsearchRepository<Colaborador, Long> {
}
