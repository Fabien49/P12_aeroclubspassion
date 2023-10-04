package com.fabienit.aeroclubspassion.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tarif.
 */
@Entity
@Table(name = "tarif")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tarif implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "taxe_atterrissage")
    private Double taxeAtterrissage;

    @Column(name = "taxe_parking")
    private Double taxeParking;

    @Column(name = "carburant")
    private Double carburant;

    @OneToOne
    @JoinColumn(unique = true)
    private Aeroclub aeroclub;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tarif id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTaxeAtterrissage() {
        return this.taxeAtterrissage;
    }

    public Tarif taxeAtterrissage(Double taxeAtterrissage) {
        this.setTaxeAtterrissage(taxeAtterrissage);
        return this;
    }

    public void setTaxeAtterrissage(Double taxeAtterrissage) {
        this.taxeAtterrissage = taxeAtterrissage;
    }

    public Double getTaxeParking() {
        return this.taxeParking;
    }

    public Tarif taxeParking(Double taxeParking) {
        this.setTaxeParking(taxeParking);
        return this;
    }

    public void setTaxeParking(Double taxeParking) {
        this.taxeParking = taxeParking;
    }

    public Double getCarburant() {
        return this.carburant;
    }

    public Tarif carburant(Double carburant) {
        this.setCarburant(carburant);
        return this;
    }

    public void setCarburant(Double carburant) {
        this.carburant = carburant;
    }

    public Aeroclub getAeroclub() {
        return this.aeroclub;
    }

    public void setAeroclub(Aeroclub aeroclub) {
        this.aeroclub = aeroclub;
    }

    public Tarif aeroclub(Aeroclub aeroclub) {
        this.setAeroclub(aeroclub);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tarif)) {
            return false;
        }
        return id != null && id.equals(((Tarif) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tarif{" +
            "id=" + getId() +
            ", taxeAtterrissage=" + getTaxeAtterrissage() +
            ", taxeParking=" + getTaxeParking() +
            ", carburant=" + getCarburant() +
            "}";
    }
}
