package br.gov.sc.fatma.sinfat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

/**
 * A ParecerTecnico.
 */
@Entity
@Table(name = "parecer_tecnico")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "parecertecnico")
public class ParecerTecnico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusGeralEnum status;

    @NotNull
    @Column(name = "parec_numero", nullable = false)
    private String parecNumero;

    @NotNull
    @Column(name = "parec_objetivo", nullable = false)
    private String parecObjetivo;

    @Column(name = "parec_atendimento_in")
    private Boolean parecAtendimentoIn;

    @NotNull
    @Column(name = "parec_observacao", nullable = false)
    private String parecObservacao;

    @NotNull
    @Column(name = "parec_data_inicio", nullable = false)
    private LocalDate parecDataInicio;

    @ManyToOne
    private Analise analise;

    @OneToMany(mappedBy = "parecerTecnico")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ParecerItens> itens = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusGeralEnum getStatus() {
        return status;
    }

    public ParecerTecnico status(StatusGeralEnum status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusGeralEnum status) {
        this.status = status;
    }

    public String getParecNumero() {
        return parecNumero;
    }

    public ParecerTecnico parecNumero(String parecNumero) {
        this.parecNumero = parecNumero;
        return this;
    }

    public void setParecNumero(String parecNumero) {
        this.parecNumero = parecNumero;
    }

    public String getParecObjetivo() {
        return parecObjetivo;
    }

    public ParecerTecnico parecObjetivo(String parecObjetivo) {
        this.parecObjetivo = parecObjetivo;
        return this;
    }

    public void setParecObjetivo(String parecObjetivo) {
        this.parecObjetivo = parecObjetivo;
    }

    public Boolean isParecAtendimentoIn() {
        return parecAtendimentoIn;
    }

    public ParecerTecnico parecAtendimentoIn(Boolean parecAtendimentoIn) {
        this.parecAtendimentoIn = parecAtendimentoIn;
        return this;
    }

    public void setParecAtendimentoIn(Boolean parecAtendimentoIn) {
        this.parecAtendimentoIn = parecAtendimentoIn;
    }

    public String getParecObservacao() {
        return parecObservacao;
    }

    public ParecerTecnico parecObservacao(String parecObservacao) {
        this.parecObservacao = parecObservacao;
        return this;
    }

    public void setParecObservacao(String parecObservacao) {
        this.parecObservacao = parecObservacao;
    }

    public LocalDate getParecDataInicio() {
        return parecDataInicio;
    }

    public ParecerTecnico parecDataInicio(LocalDate parecDataInicio) {
        this.parecDataInicio = parecDataInicio;
        return this;
    }

    public void setParecDataInicio(LocalDate parecDataInicio) {
        this.parecDataInicio = parecDataInicio;
    }

    public Analise getAnalise() {
        return analise;
    }

    public ParecerTecnico analise(Analise analise) {
        this.analise = analise;
        return this;
    }

    public void setAnalise(Analise analise) {
        this.analise = analise;
    }

    public Set<ParecerItens> getItens() {
        return itens;
    }

    public ParecerTecnico itens(Set<ParecerItens> parecerItens) {
        this.itens = parecerItens;
        return this;
    }

    public ParecerTecnico addItens(ParecerItens parecerItens) {
        itens.add(parecerItens);
        parecerItens.setParecerTecnico(this);
        return this;
    }

    public ParecerTecnico removeItens(ParecerItens parecerItens) {
        itens.remove(parecerItens);
        parecerItens.setParecerTecnico(null);
        return this;
    }

    public void setItens(Set<ParecerItens> parecerItens) {
        this.itens = parecerItens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParecerTecnico parecerTecnico = (ParecerTecnico) o;
        if (parecerTecnico.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, parecerTecnico.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ParecerTecnico{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", parecNumero='" + parecNumero + "'" +
            ", parecObjetivo='" + parecObjetivo + "'" +
            ", parecAtendimentoIn='" + parecAtendimentoIn + "'" +
            ", parecObservacao='" + parecObservacao + "'" +
            ", parecDataInicio='" + parecDataInicio + "'" +
            '}';
    }
}
