package br.gov.sc.fatma.sinfat.repository.search;

import br.gov.sc.fatma.sinfat.domain.Endereco;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Endereco entity.
 */
public interface EnderecoSearchRepository extends ElasticsearchRepository<Endereco, Long> {
}
