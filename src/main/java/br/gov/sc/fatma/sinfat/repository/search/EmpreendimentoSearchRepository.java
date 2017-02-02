package br.gov.sc.fatma.sinfat.repository.search;

import br.gov.sc.fatma.sinfat.domain.Empreendimento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Empreendimento entity.
 */
public interface EmpreendimentoSearchRepository extends ElasticsearchRepository<Empreendimento, Long> {
}
