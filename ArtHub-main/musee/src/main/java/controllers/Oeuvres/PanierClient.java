package controllers.Oeuvres;

import entities.Oeuvre;
import entities.Utilisateur;
import entities.vente;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.OeuvreService;
import services.VenteService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class PanierClient {
    @FXML
    private Button btnbuy;

    @FXML
    private Button btnmoins;

    @FXML
    private Button btnplus;

    @FXML
    private Label datecard = new Label();

    @FXML
    private Label descriptiobCard = new Label();

    @FXML
    private ImageView imageCard;

    @FXML
    private Label nbrquantite;

    @FXML
    private VBox panier;

    //@FXML
   // private Label quantite;
    //@FXML
    private int quantite;
    private Panier panierReference;

    @FXML
    private Label titreCard = new Label();
    @FXML
    private Label disponibilitecard = new Label();
    @FXML
    private Label prixcard = new Label();
    @FXML
    private Label typecard = new Label();
    @FXML
    private Label total;
    private static Panier panierController = new Panier();
    public void getPanierController(Panier p){
        if (p != null) {
            PanierClient.panierController = p;
        } else {
            // Vous pourriez vouloir logger cette situation ou lancer une exception
            System.out.println("Attention : l'instance de Panier fournie est nulle !");
        }
    }
    CardClient cardClient ;
    Oeuvre oeuvres = new Oeuvre();
    double totalPrix;
    private Utilisateur currUser;

    public void setCurrUser( Utilisateur u) {
        currUser=u;
    }

    @FXML
    void removebtn(ActionEvent event) {
        int idVenteActuelle = 0;
        refreshPanier();
        if (idVenteActuelle != -1) { // Vérifiez qu'une vente est bien sélectionnée
            try {
                VenteService venteService = new VenteService();
                venteService.supprimerVente(idVenteActuelle);
                System.out.println("Vente supprimée avec succès.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Suppression réussie");
                alert.setHeaderText(null);
                alert.setContentText("L'œuvre a été supprimée avec succès.");
                alert.showAndWait();
                panierController.updateTotalAchat(oeuvres);

                // Optionnel: Actualisez la liste des ventes ou l'interface utilisateur ici

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de la suppression de la vente.");
            }
        } else {
            System.out.println("Aucune vente sélectionnée.");
        }

    }


    public PanierClient() {
    }


    private OeuvreService oeuvreService = new OeuvreService();

    public void getData(Oeuvre o) {
        oeuvres = o;
        totalPrix = oeuvres.getPrix();
        descriptiobCard.setText(oeuvres.getDescription());
        titreCard.setText(oeuvres.getTitre());
        disponibilitecard.setText(oeuvres.getDisponibilite());
        quantite = 1;
        nbrquantite.setText(quantite+"");
        typecard.setText(oeuvres.getType());
        prixcard.setText(String.valueOf(oeuvres.getPrix()));
        datecard.setText(String.valueOf(oeuvres.getDateCreation()));
        Image img = new Image(new File(oeuvres.getPosterUrl()).toURI().toString());
        imageCard.setImage(img);
        Platform.runLater(() -> total.setText(String.format("Total: %.2f €", totalPrix)));
        //PanierClient.panierController.setTotalachaat(totalPrix);

//        Alert b = new Alert(Alert.AlertType.ERROR);
//        b.setContentText(titreCard.getText());
//        b.show();

    }
    void setData(){
        oeuvres.setPrix(Double.valueOf(prixcard.getText()));
    }





    @FXML
    void ajouterQuantite(ActionEvent event) {
        quantite++;
        totalPrix= totalPrix + oeuvres.getPrix();
        //nbrquantite.setText(String.valueOf(quantite));
        updateQuantiteEtTotal();

    }
    @FXML
    void retirerQuantite(ActionEvent event) {
        if (quantite > 0) { // Empêcher la quantité de devenir négative
            quantite--;
            totalPrix= totalPrix - oeuvres.getPrix();
           //nbrquantite.setText(String.valueOf(quantite));
            updateQuantiteEtTotal();
        }
    }
    public void updateQuantiteEtTotal() {
        System.out.println("Mise à jour du total et de la quantité"); // Diagnostic
        if (oeuvres != null) {
            //totalPrix = quantite * oeuvres.getPrix();
            System.out.println("Quantité: " + quantite + ", Prix Total: " + totalPrix); // Diagnostic
            Platform.runLater(() -> {
                nbrquantite.setText(String.valueOf(quantite)); // Affichez la nouvelle quantité
                total.setText(String.format("%.2f €", totalPrix)); // Affichez le nouveau total prix
            });
            PanierClient.panierController.setTotalachaat(totalPrix, oeuvres);
            if (panierController != null) {
                //panierController.setTotalachaat(totalPrix); // Cela mettra à jour le total dans le contrôleur Panier
            }
        } else {
            System.out.println("L'objet oeuvre est null."); // Diagnostic
        }}

    @FXML
    void acheterProduit(ActionEvent event) {


        // Assurez-vous que l'œuvre à acheter est correctement référencée. Ici, `oeuvres` est l'instance actuellement sélectionnée.
        if (oeuvres != null && quantite > 0) { // Vérifiez également que la quantité est positive.
            vente nouvelleVente = new vente();
            nouvelleVente.setID_OeuvreVendue(oeuvres.getId()); // Assurez-vous que `getId()` est la méthode correcte pour obtenir l'ID de l'œuvre.
            nouvelleVente.setDateVente(new java.sql.Date(System.currentTimeMillis())); // Utilisation de la date actuelle pour la vente.
            nouvelleVente.setPrixVente(oeuvres.getPrix() * quantite); // Calcul du total en fonction de la quantité.
            nouvelleVente.setModePaiement("Carte"); // Exemple de mode de paiement, cela pourrait venir d'une sélection de l'utilisateur.
            nouvelleVente.setQuantite(quantite); // Définir la quantité achetée.

            try {
                VenteService venteService = new VenteService();
                venteService.ajouterVente(nouvelleVente);
                System.out.println("Vente ajoutée avec succès.");

                // Afficher un message de confirmation à l'utilisateur
                Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION, "La vente a été ajoutée avec succès.");
                confirmationAlert.showAndWait();

                // Optionnel: Réinitialiser la vue ou actualiser la liste des ventes ici si nécessaire.
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de l'ajout de la vente.");

                // Afficher un message d'erreur à l'utilisateur
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'ajout de la vente.");
                errorAlert.showAndWait();
            }
        } else {
            System.out.println("Aucune œuvre sélectionnée pour l'achat ou quantité invalide.");

            // Afficher une alerte si aucune œuvre n'est sélectionnée ou si la quantité est invalide.
            Alert alert = new Alert(Alert.AlertType.WARNING, "Aucune œuvre sélectionnée pour l'achat ou quantité invalide.");
            alert.showAndWait();
        }
    }
    private void refreshPanier() {
        panier.getChildren().clear(); // Supprime tous les éléments du panier
        // Rechargez ou récupérez à nouveau la liste des œuvres dans le panier
        // Puis, pour chaque œuvre dans le panier, recréez la vue et ajoutez-la au panier

    }

}
