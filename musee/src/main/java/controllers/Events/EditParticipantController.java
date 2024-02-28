package controllers.Events;

import controllers.Events.Notification;
import entities.Participant;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import services.ServiceParticipant;

public class EditParticipantController {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    private Participant participant;

    private final ServiceParticipant service = new ServiceParticipant();

    // Reference to the notification container
    @FXML
    private VBox notificationContainer;

    // Call this method when you want to edit a participant
    public void setParticipant(Participant participant) {
        this.participant = participant;

    }

    @FXML
    private void initialize() {
        // Configure text formatter to allow only letters in nom and prenom fields
        configureTextFormatter(txtNom);
        configureTextFormatter(txtPrenom);
    }

    private void configureTextFormatter(TextField textField) {
        // Regular expression to allow only letters and spaces
        String regex = "[a-zA-ZÀ-ÿ\\s]+";

        // Create text formatter with filter that allows only characters matching the regex
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches(regex)) {
                return change;
            } else {
                return null;
            }
        });

        // Set the text formatter to the text field
        textField.setTextFormatter(textFormatter);
    }

    @FXML
    private void saveParticipant() {

        // Call the service method to update the participant
        try {
            service.modifier(participant);

            // Show success notification
            Notification notification = new Notification("Participant updated successfully.", notificationContainer);
            notification.show();
        } catch (Exception e) {
            // Show error notification
            Notification notification = new Notification("Failed to update participant: " + e.getMessage(), notificationContainer);
            notification.show();
        }
    }
}
