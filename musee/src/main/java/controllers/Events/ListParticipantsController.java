package controllers.Events;

import entities.Participant;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceParticipant;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.util.List;
import java.net.URL;
import java.util.ResourceBundle;

public class ListParticipantsController implements Initializable {

    public Button searchButton;
    public Button deletepart;
    @FXML
    private VBox layoutparticipants;

    @FXML
    private TableView<Participant> participantsTableView;

    @FXML
    private TextField searchTextField;

    private final ServiceParticipant service = new ServiceParticipant();
    private final ObservableList<Participant> allParticipants = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateParticipants();
      //  setupSearchListener();
    }



    private void populateParticipants() {

        List<Participant> participants = service.afficher();
        allParticipants.addAll(participants);
        participantsTableView.setItems(allParticipants);
    }

  /*  private void setupSearchListener() {
        searchTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterParticipants(newValue);
            }
        });
    }*/
/*    private void filterParticipants(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            participantsTableView.setItems(allParticipants);
        } else {
            ObservableList<Participant> filteredParticipants = FXCollections.observableArrayList();
            for (Participant participant : allParticipants) {
                if (participant.getNom().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredParticipants.add(participant);
                }
            }
            participantsTableView.setItems(filteredParticipants);
        }
    }*/

  @FXML
  void deleteparticipant(ActionEvent event) {
      // Get the selected participant from the TableView
      Participant selectedParticipant = participantsTableView.getSelectionModel().getSelectedItem();

      if (selectedParticipant != null) {
          // Call the service method to delete the participant using the instance method
          service.supprimer(selectedParticipant);

          // Remove the participant from the allParticipants list
          allParticipants.remove(selectedParticipant);
          // Refresh the TableView
          participantsTableView.refresh();

          System.out.println("Participant deleted successfully");
      } else {
          // Inform the user to select a participant first
          System.out.println("Please select a participant to delete.");
      }
  }
    @FXML
    void editparticipant(ActionEvent event) {
        // Get the selected participant from the TableView
        Participant selectedParticipant = participantsTableView.getSelectionModel().getSelectedItem();

        if (selectedParticipant != null) {
            // Open the EditParticipant window and pass the selected participant
            openEditParticipantWindow(selectedParticipant);
        } else {
            showAlert("Edit Participant", "Please select a participant to edit.");
        }

    }
    private void openEditParticipantWindow(Participant participant) {
        try {
            // Load the FXML file for the EditParticipant window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditParticipant.fxml"));
            Parent root = loader.load();

            // Get the controller for the EditParticipant window
            EditParticipantController controller = loader.getController();
            // Pass the selected participant to the controller
            controller.setParticipant(participant);

            // Create a new stage (window) for the EditParticipant scene
            Stage stage = new Stage();
            stage.setTitle("Edit Participant");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while opening the edit window.");
        }
    }
    // Utility method to show an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
