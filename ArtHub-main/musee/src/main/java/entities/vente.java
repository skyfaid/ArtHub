package entities;

import java.sql.Date;

public class vente {
    private int ID, ID_OeuvreVendue, id_client, quantite;
    private Date DateVente;
    private String ModePaiement;
    private double PrixVente;

    public vente(int id, int id_oeuvrevendue, Date datevente, double prix_vente, String modePaiement, int quantite) {
        this.ID = id; // Correction ici: utilisez le paramètre id au lieu de ID
        this.ID_OeuvreVendue = id_oeuvrevendue; // Correction ici: utilisez le paramètre id_oeuvrevendue
        this.id_client = id_client; // Aucun changement, déjà correct
        this.DateVente = datevente; // Correction ici: utilisez le paramètre datevente
        this.ModePaiement = modePaiement; // Correction ici: utilisez le paramètre modePaiement
        this.PrixVente = prix_vente; // Correction ici: utilisez le paramètre prix_vente
        this.quantite = quantite; // Aucun changement, déjà correct
    }

    public vente() {

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID_OeuvreVendue() {
        return ID_OeuvreVendue;
    }

    public void setID_OeuvreVendue(int ID_OeuvreVendue) {
        this.ID_OeuvreVendue = ID_OeuvreVendue;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Date getDateVente() {
        return DateVente;
    }

    public void setDateVente(Date dateVente) {
        DateVente = dateVente;
    }

    public String getModePaiement() {
        return ModePaiement;
    }

    public void setModePaiement(String modePaiement) {
        ModePaiement = modePaiement;
    }

    public double getPrixVente() {
        return PrixVente;
    }

    public void setPrixVente(double prixVente) {
        PrixVente = prixVente;
    }

    @Override
    public String toString() {
        return "vente{" +
                "ID=" + ID +
                ", ID_OeuvreVendue=" + ID_OeuvreVendue +
                ", id_client=" + id_client +
                ", quantite=" + quantite +
                ", DateVente=" + DateVente +
                ", ModePaiement='" + ModePaiement + '\'' +
                ", PrixVente=" + PrixVente +
                '}';
    }



}
