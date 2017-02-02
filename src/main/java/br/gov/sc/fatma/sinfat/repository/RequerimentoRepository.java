package br.gov.sc.fatma.sinfat.repository;

import br.gov.sc.fatma.sinfat.domain.Requerimento;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Requerimento entity.
 */
@SuppressWarnings("unused")
public interface RequerimentoRepository extends JpaRepository<Requerimento,Long> {

}
