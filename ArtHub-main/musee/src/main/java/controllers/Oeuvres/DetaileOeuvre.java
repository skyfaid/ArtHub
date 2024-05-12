package controllers.Oeuvres;

import entities.Oeuvre;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class DetaileOeuvre {
    @FXML
    private Button btnmoins;

    @FXML
    private Button btnplus;

    @FXML
    private Label datecard;

    @FXML
    private Label descriptiobCard;

    @FXML
    private Label disponibilitecard;

    @FXML
    private ImageView imageCard;

    @FXML
    private Label nbrquantite;

    @FXML
    private Label prixcard;

    @FXML
    private Label titreCard;

    @FXML
    private Label typecard;
    private Oeuvre oeuvre=new Oeuvre();
    private int quantite = 0;
    //private final VenteService venteService = new VenteService();
    Panier panier = new Panier();
    private Utilisateur currUser;

    public void setCurrUser( Utilisateur u) {
        currUser=u;
    }

    @FXML
    void addarticle(ActionEvent event) {
        try {
            // Charger la vue du panier
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiOeuvre/Panier.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur du panier
            Panier panierController = loader.getController();

            // Ajouter l'œuvre au panier en utilisant une méthode du contrôleur
            panierController.getOeuvre(oeuvre);
            panierController.setCurrUser(currUser);
           // GestionPanier.getInstance().ajouterOeuvre(oeuvre);
            // Naviguer vers la vue du panier
            typecard.getScene().setRoot(root);
//panierController.panier.add(oeuvre);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ajout de l'œuvre au panier : " + e.getMessage());
        }
    }


    public DetaileOeuvre() {
    }

    public void getDataFromCard(Oeuvre o) {
        oeuvre = o;
        descriptiobCard.setText(oeuvre.getDescription());
        titreCard.setText(oeuvre.getTitre());
        disponibilitecard.setText(oeuvre.getDisponibilite());
        typecard.setText(oeuvre.getType());
        prixcard.setText(String.valueOf(oeuvre.getPrix()));
        datecard.setText(String.valueOf(oeuvre.getDateCreation()));
        Image img = new Image(new File(oeuvre.getPosterUrl()).toURI().toString());
        imageCard.setImage(img);
        //Platform.runLater(() -> total.setText(String.format("Total: %.2f €", totalPrix)));

//        Alert b = new Alert(Alert.AlertType.ERROR);
//        b.setContentText(titreCard.getText());
//        b.show();

    }
    public void setOeuvre(Oeuvre oeuvre) {
        this.oeuvre = oeuvre;
        // Mettre à jour l'interface utilisateur avec les détails de l'œuvre
    }
}
