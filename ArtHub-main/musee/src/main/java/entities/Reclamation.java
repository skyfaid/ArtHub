package entities;

import java.time.LocalDateTime;

public class Reclamation {
    private int ReclamationID;
    private int utilisateur_id;
    private int id;
    private String Description;
    private String productPNG;
    private String Status;
    private LocalDateTime DateSubmitted;
    private String phoneNumber ;



    // Default constructor
    public Reclamation() {
        this.Status = "pending";
    }


    public Reclamation(int ReclamationID, int utilisateur_id, int id, String Description, String productPNG, String Status, LocalDateTime DateSubmitted , String phoneNumber) {
        this.ReclamationID = ReclamationID;
        this.utilisateur_id = utilisateur_id;
        this.id = id;
        this.Description = Description;
        this.productPNG = productPNG;
        this.Status = Status;
        this.DateSubmitted = DateSubmitted;
        this.phoneNumber = phoneNumber;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getProductPNG() {
        return productPNG;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setProductPNG(String productPNG) {
        this.productPNG = productPNG;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public LocalDateTime getDateSubmitted() {
        return DateSubmitted;
    }

    public void setDateSubmitted(LocalDateTime DateSubmitted) {
        this.DateSubmitted = DateSubmitted;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString() {
        return "Reclamation{" +
                "ReclamationID=" + ReclamationID +
                ", utilisateur_id=" + utilisateur_id +
                ", id=" + id +
                ", Description='" + Description + '\'' +
                ", productPNG='" + productPNG + '\'' +
                ", Status='" + Status + '\'' +
                ", DateSubmitted=" + DateSubmitted + '\'' +
                ", PhoneNumber='" + phoneNumber + '\'' +


                '}';
    }
}
