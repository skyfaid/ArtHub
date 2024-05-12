package controllers.Users;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ServiceUtilisateur;

import java.io.IOException;
import java.sql.SQLException;

public class CodeVerificationController {

    @FXML
    private TextField codeField;

    private Stage primaryStage;
    private String userEmail;

    private final ServiceUtilisateur userService = new ServiceUtilisateur();

    // Set the primary stage
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Set the email
    public void setEmail(String email) {
        this.userEmail = email;
    }

    @FXML
    void verifyCode(ActionEvent event) {
        String enteredCode = codeField.getText().trim(); // Get the entered code
        System.out.println(enteredCode);
        System.out.println(userEmail);
        try {
            // Verify the code against the database
            if (userService.verifyResetCode(userEmail, enteredCode)) {
                // If code is verified successfully, switch to the new password scene
                switchToNewPasswordScene();
            } else {
                showAlert("Invalid Code", "The entered code is invalid.", Alert.AlertType.ERROR);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void switchToNewPasswordScene() throws IOException {
        // Load the NewPasswordScene FXML
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/guiUtilisateur/NewPasswordScene.fxml"));
        javafx.scene.Parent newPasswordParent = loader.load();

        // Set the controller for NewPasswordScene
        NewPasswordController newPasswordController = loader.getController();
        newPasswordController.setPrimaryStage(primaryStage);

        // Pass the user email to the controller of the new scene
        newPasswordController.setUserEmail(userEmail);

        // Set the scene on the primary stage
        javafx.scene.Scene newPasswordScene = new javafx.scene.Scene(newPasswordParent);
        primaryStage.setScene(newPasswordScene);
        primaryStage.show();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
