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

import br.gov.sc.fatma.sinfat.domain.enumeration.CargoEnum;

/**
 * A Colaborador.
 */
@Entity
@Table(name = "colaborador")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "colaborador")
public class Colaborador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", nullable = false)
    private CargoEnum cargo;

    @OneToOne
    @JoinColumn(unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "colaborador")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Municipio> municipios = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CargoEnum getCargo() {
        return cargo;
    }

    public Colaborador cargo(CargoEnum cargo) {
        this.cargo = cargo;
        return this;
    }

    public void setCargo(CargoEnum cargo) {
        this.cargo = cargo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Colaborador usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<Municipio> getMunicipios() {
        return municipios;
    }

    public Colaborador municipios(Set<Municipio> municipios) {
        this.municipios = municipios;
        return this;
    }

    public Colaborador addMunicipio(Municipio municipio) {
        municipios.add(municipio);
        municipio.setColaborador(this);
        return this;
    }

    public Colaborador removeMunicipio(Municipio municipio) {
        municipios.remove(municipio);
        municipio.setColaborador(null);
        return this;
    }

    public void setMunicipios(Set<Municipio> municipios) {
        this.municipios = municipios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Colaborador colaborador = (Colaborador) o;
        if (colaborador.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, colaborador.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Colaborador{" +
            "id=" + id +
            ", cargo='" + cargo + "'" +
            '}';
    }
}
