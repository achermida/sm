package br.gov.sc.fatma.sinfat.repository;

import br.gov.sc.fatma.sinfat.domain.ParecerItens;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ParecerItens entity.
 */
@SuppressWarnings("unused")
public interface ParecerItensRepository extends JpaRepository<ParecerItens,Long> {

}
