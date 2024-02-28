package entities;

import java.util.Date;

public class Participant {
    private int event_id;
    private int participant_id;
    private int utilisateur_id; // Add this line
    private String eventName; // Add event name property

    public Participant(int event_id, int participant_id, int utilisateur_id, String eventName) {
        this.event_id = event_id;
        this.participant_id = participant_id;
        this.utilisateur_id = utilisateur_id;
        this.eventName = eventName; // Initialize event name property
    }

    public Participant(int event_id, int participant_id, int utilisateur_id) {
        this.event_id = event_id;
        this.participant_id = participant_id;
        this.utilisateur_id = utilisateur_id;
    }

    public Participant(int eventId, int participantId, int utilisateurId, String userNom, String userPrenom, String eventNom) {
    }

    public int getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setUtilisateur_id(int utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "event_id=" + event_id +
                ", participant_id=" + participant_id +
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
