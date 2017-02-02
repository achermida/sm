package br.gov.sc.fatma.sinfat.repository;

import br.gov.sc.fatma.sinfat.domain.ParecerTecnico;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ParecerTecnico entity.
 */
@SuppressWarnings("unused")
public interface ParecerTecnicoRepository extends JpaRepository<ParecerTecnico,Long> {

}
