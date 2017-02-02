package br.gov.sc.fatma.sinfat.repository;

import br.gov.sc.fatma.sinfat.domain.Colaborador;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Colaborador entity.
 */
@SuppressWarnings("unused")
public interface ColaboradorRepository extends JpaRepository<Colaborador,Long> {

}
