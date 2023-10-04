package com.fabienit.aeroclubspassion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Atelier.
 */
@Entity
@Table(name = "atelier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Atelier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "compteur_chgt_moteur")
    private Integer compteurChgtMoteur;

    @Column(name = "compteur_carrosserie")
    private Integer compteurCarrosserie;

    @Column(name = "compteur_helisse")
    private Integer compteurHelisse;

    @Column(name = "date")
    private ZonedDateTime date;

    @ManyToOne
    @JsonIgnoreProperties(value = { "aeroclub" }, allowSetters = true)
    private Avion avion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Atelier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCompteurChgtMoteur() {
        return this.compteurChgtMoteur;
    }

    public Atelier compteurChgtMoteur(Integer compteurChgtMoteur) {
        this.setCompteurChgtMoteur(compteurChgtMoteur);
        return this;
    }

    public void setCompteurChgtMoteur(Integer compteurChgtMoteur) {
        this.compteurChgtMoteur = compteurChgtMoteur;
    }

    public Integer getCompteurCarrosserie() {
        return this.compteurCarrosserie;
    }

    public Atelier compteurCarrosserie(Integer compteurCarrosserie) {
        this.setCompteurCarrosserie(compteurCarrosserie);
        return this;
    }

    public void setCompteurCarrosserie(Integer compteurCarrosserie) {
        this.compteurCarrosserie = compteurCarrosserie;
    }

    public Integer getCompteurHelisse() {
        return this.compteurHelisse;
    }

    public Atelier compteurHelisse(Integer compteurHelisse) {
        this.setCompteurHelisse(compteurHelisse);
        return this;
    }

    public void setCompteurHelisse(Integer compteurHelisse) {
        this.compteurHelisse = compteurHelisse;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Atelier date(ZonedDateTime date) {
        this.setDate(date);
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Avion getAvion() {
        return this.avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public Atelier avion(Avion avion) {
        this.setAvion(avion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Atelier)) {
            return false;
        }
        return id != null && id.equals(((Atelier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Atelier{" +
            "id=" + getId() +
            ", compteurChgtMoteur=" + getCompteurChgtMoteur() +
            ", compteurCarrosserie=" + getCompteurCarrosserie() +
            ", compteurHelisse=" + getCompteurHelisse() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
