package controllers.Users;

import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ServiceUtilisateur;

import java.io.IOException;
import java.sql.SQLException;

public class ActivateAccountController {
    @FXML
    private TextField alternateCodeField;

    private Stage primaryStage;
    private Utilisateur user;

    private final ServiceUtilisateur userService = new ServiceUtilisateur();

    // Set the primary stage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void setUser(Utilisateur u) {
        this.user=u;
    }
    @FXML
    void verifyAlternateCode(ActionEvent event) {
        String enteredAlternateCode = alternateCodeField.getText().trim();
        try {
            if (userService.verifyResetCode(user.getEmail(), enteredAlternateCode)) {
                userService.activateUserByEmail(user.getEmail());
                showAlert("Account Activated", "Your account is now activated.", Alert.AlertType.INFORMATION);
                closeStage();
            } else {
                showAlert("Invalid Alternate Code", "The entered alternate code is invalid.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void closeStage() {
        Stage stage = (Stage) alternateCodeField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
