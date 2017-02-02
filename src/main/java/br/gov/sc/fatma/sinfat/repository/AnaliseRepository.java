package br.gov.sc.fatma.sinfat.repository;

import br.gov.sc.fatma.sinfat.domain.Analise;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Analise entity.
 */
@SuppressWarnings("unused")
public interface AnaliseRepository extends JpaRepository<Analise,Long> {

}
