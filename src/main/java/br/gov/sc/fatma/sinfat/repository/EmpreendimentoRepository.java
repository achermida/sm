package br.gov.sc.fatma.sinfat.repository;

import br.gov.sc.fatma.sinfat.domain.Empreendimento;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Empreendimento entity.
 */
@SuppressWarnings("unused")
public interface EmpreendimentoRepository extends JpaRepository<Empreendimento,Long> {

    @Query("select distinct empreendimento from Empreendimento empreendimento left join fetch empreendimento.responsaveis")
    List<Empreendimento> findAllWithEagerRelationships();

    @Query("select empreendimento from Empreendimento empreendimento left join fetch empreendimento.responsaveis where empreendimento.id =:id")
    Empreendimento findOneWithEagerRelationships(@Param("id") Long id);

}
