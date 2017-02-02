package br.gov.sc.fatma.sinfat.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Municipio.
 */
@Entity
@Table(name = "municipio")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "municipio")
public class Municipio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "ibge", nullable = false)
    private Long ibge;

    @ManyToOne
    private Colaborador colaborador;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Municipio nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getIbge() {
        return ibge;
    }

    public Municipio ibge(Long ibge) {
        this.ibge = ibge;
        return this;
    }

    public void setIbge(Long ibge) {
        this.ibge = ibge;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public Municipio colaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
        return this;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Municipio municipio = (Municipio) o;
        if (municipio.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, municipio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Municipio{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            ", ibge='" + ibge + "'" +
            '}';
    }
}
