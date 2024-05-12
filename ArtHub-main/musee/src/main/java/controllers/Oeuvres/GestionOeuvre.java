package controllers.Oeuvres;

import entities.Oeuvre;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import services.OeuvreService;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class GestionOeuvre implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private Button btnSave;

    @FXML
    private DatePicker date;

    @FXML
    private TextField description;

    @FXML
    private TextField disponibilite;

    @FXML
    private TextField prix;

    @FXML
    private TextField titre;

    @FXML
    private TextField type;

    private String imageURL = "";

    private final OeuvreService oeuvreService = new OeuvreService();

    @FXML
    void createOeuvre(ActionEvent event) {
        try {
            String titreText = titre.getText();
            String descriptionText = description.getText();
            LocalDate dateCreation = date.getValue();
            String prixText = prix.getText();
            String disponibiliteText = disponibilite.getText();
            String typeText = type.getText().trim();

            if (!prixText.matches("\\d+\\.?\\d*")) {
                throw new NumberFormatException("Le prix doit être un nombre valide.");
            }
            double prixDouble = Double.parseDouble(prixText);

            Oeuvre oeuvre = new Oeuvre();
            oeuvre.setTitre(titreText);
            oeuvre.setDescription(descriptionText);
            oeuvre.setDisponibilite(disponibiliteText);
            oeuvre.setType(typeText);
            oeuvre.setDateCreation(dateCreation);
            oeuvre.setPrix(prixDouble);
            oeuvre.setPosterUrl(imageURL);

            oeuvreService.ajouterOeuvre(oeuvre);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Oeuvre ajoutée avec succès\nImage sélectionnée: " + imageURL);
            alert.showAndWait();

            // End the function after displaying the alert
            return;
        } catch (NumberFormatException | SQLException e) {
            // Display error alert if NumberFormatException or SQLException occurs
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setContentText("Erreur lors de l'ajout de l'oeuvre: " + e.getMessage());
            errorAlert.showAndWait();

            // End the function
            return;
        }
    }


    @FXML
    void deleteOeuvre(ActionEvent event) {
        // Implement deleteOeuvre functionality
    }

    @FXML
    void updateOeuvre(ActionEvent event) {
        // Implement updateOeuvre functionality
    }

    @FXML
    void chargerimage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imageURL = selectedFile.getAbsolutePath();
        }
    }
}
