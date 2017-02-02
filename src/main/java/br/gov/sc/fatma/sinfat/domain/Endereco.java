package br.gov.sc.fatma.sinfat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Endereco.
 */
@Entity
@Table(name = "endereco")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "endereco")
public class Endereco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "logradouro", nullable = false)
    private String logradouro;

    @Column(name = "numero")
    private Integer numero;

    @NotNull
    @Column(name = "bairro", nullable = false)
    private String bairro;

    @Column(name = "complemento")
    private String complemento;

    @NotNull
    @Column(name = "cep", nullable = false)
    private String cep;

    @OneToOne
    @JoinColumn(unique = true)
    private Municipio municipio;

    @OneToOne(mappedBy = "endereco")
    @JsonIgnore
    private Empreendimento empreendimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public Endereco logradouro(String logradouro) {
        this.logradouro = logradouro;
        return this;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Integer getNumero() {
        return numero;
    }

    public Endereco numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public Endereco bairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public Endereco complemento(String complemento) {
        this.complemento = complemento;
        return this;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCep() {
        return cep;
    }

    public Endereco cep(String cep) {
        this.cep = cep;
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public Endereco municipio(Municipio municipio) {
        this.municipio = municipio;
        return this;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Empreendimento getEmpreendimento() {
        return empreendimento;
    }

    public Endereco empreendimento(Empreendimento empreendimento) {
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
        Endereco endereco = (Endereco) o;
        if (endereco.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, endereco.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Endereco{" +
            "id=" + id +
            ", logradouro='" + logradouro + "'" +
            ", numero='" + numero + "'" +
            ", bairro='" + bairro + "'" +
            ", complemento='" + complemento + "'" +
            ", cep='" + cep + "'" +
            '}';
    }
}
