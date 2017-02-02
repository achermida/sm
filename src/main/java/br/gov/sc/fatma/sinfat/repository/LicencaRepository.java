package br.gov.sc.fatma.sinfat.repository;

import br.gov.sc.fatma.sinfat.domain.Licenca;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Licenca entity.
 */
@SuppressWarnings("unused")
public interface LicencaRepository extends JpaRepository<Licenca,Long> {

}
