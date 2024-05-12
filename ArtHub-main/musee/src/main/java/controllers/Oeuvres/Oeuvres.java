package controllers.Oeuvres;

import entities.Oeuvre;
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

public class Oeuvres implements Initializable {

    public ImageView imageView;
    @FXML
    private GridPane oeuverContainer;


    @FXML
    private TextField chercher;

    private final OeuvreService  oeuvreService = new OeuvreService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getFromDb();
        chercher.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerOeuvres(newValue);
        });

    }

    public void getFromDb() {
        try {
            List<Oeuvre> oeuvreList = oeuvreService.recupererOeuvres();
            int rowCount = 0; // Compteur pour les lignes
            int columnCount = 0; // Compteur pour les colonnes
            for (Oeuvre oeuvre : oeuvreList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/guiOeuvre/Card.fxml"));
                Node card = fxmlLoader.load();
                Card cardController = fxmlLoader.getController();
                cardController.getData(oeuvre);
                oeuverContainer.add(card, columnCount, rowCount);

                columnCount++; // Incrémenter le compteur de colonne
                if (columnCount > 3 - 1) { // NOMBRE_COLONNES est le nombre de colonnes que vous voulez avoir
                    columnCount = 0;
                    rowCount++; // Passer à la ligne suivante
                }
                // Display the image of the current Oeuvre
                displayOeuvreImage(oeuvre, cardController);

                // Add the Card to the container
                //oeuverContainer.getChildren().add(card);
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error loading Oeuvres: " + e.getMessage());
            alert.show();
        }
    }
    private void filtrerOeuvres(String recherche) {
        oeuverContainer.getChildren().clear(); // Nettoyez le conteneur avant de le remplir à nouveau.

        try {
            List<Oeuvre> oeuvreList = oeuvreService.recupererOeuvres();
            // Cette ligne suppose que recupererOeuvres() renvoie toutes les œuvres disponibles.
            oeuvreList.stream()
                    .filter(oeuvre -> oeuvre.getTitre().toLowerCase().contains(recherche.toLowerCase()))
                    .forEach(oeuvre -> {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/guiOeuvre/Card.fxml"));
                            VBox card = fxmlLoader.load();
                            Card cardController = fxmlLoader.getController();
                            cardController.getData(oeuvre);

                            displayOeuvreImage(oeuvre, cardController);
                            oeuverContainer.getChildren().add(card);
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

    // Method to display the image of an Oeuvre in the Card
    private void displayOeuvreImage(Oeuvre oeuvre, Card cardController) {
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


}
