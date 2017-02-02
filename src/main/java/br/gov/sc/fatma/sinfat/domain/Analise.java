package br.gov.sc.fatma.sinfat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import br.gov.sc.fatma.sinfat.domain.enumeration.StatusGeralEnum;

/**
 * A Analise.
 */
@Entity
@Table(name = "analise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "analise")
public class Analise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusGeralEnum status;

    @OneToMany(mappedBy = "analise")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ParecerTecnico> pareceres = new HashSet<>();

    @ManyToOne
    private Requerimento requerimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusGeralEnum getStatus() {
        return status;
    }

    public Analise status(StatusGeralEnum status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusGeralEnum status) {
        this.status = status;
    }

    public Set<ParecerTecnico> getPareceres() {
        return pareceres;
    }

    public Analise pareceres(Set<ParecerTecnico> parecerTecnicos) {
        this.pareceres = parecerTecnicos;
        return this;
    }

    public Analise addPareceres(ParecerTecnico parecerTecnico) {
        pareceres.add(parecerTecnico);
        parecerTecnico.setAnalise(this);
        return this;
    }

    public Analise removePareceres(ParecerTecnico parecerTecnico) {
        pareceres.remove(parecerTecnico);
        parecerTecnico.setAnalise(null);
        return this;
    }

    public void setPareceres(Set<ParecerTecnico> parecerTecnicos) {
        this.pareceres = parecerTecnicos;
    }

    public Requerimento getRequerimento() {
        return requerimento;
    }

    public Analise requerimento(Requerimento requerimento) {
        this.requerimento = requerimento;
        return this;
    }

    public void setRequerimento(Requerimento requerimento) {
        this.requerimento = requerimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Analise analise = (Analise) o;
        if (analise.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, analise.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Analise{" +
            "id=" + id +
            ", status='" + status + "'" +
            '}';
    }
}
