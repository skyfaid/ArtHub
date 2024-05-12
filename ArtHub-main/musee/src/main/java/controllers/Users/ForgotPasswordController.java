package controllers.Users;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ServiceUtilisateur;

import java.io.IOException;
import java.sql.SQLException;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    private final String twilioAccountSid = "ACe92c3d66e8ce233e9203acbe50492a5c";
    private final String twilioAuthToken = "00ef7cf39edd636803c62c698fcb19c2";
    private final String twilioPhoneNumber = "+12706551982";

    private final ServiceUtilisateur userService = new ServiceUtilisateur();

    @FXML
    void sendResetCode(ActionEvent event) {
        String email = emailField.getText();

        try {
            // Check if the email exists in the database
            if (!userService.estEmailUnique(email)) {
                // Retrieve the user's phone number from the database
                String phoneNumber = userService.getPhoneNumberByEmail(email);

                // Generate a reset code and send it to the user's phone
                String resetCode = generateResetCode();
                sendResetCodeBySMS(phoneNumber, resetCode);

                // Save the reset code in the database for verification
                openResetCodeScene(email);
                userService.updateResetCode(email, resetCode);

                // Display a confirmation message
                showAlert("Code Sent", "A reset code has been sent to your phone.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Email not found", "The provided email address does not exist.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void openResetCodeScene(String email) throws IOException {
        // Load the FXML file for the reset code scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiUtilisateur/CodeVerificationScene.fxml"));
        Parent root = loader.load();

        // Pass the email to the controller of the new scene
        CodeVerificationController codeVerificationController = loader.getController();
        codeVerificationController.setPrimaryStage((Stage) emailField.getScene().getWindow()); // Set the primary stage
        codeVerificationController.setEmail(email); // Set the email

        // Set the new scene on the stage
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }



    private String generateResetCode() {
        // Generate a random numeric code, you can use your own logic
        return String.format("%04d", (int) (Math.random() * 10000));
    }

    private void sendResetCodeBySMS(String phoneNumber, String resetCode) {
        Twilio.init(twilioAccountSid, twilioAuthToken);

        try {
            Message message = Message.creator(
                            new PhoneNumber(phoneNumber),
                            new PhoneNumber(twilioPhoneNumber),
                            "Your password reset code is: " + resetCode)
                    .create();

            System.out.println("SMS sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
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
