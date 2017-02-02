package br.gov.sc.fatma.sinfat.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import br.gov.sc.fatma.sinfat.domain.enumeration.StatusGeralEnum;

import br.gov.sc.fatma.sinfat.domain.enumeration.TipoLicencaEnum;

/**
 * A Licenca.
 */
@Entity
@Table(name = "licenca")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "licenca")
public class Licenca implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusGeralEnum status;

    @NotNull
    @Column(name = "licen_numero", nullable = false)
    private String licenNumero;

    @NotNull
    @Column(name = "licen_condicoes_validade", nullable = false)
    private String licenCondicoesValidade;

    @NotNull
    @Min(value = 1)
    @Column(name = "licen_validade", nullable = false)
    private Integer licenValidade;

    @NotNull
    @Column(name = "licen_caracteristica", nullable = false)
    private String licenCaracteristica;

    @NotNull
    @Column(name = "licen_data_emissao", nullable = false)
    private LocalDate licenDataEmissao;

    @Column(name = "licen_data_entrega")
    private LocalDate licenDataEntrega;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoLicencaEnum tipo;

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

    public Licenca status(StatusGeralEnum status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusGeralEnum status) {
        this.status = status;
    }

    public String getLicenNumero() {
        return licenNumero;
    }

    public Licenca licenNumero(String licenNumero) {
        this.licenNumero = licenNumero;
        return this;
    }

    public void setLicenNumero(String licenNumero) {
        this.licenNumero = licenNumero;
    }

    public String getLicenCondicoesValidade() {
        return licenCondicoesValidade;
    }

    public Licenca licenCondicoesValidade(String licenCondicoesValidade) {
        this.licenCondicoesValidade = licenCondicoesValidade;
        return this;
    }

    public void setLicenCondicoesValidade(String licenCondicoesValidade) {
        this.licenCondicoesValidade = licenCondicoesValidade;
    }

    public Integer getLicenValidade() {
        return licenValidade;
    }

    public Licenca licenValidade(Integer licenValidade) {
        this.licenValidade = licenValidade;
        return this;
    }

    public void setLicenValidade(Integer licenValidade) {
        this.licenValidade = licenValidade;
    }

    public String getLicenCaracteristica() {
        return licenCaracteristica;
    }

    public Licenca licenCaracteristica(String licenCaracteristica) {
        this.licenCaracteristica = licenCaracteristica;
        return this;
    }

    public void setLicenCaracteristica(String licenCaracteristica) {
        this.licenCaracteristica = licenCaracteristica;
    }

    public LocalDate getLicenDataEmissao() {
        return licenDataEmissao;
    }

    public Licenca licenDataEmissao(LocalDate licenDataEmissao) {
        this.licenDataEmissao = licenDataEmissao;
        return this;
    }

    public void setLicenDataEmissao(LocalDate licenDataEmissao) {
        this.licenDataEmissao = licenDataEmissao;
    }

    public LocalDate getLicenDataEntrega() {
        return licenDataEntrega;
    }

    public Licenca licenDataEntrega(LocalDate licenDataEntrega) {
        this.licenDataEntrega = licenDataEntrega;
        return this;
    }

    public void setLicenDataEntrega(LocalDate licenDataEntrega) {
        this.licenDataEntrega = licenDataEntrega;
    }

    public TipoLicencaEnum getTipo() {
        return tipo;
    }

    public Licenca tipo(TipoLicencaEnum tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoLicencaEnum tipo) {
        this.tipo = tipo;
    }

    public Empreendimento getEmpreendimento() {
        return empreendimento;
    }

    public Licenca empreendimento(Empreendimento empreendimento) {
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
        Licenca licenca = (Licenca) o;
        if (licenca.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, licenca.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Licenca{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", licenNumero='" + licenNumero + "'" +
            ", licenCondicoesValidade='" + licenCondicoesValidade + "'" +
            ", licenValidade='" + licenValidade + "'" +
            ", licenCaracteristica='" + licenCaracteristica + "'" +
            ", licenDataEmissao='" + licenDataEmissao + "'" +
            ", licenDataEntrega='" + licenDataEntrega + "'" +
            ", tipo='" + tipo + "'" +
            '}';
    }
}
