package br.gov.sc.fatma.sinfat.repository;

import br.gov.sc.fatma.sinfat.domain.Endereco;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Endereco entity.
 */
@SuppressWarnings("unused")
public interface EnderecoRepository extends JpaRepository<Endereco,Long> {

}
