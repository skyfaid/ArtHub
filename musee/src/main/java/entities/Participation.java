package entities;

import java.util.Date;

public class Participation {
    private int id_participation;
    private int id_activite;
    private int utilisateur_id;
    private int score;
    private Date participation_date;

    // Constructor
    public Participation(int id_participation, int id_activite, int utilisateur_id, int score, Date participation_date) {
        this.id_participation = id_participation;
        this.id_activite = id_activite;
        this.utilisateur_id = utilisateur_id;
        this.score = score;
        this.participation_date = participation_date;
    }

    // Getters and Setters
    public int getId_participation() {
        return id_participation;
    }

    public void setId_participation(int id_participation) {
        this.id_participation = id_participation;
    }

    public int getId_activite() {
        return id_activite;
    }

    public void setId_activite(int id_activite) {
        this.id_activite = id_activite;
    }

    public int getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setutilisateur_id(int utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getParticipation_date() {
        return participation_date;
    }

    public void setParticipation_date(Date participation_date) {
        this.participation_date = participation_date;
    }

    // toString method
    @Override
    public String toString() {
        return "Participation{" +
                "id_participation=" + id_participation +
                ", id_activite=" + id_activite +
                ", id_user=" + utilisateur_id +
                ", score=" + score +
                ", participation_date=" + participation_date +
                '}';
    }
}
