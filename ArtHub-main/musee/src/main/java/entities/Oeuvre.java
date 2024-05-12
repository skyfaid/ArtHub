package entities;

import java.time.LocalDate;

public class Oeuvre {
    private int id;
    private String titre, description, disponibilite, type;
    private LocalDate dateCreation;
    private Double prix;
    private String posterUrl;

    public Oeuvre(int id, String titre, String description, String disponibilite, String type, LocalDate dateCreation, Double prix, String posterUrl) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.disponibilite = disponibilite;
        this.type = type;
        this.dateCreation = dateCreation;
        this.prix = prix;
        this.posterUrl = posterUrl;
    }

    public Oeuvre() {
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    @Override
    public String toString() {
        return "Oeuvre{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", disponibilite='" + disponibilite + '\'' +
                ", type='" + type + '\'' +
                ", dateCreation=" + dateCreation +
                ", prix=" + prix +
                ", posterUrl='" + posterUrl + '\'' +
                '}';
    }
}
