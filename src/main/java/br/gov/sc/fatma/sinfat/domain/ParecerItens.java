package br.gov.sc.fatma.sinfat.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import br.gov.sc.fatma.sinfat.domain.enumeration.TipoParecerItensEnum;

/**
 * A ParecerItens.
 */
@Entity
@Table(name = "parecer_itens")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "pareceritens")
public class ParecerItens implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoParecerItensEnum tipo;

    @Column(name = "parit_descricao")
    private String paritDescricao;

    @Column(name = "parit_sequencia")
    private Integer paritSequencia;

    @ManyToOne
    private ParecerTecnico parecerTecnico;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoParecerItensEnum getTipo() {
        return tipo;
    }

    public ParecerItens tipo(TipoParecerItensEnum tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoParecerItensEnum tipo) {
        this.tipo = tipo;
    }

    public String getParitDescricao() {
        return paritDescricao;
    }

    public ParecerItens paritDescricao(String paritDescricao) {
        this.paritDescricao = paritDescricao;
        return this;
    }

    public void setParitDescricao(String paritDescricao) {
        this.paritDescricao = paritDescricao;
    }

    public Integer getParitSequencia() {
        return paritSequencia;
    }

    public ParecerItens paritSequencia(Integer paritSequencia) {
        this.paritSequencia = paritSequencia;
        return this;
    }

    public void setParitSequencia(Integer paritSequencia) {
        this.paritSequencia = paritSequencia;
    }

    public ParecerTecnico getParecerTecnico() {
        return parecerTecnico;
    }

    public ParecerItens parecerTecnico(ParecerTecnico parecerTecnico) {
        this.parecerTecnico = parecerTecnico;
        return this;
    }

    public void setParecerTecnico(ParecerTecnico parecerTecnico) {
        this.parecerTecnico = parecerTecnico;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParecerItens parecerItens = (ParecerItens) o;
        if (parecerItens.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, parecerItens.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ParecerItens{" +
            "id=" + id +
            ", tipo='" + tipo + "'" +
            ", paritDescricao='" + paritDescricao + "'" +
            ", paritSequencia='" + paritSequencia + "'" +
            '}';
    }
}
