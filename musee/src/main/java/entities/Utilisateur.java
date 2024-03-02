package entities;


import java.util.Date;

public class Utilisateur {

    private int utilisateurId;

    private String pseudo;

    private String prenom;

    private String nom;

    private String email;

    private String motDePasseHash;

    private String role="user";

    private Date dateInscription;

    private Date derniereConnexion;

    private String urlImageProfil;

    private boolean estActif;

    private String gender;
    private String phoneNumber;
    public Utilisateur() {
    }

    public Utilisateur(String pseudo, String prenom, String nom, String email, String motDePasseHash,String gender,String phone) {
        this.pseudo = pseudo;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.motDePasseHash = motDePasseHash;
        this.gender=gender;
        this.phoneNumber=phone;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {

        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public boolean isEstActif() {
        return estActif;
    }

    public void setEstActif(boolean estActif) {
        this.estActif = estActif;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasseHash() {
        return motDePasseHash;
    }

    public void setMotDePasseHash(String motDePasseHash) {
        this.motDePasseHash = motDePasseHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Date getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(Date derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    public String getUrlImageProfil() {
        return urlImageProfil;
    }

    public void setUrlImageProfil(String urlImageProfil) {
        this.urlImageProfil = urlImageProfil;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "utilisateurId=" + utilisateurId +
                ", pseudo='" + pseudo + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", motDePasseHash='" + motDePasseHash + '\'' +
                ", role='" + role + '\'' +
                ", dateInscription=" + dateInscription +
                ", derniereConnexion=" + derniereConnexion +
                ", urlImageProfil='" + urlImageProfil + '\'' +
                '}';
    }


}

