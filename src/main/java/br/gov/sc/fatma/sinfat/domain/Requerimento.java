package br.gov.sc.fatma.sinfat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import br.gov.sc.fatma.sinfat.domain.enumeration.StatusGeralEnum;

import br.gov.sc.fatma.sinfat.domain.enumeration.ReqFaseEnum;

/**
 * Categoria do lancamento ex: Alimentacao, Lazer, Presentes
 */
@ApiModel(description = "Categoria do lancamento ex: Alimentacao, Lazer, Presentes")
@Entity
@Table(name = "requerimento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "requerimento")
public class Requerimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "dt_cadastro", nullable = false)
    private LocalDate dtCadastro;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusGeralEnum status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "fase", nullable = false)
    private ReqFaseEnum fase;

    @Column(name = "obs")
    private String obs;

    @OneToMany(mappedBy = "requerimento")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Analise> analises = new HashSet<>();

    @ManyToOne
    private Processo processo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    public Requerimento dtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
        return this;
    }

    public void setDtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public StatusGeralEnum getStatus() {
        return status;
    }

    public Requerimento status(StatusGeralEnum status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusGeralEnum status) {
        this.status = status;
    }

    public ReqFaseEnum getFase() {
        return fase;
    }

    public Requerimento fase(ReqFaseEnum fase) {
        this.fase = fase;
        return this;
    }

    public void setFase(ReqFaseEnum fase) {
        this.fase = fase;
    }

    public String getObs() {
        return obs;
    }

    public Requerimento obs(String obs) {
        this.obs = obs;
        return this;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Set<Analise> getAnalises() {
        return analises;
    }

    public Requerimento analises(Set<Analise> analises) {
        this.analises = analises;
        return this;
    }

    public Requerimento addAnalises(Analise analise) {
        analises.add(analise);
        analise.setRequerimento(this);
        return this;
    }

    public Requerimento removeAnalises(Analise analise) {
        analises.remove(analise);
        analise.setRequerimento(null);
        return this;
    }

    public void setAnalises(Set<Analise> analises) {
        this.analises = analises;
    }

    public Processo getProcesso() {
        return processo;
    }

    public Requerimento processo(Processo processo) {
        this.processo = processo;
        return this;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Requerimento requerimento = (Requerimento) o;
        if (requerimento.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, requerimento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Requerimento{" +
            "id=" + id +
            ", dtCadastro='" + dtCadastro + "'" +
            ", status='" + status + "'" +
            ", fase='" + fase + "'" +
            ", obs='" + obs + "'" +
            '}';
    }
}
