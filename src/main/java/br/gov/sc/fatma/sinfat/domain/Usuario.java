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
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusGeralEnum status;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "cpf", nullable = false)
    private String cpf;

    @OneToOne
    @JoinColumn(unique = true)
    private Contato contato;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany(mappedBy = "responsaveis")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Empreendimento> empreendimentos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusGeralEnum getStatus() {
        return status;
    }

    public Usuario status(StatusGeralEnum status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusGeralEnum status) {
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public Usuario nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public Usuario cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Contato getContato() {
        return contato;
    }

    public Usuario contato(Contato contato) {
        this.contato = contato;
        return this;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public User getUser() {
        return user;
    }

    public Usuario user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Empreendimento> getEmpreendimentos() {
        return empreendimentos;
    }

    public Usuario empreendimentos(Set<Empreendimento> empreendimentos) {
        this.empreendimentos = empreendimentos;
        return this;
    }

    public Usuario addEmpreendimentos(Empreendimento empreendimento) {
        empreendimentos.add(empreendimento);
        empreendimento.getResponsaveis().add(this);
        return this;
    }

    public Usuario removeEmpreendimentos(Empreendimento empreendimento) {
        empreendimentos.remove(empreendimento);
        empreendimento.getResponsaveis().remove(this);
        return this;
    }

    public void setEmpreendimentos(Set<Empreendimento> empreendimentos) {
        this.empreendimentos = empreendimentos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Usuario usuario = (Usuario) o;
        if (usuario.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", nome='" + nome + "'" +
            ", cpf='" + cpf + "'" +
            '}';
    }
}
