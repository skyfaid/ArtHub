package entities;

import org.w3c.dom.Text;

public class categorie {
   private int idCategorie;
   private String nom ;
   private Text description ;

    public categorie(int idCategorie, String nom, Text description) {
        this.idCategorie = idCategorie;
        this.nom = nom;
        this.description = description;
    }

    public categorie(String nom, Text description) {
        this.nom = nom;
        this.description = description;
    }

    public categorie() {
    }

    public categorie(int id, String nom, String description) {
    }

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Text getDescription() {
        return description;
    }

    public void setDescription(Text description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "categorie{" +
                "idCategorie=" + idCategorie +
                ", nom='" + nom + '\'' +
                ", description=" + description +
                '}';
    }
}
