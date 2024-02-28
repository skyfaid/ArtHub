package entities;


import java.time.LocalDate;

public class Evenement {
    private int id;
    private String nom;
    private LocalDate datedebut;
    private LocalDate datefin;
    private String lieu;
    private String type;
    private String description;
    private int nombrePlaces;
    private String posterUrl; // New attribute for storing image URL

    private int nombreParticipants;


    // Full constructor including all attributes including poster
    public Evenement(int id, String nom, LocalDate datedebut, LocalDate datefin, String lieu, String type, String description, int nombrePlaces, String posterUrl) {
        this.id = id;
        this.nom = nom;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.lieu = lieu;
        this.type = type;
        this.description = description;
        this.nombrePlaces = nombrePlaces;
        this.posterUrl = posterUrl; // Initialize the posterUrl attribute
    }

    // Second constructor without id and poster
    public Evenement(String nom, LocalDate datedebut, LocalDate datefin, String lieu, String type, String description, int nombrePlaces, String posterUrl) {
        this.nom = nom;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.lieu = lieu;
        this.type = type;
        this.description = description;
        this.nombrePlaces = nombrePlaces;
        this.posterUrl = posterUrl; // Initialize the posterUrl attribute
    }

    // Constructor with only id for deletion purposes
    public Evenement(int id) {
        this.id = id;
    }

    public Evenement(int id, String nom, LocalDate datedebut, LocalDate datefin, String lieu, String type, String description, int nombrePlaces, String posterUrl, int nombreParticipants) {
        this.nom = nom;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.lieu = lieu;
        this.type = type;
        this.description = description;
        this.nombrePlaces = nombrePlaces;
        this.posterUrl = posterUrl; // Initialize the posterUrl attribute
        this.nombreParticipants = nombreParticipants;
    }

    public int getNombreParticipants() {
        return nombreParticipants;
    }

    public void setNombreParticipants(int nombreParticipants) {
        this.nombreParticipants = nombreParticipants;
    }

    // Getters and setters for posterUrl attribute
    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    // Getters and setters for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and setters for nom
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Getters and setters for datedebut
    public LocalDate getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(LocalDate datedebut) {
        this.datedebut = datedebut;
    }

    // Getters and setters for datefin
    public LocalDate getDatefin() {
        return datefin;
    }

    public void setDatefin(LocalDate datefin) {
        this.datefin = datefin;
    }

    // Getters and setters for lieu
    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    // Getters and setters for type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Getters and setters for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getters and setters for nombrePlaces
    public int getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(int nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", datedebut=" + datedebut +
                ", datefin=" + datefin +
                ", lieu='" + lieu + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", nombrePlaces=" + nombrePlaces +
                ", posterUrl='" + posterUrl + '\'' +
                '}';
    }


}











