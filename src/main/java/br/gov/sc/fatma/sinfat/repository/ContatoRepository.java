package br.gov.sc.fatma.sinfat.repository;

import br.gov.sc.fatma.sinfat.domain.Contato;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Contato entity.
 */
@SuppressWarnings("unused")
public interface ContatoRepository extends JpaRepository<Contato,Long> {

}
