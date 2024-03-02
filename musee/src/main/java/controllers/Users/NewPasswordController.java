package controllers.Users;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import services.ServiceUtilisateur;

import java.io.IOException;
import java.sql.SQLException;

public class NewPasswordController {

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    private Stage primaryStage;
    private String userEmail;

    private final ServiceUtilisateur userService = new ServiceUtilisateur();

    // Set the primary stage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Set the user email
    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    @FXML
    void setNewPassword(ActionEvent event) {
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (newPassword.equals(confirmPassword)) {
            try {
                // Update the password in the database
                userService.updatePasswordByEmail(userEmail, newPassword);

                // Display a confirmation message
                showAlert("Password Updated", "Your password has been updated successfully.", Alert.AlertType.INFORMATION);

                // Close the stage or navigate to another scene as needed
                // primaryStage.close();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Password Mismatch", "The entered passwords do not match. Please try again.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
