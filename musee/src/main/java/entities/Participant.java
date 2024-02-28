package entities;

import java.util.Date;

public class Participant {
    private int event_id;
    private int participant_id;
    private int utilisateur_id; // Add this line
    private String eventName; // Add event name property
    private String nom; // User's last name
    private String prenom; // User's first name
    private String gender;
    public Participant(int event_id, int participant_id, int utilisateur_id, String eventName) {
        this.event_id = event_id;
        this.participant_id = participant_id;
        this.utilisateur_id = utilisateur_id;
        this.eventName = eventName; // Initialize event name property
    }
    // Constructor used when all attributes are known
    public Participant(int event_id, int participant_id, int utilisateur_id, String eventName, String nom, String prenom) {
        this.event_id = event_id;
        this.participant_id = participant_id;
        this.utilisateur_id = utilisateur_id;
        this.eventName = eventName;
        this.nom = nom;
        this.prenom = prenom;
    }
    public Participant(int event_id, int participant_id, int utilisateur_id) {
        this.event_id = event_id;
        this.participant_id = participant_id;
        this.utilisateur_id = utilisateur_id;
    }
    public Participant(int event_id, int utilisateur_id) {
        this.event_id = event_id;
        this.utilisateur_id = utilisateur_id;
    }

    // Constructor with all fields
    public Participant(int event_id, int participant_id, int utilisateur_id, String eventName, String nom, String prenom, String gender) {
        this.event_id = event_id;
        this.participant_id = participant_id;
        this.utilisateur_id = utilisateur_id;
        this.eventName = eventName;
        this.nom = nom;
        this.prenom = prenom;
        this.gender = gender; // Initialize gender property
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public int getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setUtilisateur_id(int utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "event_id=" + event_id +
                ", participant_id=" + participant_id +
                ", utilisateur_id=" + utilisateur_id +
                ", eventName='" + eventName + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getParticipant_id() {
        return participant_id;
    }

    public void setParticipant_id(int participant_id) {
        this.participant_id = participant_id;
    }

    // Getters and setters for other properties

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


}
