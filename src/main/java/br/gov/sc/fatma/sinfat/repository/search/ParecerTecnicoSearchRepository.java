package br.gov.sc.fatma.sinfat.repository.search;

import br.gov.sc.fatma.sinfat.domain.ParecerTecnico;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ParecerTecnico entity.
 */
public interface ParecerTecnicoSearchRepository extends ElasticsearchRepository<ParecerTecnico, Long> {
}
