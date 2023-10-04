package com.fabienit.aeroclubspassion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_emprunt")
    private ZonedDateTime dateEmprunt;

    @Column(name = "date_retour")
    private ZonedDateTime dateRetour;

    @JsonIgnoreProperties(value = { "aeroclub" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Avion avion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reservation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateEmprunt() {
        return this.dateEmprunt;
    }

    public Reservation dateEmprunt(ZonedDateTime dateEmprunt) {
        this.setDateEmprunt(dateEmprunt);
        return this;
    }

    public void setDateEmprunt(ZonedDateTime dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public ZonedDateTime getDateRetour() {
        return this.dateRetour;
    }

    public Reservation dateRetour(ZonedDateTime dateRetour) {
        this.setDateRetour(dateRetour);
        return this;
    }

    public void setDateRetour(ZonedDateTime dateRetour) {
        this.dateRetour = dateRetour;
    }

    public Avion getAvion() {
        return this.avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public Reservation avion(Avion avion) {
        this.setAvion(avion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        return id != null && id.equals(((Reservation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + getId() +
            ", dateEmprunt='" + getDateEmprunt() + "'" +
            ", dateRetour='" + getDateRetour() + "'" +
            "}";
    }
}
