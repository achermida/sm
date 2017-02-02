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

/**
 * Classe principal do nosso sistema de financas
 */
@ApiModel(description = "Classe principal do nosso sistema de financas")
@Entity
@Table(name = "empreendimento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "empreendimento")
public class Empreendimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Column(name = "razao_social")
    private String razaoSocial;

    @NotNull
    @Column(name = "x", nullable = false)
    private Double x;

    @NotNull
    @Column(name = "y", nullable = false)
    private Double y;

    @NotNull
    @Column(name = "dt_cadastro", nullable = false)
    private LocalDate dtCadastro;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusGeralEnum status;

    @OneToOne
    @JoinColumn(unique = true)
    private Endereco endereco;

    @OneToOne
    @JoinColumn(unique = true)
    private Usuario responsavel;

    @OneToMany(mappedBy = "empreendimento")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Processo> processos = new HashSet<>();

    @OneToMany(mappedBy = "empreendimento")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Licenca> licencas = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "empreendimento_responsaveis",
               joinColumns = @JoinColumn(name="empreendimentos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="responsaveis_id", referencedColumnName="ID"))
    private Set<Usuario> responsaveis = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public Empreendimento cnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public Empreendimento razaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
        return this;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public Double getX() {
        return x;
    }

    public Empreendimento x(Double x) {
        this.x = x;
        return this;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public Empreendimento y(Double y) {
        this.y = y;
        return this;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    public Empreendimento dtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
        return this;
    }

    public void setDtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public StatusGeralEnum getStatus() {
        return status;
    }

    public Empreendimento status(StatusGeralEnum status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusGeralEnum status) {
        this.status = status;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Empreendimento endereco(Endereco endereco) {
        this.endereco = endereco;
        return this;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public Empreendimento responsavel(Usuario usuario) {
        this.responsavel = usuario;
        return this;
    }

    public void setResponsavel(Usuario usuario) {
        this.responsavel = usuario;
    }

    public Set<Processo> getProcessos() {
        return processos;
    }

    public Empreendimento processos(Set<Processo> processos) {
        this.processos = processos;
        return this;
    }

    public Empreendimento addProcessos(Processo processo) {
        processos.add(processo);
        processo.setEmpreendimento(this);
        return this;
    }

    public Empreendimento removeProcessos(Processo processo) {
        processos.remove(processo);
        processo.setEmpreendimento(null);
        return this;
    }

    public void setProcessos(Set<Processo> processos) {
        this.processos = processos;
    }

    public Set<Licenca> getLicencas() {
        return licencas;
    }

    public Empreendimento licencas(Set<Licenca> licencas) {
        this.licencas = licencas;
        return this;
    }

    public Empreendimento addLicencas(Licenca licenca) {
        licencas.add(licenca);
        licenca.setEmpreendimento(this);
        return this;
    }

    public Empreendimento removeLicencas(Licenca licenca) {
        licencas.remove(licenca);
        licenca.setEmpreendimento(null);
        return this;
    }

    public void setLicencas(Set<Licenca> licencas) {
        this.licencas = licencas;
    }

    public Set<Usuario> getResponsaveis() {
        return responsaveis;
    }

    public Empreendimento responsaveis(Set<Usuario> usuarios) {
        this.responsaveis = usuarios;
        return this;
    }

    public Empreendimento addResponsaveis(Usuario usuario) {
        responsaveis.add(usuario);
        usuario.getEmpreendimentos().add(this);
        return this;
    }

    public Empreendimento removeResponsaveis(Usuario usuario) {
        responsaveis.remove(usuario);
        usuario.getEmpreendimentos().remove(this);
        return this;
    }

    public void setResponsaveis(Set<Usuario> usuarios) {
        this.responsaveis = usuarios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Empreendimento empreendimento = (Empreendimento) o;
        if (empreendimento.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, empreendimento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Empreendimento{" +
            "id=" + id +
            ", cnpj='" + cnpj + "'" +
            ", razaoSocial='" + razaoSocial + "'" +
            ", x='" + x + "'" +
            ", y='" + y + "'" +
            ", dtCadastro='" + dtCadastro + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
