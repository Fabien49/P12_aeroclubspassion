package com.fabienit.aeroclubspassion.domain;

import java.io.Serializable;
import java.time.Duration;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Avion.
 */
@Entity
@Table(name = "avion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Avion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "marque", nullable = false)
    private String marque;

    @Column(name = "type")
    private String type;

    @Column(name = "moteur")
    private String moteur;

    @Column(name = "puissance")
    private Integer puissance;

    @Column(name = "place")
    private Integer place;

    @Column(name = "autonomie")
    private Duration autonomie;

    @Column(name = "jhi_usage")
    private String usage;

    @Column(name = "heures")
    private Duration heures;


    @ManyToOne
    private Aeroclub aeroclub;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    @Column(name = "image")
    private String image;
    public Long getId() {
        return this.id;
    }

    public Avion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return this.marque;
    }

    public Avion marque(String marque) {
        this.setMarque(marque);
        return this;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getType() {
        return this.type;
    }

    public Avion type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoteur() {
        return this.moteur;
    }

    public Avion moteur(String moteur) {
        this.setMoteur(moteur);
        return this;
    }

    public void setMoteur(String moteur) {
        this.moteur = moteur;
    }

    public Integer getPuissance() {
        return this.puissance;
    }

    public Avion puissance(Integer puissance) {
        this.setPuissance(puissance);
        return this;
    }

    public void setPuissance(Integer puissance) {
        this.puissance = puissance;
    }

    public Integer getPlace() {
        return this.place;
    }

    public Avion place(Integer place) {
        this.setPlace(place);
        return this;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Duration getAutonomie() {
        return this.autonomie;
    }

    public Avion autonomie(Duration autonomie) {
        this.setAutonomie(autonomie);
        return this;
    }

    public void setAutonomie(Duration autonomie) {
        this.autonomie = autonomie;
    }

    public String getUsage() {
        return this.usage;
    }

    public Avion usage(String usage) {
        this.setUsage(usage);
        return this;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public Duration getHeures() {
        return this.heures;
    }

    public Avion heures(Duration heures) {
        this.setHeures(heures);
        return this;
    }

    public void setHeures(Duration heures) {
        this.heures = heures;
    }

    public String getImage() {
        return this.image;
    }

    public Avion image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Aeroclub getAeroclub() {
        return this.aeroclub;
    }

    public void setAeroclub(Aeroclub aeroclub) {
        this.aeroclub = aeroclub;
    }

    public Avion aeroclub(Aeroclub aeroclub) {
        this.setAeroclub(aeroclub);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Avion)) {
            return false;
        }
        return id != null && id.equals(((Avion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Avion{" +
            "id=" + getId() +
            ", marque='" + getMarque() + "'" +
            ", type='" + getType() + "'" +
            ", moteur='" + getMoteur() + "'" +
            ", puissance=" + getPuissance() +
            ", place=" + getPlace() +
            ", autonomie='" + getAutonomie() + "'" +
            ", usage='" + getUsage() + "'" +
            ", heures='" + getHeures() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
}
