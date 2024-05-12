package controllers.Oeuvres;

import entities.Oeuvre;
import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import services.OeuvreService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OeuvresClient implements Initializable {
   // @FXML
    //private VBox cardclient;
    @FXML
    private GridPane cardclient;


    @FXML
    private ImageView imageView;
    @FXML
    private TextField searchClient;
    private final OeuvreService oeuvreService = new OeuvreService();
    private  Utilisateur currUser;

    public void setCurrUser( Utilisateur u) {
        currUser=u;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getFromDb();
        searchClient.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerOeuvres(newValue);
        });


    }
    public void getFromDb() {
        filtrerOeuvres("");
        try {
            List<Oeuvre> oeuvreList = oeuvreService.recupererOeuvres();
            int rowCount = 0; // Compteur pour les lignes
            int columnCount = 0; // Compteur pour les colonnes
            for (Oeuvre oeuvre : oeuvreList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/guiOeuvre/CardClient.fxml"));
                Node card = fxmlLoader.load();
                CardClient cardController = fxmlLoader.getController();
                cardController.getData(oeuvre);
                cardController.setCurrUser(currUser);
                cardclient.add(card, columnCount, rowCount);
                columnCount++; // Incrémenter le compteur de colonne
                if (columnCount > 3 -1) { // NOMBRE_COLONNES est le nombre de colonnes que vous voulez avoir
                    columnCount = 0;
                    rowCount++; // Passer à la ligne suivante
                }
                // Display the image of the current Oeuvre
                displayOeuvreImage(oeuvre, cardController);

                // Add the Card to the container
                //cardclient.getChildren().add(card);
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error loading Oeuvres: " + e.getMessage());
            alert.show();
        }

    }

    private void displayOeuvreImage(Oeuvre oeuvre, CardClient cardController) {
        // Get the image URL from the Oeuvre
        String imageURL = oeuvre.getPosterUrl();
        if (imageURL != null && !imageURL.isEmpty()) {
            // Load and display the image
            Image image = new Image("file:" + imageURL); // Assuming imageURL is the file path
            cardController.getImageView().setImage(image);

        } else {
            // Clear the image view if no image is available
            cardController.getImageView().setImage(null);
        }
    }
    private void filtrerOeuvres(String recherche) {
        cardclient.getChildren().clear(); // Nettoyez le conteneur avant de le remplir à nouveau.

        try {
            List<Oeuvre> oeuvreList = oeuvreService.recupererOeuvres();
            // Cette ligne suppose que recupererOeuvres() renvoie toutes les œuvres disponibles.
            oeuvreList.stream()
                    .filter(oeuvre -> oeuvre.getTitre().toLowerCase().contains(recherche.toLowerCase()))
                    .forEach(oeuvre -> {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/guiOeuvre/CardClient.fxml"));
                            VBox card = fxmlLoader.load();
                            CardClient cardController = fxmlLoader.getController();
                            cardController.getData(oeuvre);
                            cardController.setCurrUser(currUser);

                            displayOeuvreImage(oeuvre, cardController);
                            cardclient.getChildren().add(card);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur lors du filtrage des oeuvres: " + e.getMessage());
            alert.show();
        }
    }

}
