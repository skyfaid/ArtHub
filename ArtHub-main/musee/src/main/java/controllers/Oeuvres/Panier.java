package controllers.Oeuvres;

import controllers.AdminInterfaceController;
import controllers.UserInterfaceController;
import controllers.Users.UpdateAccountController;
import entities.Oeuvre;
import entities.Utilisateur;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.UserConnected;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Panier implements  Initializable {
    @FXML
    private VBox idpanierr;
    @FXML
    private AnchorPane panierContainer;
    public static List<Oeuvre> panier = new ArrayList<>();
    private Utilisateur currUser;
    @FXML
    private Label totalachaat;
    @FXML
    private Button payy;
    private double totaux = 0;
    private PanierClient panierClient=new PanierClient();
    @FXML
    void payment(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiOeuvre/Paiment.fxml")); // Assurez-vous que le chemin est correct
            Parent paymentView = loader.load();
            PaymentController paymentController = loader.getController();
            // Assurez-vous que totalPrix contient le total actuel du panier.
           // Ici, vous passez le total du panier au contrôleur de paiement
            paymentController.setData(totaux);
            Scene scene = new Scene(paymentView);
            Stage paymentStage = new Stage();
            paymentStage.setTitle("Paiement");
            paymentStage.setScene(scene);
            paymentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérez l'exception ici

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de chargement");
            alert.setContentText("Impossible de charger l'interface de paiement.");
            alert.showAndWait();
        }
    }

    public void setCurrUser( Utilisateur u) {
        currUser=u;
    }

    public Panier()  {
    }

    @FXML
    void retour(ActionEvent event) {
        try {
            // Charger la vue des oeuvres
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/UserInterface.fxml")); // Remplacez par le chemin correct vers votre vue OeuvresClient.fxml
            AnchorPane oeuvresView = loader.load();
           // loader.setLocation(getClass().getResource("/guiOeuvre/OeuvresClient.fxml")); // Assurez-vous que le chemin est correct
            //AnchorPane oeuvresView = loader.load();
          UserInterfaceController controller = loader.getController();
            controller.setUtilisateur(UserConnected.getUser());



            // Récupérer la scène actuelle et définir le nouveau contenu de la scène
            AnchorPane root = (AnchorPane) panierContainer.getScene().getRoot(); // Assurez-vous que la racine de votre scène est un VBox, sinon ajustez selon votre structure de vue
            root.getChildren().setAll(oeuvresView);

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur lors du chargement de la vue des oeuvres: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void getOeuvre(Oeuvre o){
        if (o != null) {
            panier.add(o);
            setPanier();
            totalachaat.setText(String.format("%.2f €", totaux));
            //updateTotalAchat();
        }else{
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("pas d'oeuvre selectionner!");
            a.show();
        }
    }
    public void updateTotalAchat(Oeuvre oeuvre) {
        panier.remove(oeuvre);
        totaux = 0; // Réinitialisez le total à zéro avant de recalculer

            for (Oeuvre o : panier) {
                totaux += o.getPrix(); // Ajoutez le prix de chaque œuvre au total
            }
        // Mettez à jour le label avec le nouveau total
            totalachaat.setText(String.format("%.2f €", totaux));


    }

   public void setPanier() {
        idpanierr.getChildren().clear();

        for (Oeuvre o : panier){

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/guiOeuvre/PanierClient.fxml"));
                VBox card = fxmlLoader.load();
                PanierClient panierClient = fxmlLoader.getController();
                panierClient.getData(o);
                totaux = totaux+o.getPrix();
                panierClient.setCurrUser(currUser);
                panierClient.getPanierController(this);
                //panierClient.updateQuantiteEtTotal();
                idpanierr.getChildren().add(card);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }}
       //updateTotalAchat();
        }


    void setTotalachaat(double total, Oeuvre oeuvre){
         // Initialisez une variable locale pour le total
        for (Oeuvre o : panier) {
            if (!o.equals(oeuvre))
                total += o.getPrix(); // Calculez le total
        }
        totaux = total; // Mettez à jour le total global
        totalachaat.setText(String.format("%.2f €", totaux));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        totaux = 0;
       // afficherPanier();
        panierClient.getPanierController(this);
    }

//    private void afficherPanier() {
//        List<Oeuvre> oeuvres = GestionPanier.getInstance().getPanier();
//        idpanierr.getChildren().clear();
//        for (Oeuvre oeuvre : oeuvres) {
//            Label label = new Label(oeuvre.getTitre() + " - Prix: " + oeuvre.getPrix());
//            idpanierr.getChildren().add(label);
//        }
//    }



}
