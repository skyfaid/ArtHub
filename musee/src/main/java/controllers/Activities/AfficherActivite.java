package controllers.Activities;

import entities.Participation;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import services.ServiceParticipation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class AfficherActivite {
    public VBox Ton;
    @FXML
    private TableView<Participation> participationTableView;
    @FXML
    private TableColumn<Participation, Integer> Id_participationColumn;
    @FXML
    private TableColumn<Participation, Integer> id_activiteColumn;
    @FXML
    private TableColumn<Participation, Integer> utilisateur_idColumn;
    @FXML
    private TableColumn<Participation, Integer> ScoreColumn;
    @FXML
    private TableColumn<Participation, Date> Participation_dateColumn;
    @FXML
    public VBox Test;
    @FXML
    private AnchorPane contentPane;
    // Reference to the service class
    private  ServiceParticipation serviceParticipation = new ServiceParticipation() {
    };

    @FXML
    protected void handleShowParticipations() {
        participationTableView.setVisible(true);
    }
/*
    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
*/
        @FXML
        private void initialize() {
            // Initialize your service class

            // Set up the columns to use the Participation property names
            Id_participationColumn.setCellValueFactory(new PropertyValueFactory<>("id_participation"));
            id_activiteColumn.setCellValueFactory(new PropertyValueFactory<>("id_activite"));
            utilisateur_idColumn.setCellValueFactory(new PropertyValueFactory<>("utilisateur_id"));
            ScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
            Participation_dateColumn.setCellValueFactory(new PropertyValueFactory<>("participation_date"));

            // Load and display the list of participations
            loadParticipationsData();
        }
/*
        private void loadParticipationsData() {
            try {
                ObservableList<Participation> participations = FXCollections.observableArrayList(serviceParticipation.recuperer());
                participationTableView.setItems(participations);
            } catch (SQLException e) {
                e.printStackTrace(); // Or handle the error as appropriate for your application
            }
        }
 */
public void handleTopScoresButtonClick(ActionEvent actionEvent) {
    try {
        List<Participation> top3Participations = serviceParticipation.recuperera(3);
        participationTableView.getItems().clear();
        participationTableView.getItems().addAll(top3Participations);
    } catch (SQLException e) {
        e.printStackTrace(); // Handle exception appropriately.
    }
}

    private void loadParticipationsData() {
        try {
            List<Participation> participations = serviceParticipation.recuperer(); // This method should return a list of all activities.
            participationTableView.setItems(FXCollections.observableArrayList(participations));
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately.
        }
    }

    public void handleResetButtonClick(ActionEvent actionEvent) {
    loadParticipationsData();
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
    Test.setVisible(true);
        Ton.setVisible(false);
        try {
            AnchorPane pane_act = FXMLLoader.load(getClass().getResource("/guiActivite/AjouterActivite.fxml"));
           Test.getChildren().add(pane_act);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void showView(VBox view) {
        contentPane.getChildren().forEach(child -> child.setVisible(false));
        view.setVisible(true);
    }


    // Other methods for your controller...

}
