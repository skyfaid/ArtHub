package entities;

import javafx.scene.control.DatePicker;

public class Formations {
    private int id,nbr_participants;
    private String lien,date_fin,date_debut;

    public Formations() {
    }
    public Formations(int id, int nbr_participants, String lien, String date_fin, String date_debut) {
        this.id = id;
        this.nbr_participants = nbr_participants;
        this.lien = lien;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
    }
    public Formations( int nbr_participants, String lien, String date_fin, String date_debut) {
        this.nbr_participants = nbr_participants;
        this.lien = lien;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
    }

    public Formations(int i, String text, DatePicker dateFTf, DatePicker dateDTf) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbr_participants() {
        return nbr_participants;
    }

    public void setNbr_participants(int nbr_participants) {
        this.nbr_participants = nbr_participants;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public String getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(String date_fin) {
        this.date_fin = date_fin;
    }

    public String getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(String date_debut) {
        this.date_debut = date_debut;
    }

    @Override
    public String toString() {
        return "Formations{" +
                "id=" + id +
                ", nbr_participants=" + nbr_participants +
                ", lien='" + lien + '\'' +
                ", date_fin='" + date_fin + '\'' +
                ", date_debut='" + date_debut + '\'' +
                '}';
    }
}
