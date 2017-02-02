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
 * A Processo.
 */
@Entity
@Table(name = "processo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "processo")
public class Processo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusGeralEnum status;

    @NotNull
    @Column(name = "proc_numero", nullable = false)
    private String procNumero;

    @OneToMany(mappedBy = "processo")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Requerimento> requerimentos = new HashSet<>();

    @ManyToOne
    private Empreendimento empreendimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusGeralEnum getStatus() {
        return status;
    }

    public Processo status(StatusGeralEnum status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusGeralEnum status) {
        this.status = status;
    }

    public String getProcNumero() {
        return procNumero;
    }

    public Processo procNumero(String procNumero) {
        this.procNumero = procNumero;
        return this;
    }

    public void setProcNumero(String procNumero) {
        this.procNumero = procNumero;
    }

    public Set<Requerimento> getRequerimentos() {
        return requerimentos;
    }

    public Processo requerimentos(Set<Requerimento> requerimentos) {
        this.requerimentos = requerimentos;
        return this;
    }

    public Processo addRequerimentos(Requerimento requerimento) {
        requerimentos.add(requerimento);
        requerimento.setProcesso(this);
        return this;
    }

    public Processo removeRequerimentos(Requerimento requerimento) {
        requerimentos.remove(requerimento);
        requerimento.setProcesso(null);
        return this;
    }

    public void setRequerimentos(Set<Requerimento> requerimentos) {
        this.requerimentos = requerimentos;
    }

    public Empreendimento getEmpreendimento() {
        return empreendimento;
    }

    public Processo empreendimento(Empreendimento empreendimento) {
        this.empreendimento = empreendimento;
        return this;
    }

    public void setEmpreendimento(Empreendimento empreendimento) {
        this.empreendimento = empreendimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Processo processo = (Processo) o;
        if (processo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, processo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Processo{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", procNumero='" + procNumero + "'" +
            '}';
    }
}
