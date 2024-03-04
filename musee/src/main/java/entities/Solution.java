package entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Solution {
    private int SolutionID;
    private int ReclamationID;
    private int utilisateur_id;
    private String Status;
    private float RefundAmount;
    private String AdminFeedback;
    private Timestamp DateResolved;
    private int oeuvreId;



    // Default constructor
    public Solution() {
        this.DateResolved = Timestamp.valueOf(LocalDateTime.now());
    }

    // Parameterized constructor
    public Solution(int SolutionID, int ReclamationID, int utilisateur_id, String Status, float RefundAmount, String AdminFeedback, LocalDateTime DateResolved, int oeuvreId) {
        this.SolutionID = SolutionID;
        this.ReclamationID = ReclamationID;
        this.utilisateur_id = utilisateur_id;
        this.Status = Status;
        this.RefundAmount = RefundAmount;
        this.AdminFeedback = AdminFeedback;
        this.DateResolved = Timestamp.valueOf(DateResolved != null ? DateResolved : LocalDateTime.now()); // Correctly set to current date and time if null
        this.oeuvreId = oeuvreId; // Correctly assign oeuvreId
    }

    public int getSolutionID() {
        return SolutionID;
    }

    public void setSolutionID(int SolutionID) {
        this.SolutionID = SolutionID;
    }

    public int getReclamationID() {
        return ReclamationID;
    }

    public void setReclamationID(int ReclamationID) {
        this.ReclamationID = ReclamationID;
    }

    public int getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setUtilisateur_id(int utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public float getRefundAmount() {
        return RefundAmount;
    }

    public void setRefundAmount(float RefundAmount) {
        this.RefundAmount = RefundAmount;
    }

    public String getAdminFeedback() {
        return AdminFeedback;
    }

    public void setAdminFeedback(String AdminFeedback) {
        this.AdminFeedback = AdminFeedback;
    }
    // Getters and Setters


    public Timestamp getDateResolved() {
        return DateResolved;
    }

    public void setDateResolved(Timestamp DateResolved) {
        this.DateResolved = DateResolved;
    }


    public int getOeuvreId() {
        return oeuvreId;
    }

    public void setOeuvreId(int oeuvreId) {
        this.oeuvreId = oeuvreId;
    }

    @Override
    public String toString() {
        // Update the toString method to include oeuvreId
        return "Solution{" +
                "SolutionID=" + SolutionID +
                ", ReclamationID=" + ReclamationID +
                ", utilisateur_id=" + utilisateur_id +
                ", Status='" + Status + '\'' +
                ", RefundAmount=" + RefundAmount +
                ", AdminFeedback='" + AdminFeedback + '\'' +
                ", DateResolved=" + DateResolved +
                ", OeuvreID=" + oeuvreId + // Include OeuvreID in the toString representation
                '}';
    }


}
