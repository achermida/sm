package br.gov.sc.fatma.sinfat.repository;

import br.gov.sc.fatma.sinfat.domain.Processo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Processo entity.
 */
@SuppressWarnings("unused")
public interface ProcessoRepository extends JpaRepository<Processo,Long> {

}
