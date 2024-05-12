package entities;

import java.time.LocalDate;

public class Activite {
    private int id_activite;
    private String nom_act;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String Lieu;
    private int nbre_places;
    private String type_act;
    private String posterUrl ;

    // Constructor
    public Activite(int id_activite, String nom_act, LocalDate dateDebut, LocalDate dateFin, String Lieu, int nbre_places, String type_act, String posterUrl) {
        this.id_activite = id_activite;
        this.nom_act = nom_act;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.Lieu = Lieu;
        this.nbre_places = nbre_places;
        this.type_act = type_act;
        this.posterUrl = posterUrl ;
    }

    public Activite(String nom_act, LocalDate dateDebut, LocalDate dateFin, String Lieu, int nbre_places, String type_act, String posterUrl) {
        this.nom_act = nom_act;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.Lieu = Lieu;
        this.nbre_places = nbre_places;
        this.type_act = type_act;
        this.posterUrl = posterUrl ;
    }


    public Activite() {


    }
    public Activite(int id_activite) {
        this.id_activite=id_activite;


    }

    public Activite(int idActivite, String nomAct, LocalDate datedebut, LocalDate datefin, String lieu, int nbrePlaces, String typeAct) {
        this.nom_act = nom_act;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.Lieu = Lieu;
        this.nbre_places = nbre_places;
        this.type_act = type_act;
    }


    // public Activite(String text, LocalDate dateDebut, LocalDate dateFin, String text1, int nbrePlaces, String text2) {}

    // Getters and Setters
    public int getId_activite() {
        return id_activite;
    }

    public void setId_activite(int id_activite) {
        this.id_activite = id_activite;
    }

    public String getNom_act() {
        return nom_act;
    }

    public void setNom_act(String nom_act) {
        this.nom_act = nom_act;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getLieu() {
        return Lieu;
    }

    public void setLieu(String Lieu) {
        this.Lieu = Lieu;
    }

    public int getNbre_places() {
        return nbre_places;
    }

    public void setNbre_places(int nbre_places) {
        this.nbre_places = nbre_places;
    }

    public String getType_act() {
        return type_act;
    }

    public void setType_act(String type_act) {
        this.type_act = type_act;
    }
    
    @Override

    public String toString() {
        return "Activite{" +
                "id_activite=" + id_activite +
                ", nom_act='" + nom_act + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", Lieu='" + Lieu + '\'' +
                ", nbre_places=" + nbre_places +
                ", type_act='" + type_act + '\'' +
                '}';
    }

    // Getters and setters for posterUrl attribute
    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}
