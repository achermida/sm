package br.gov.sc.fatma.sinfat.repository.search;

import br.gov.sc.fatma.sinfat.domain.Usuario;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Usuario entity.
 */
public interface UsuarioSearchRepository extends ElasticsearchRepository<Usuario, Long> {
}
