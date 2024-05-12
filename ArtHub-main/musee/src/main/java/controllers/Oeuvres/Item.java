package controllers.Oeuvres;

import entities.vente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Item {
    @FXML
    private Label datevente;

    @FXML
    private Button deletevente;

    @FXML
    private Label idclient;

    @FXML
    private Label idoeuvree;

    @FXML
    private Label modepaim;

    @FXML
    private Label prixoeuvre;

    @FXML
    private Label quantitee;

    @FXML
    private Button updatevente;
    private int venteId;
    private Admin adminController;
    public void setData(vente sale , Admin admin) {
        this.venteId = sale.getID();
        this.adminController = admin;
        //idclient.setText(String.valueOf(sale.getId_client()));
        idoeuvree.setText(String.valueOf(sale.getID_OeuvreVendue()));
        datevente.setText(String.valueOf(sale.getDateVente()));
        prixoeuvre.setText(String.format("%.2f", sale.getPrixVente()));
        modepaim.setText(sale.getModePaiement());
        quantitee.setText(String.valueOf(sale.getQuantite()));
    }


    @FXML
    void handleDeleteAction(ActionEvent event) {
        adminController.supprimerVente(venteId);
    }
}
