package controllers.Formations;

import entities.Participants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import services.FormationService;
import services.ParticipantService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AjouterParticipants {

    @FXML
    private TableColumn<Participants,String> DateInscri_Col;

    @FXML
    private DatePicker Date_Inscri_TF;

    @FXML
    private TableColumn<Participants,Integer> NbrFormationsCol;

    public DatePicker getDate_Inscri_TF() {
        return Date_Inscri_TF;
    }

    public TextField getNbrFormationsTF() {
        return NbrFormationsTF;
    }

    @FXML
    private TextField NbrFormationsTF;

    @FXML
    private TableView<Participants> ParticipantsTableView;

    @FXML
    private TableColumn<Participants,Integer> idCol;
    LocalDate selected_Inscri;

    ParticipantService ParticipantService=new ParticipantService();

    @FXML
    void Ajouter(ActionEvent event) {

        selected_Inscri=Date_Inscri_TF.getValue();

        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ParticipantService ps=new ParticipantService();
        String Date_Inscri=selected_Inscri.format(formatter);
        try {
            ps.ajouter(new Participants(Integer.parseInt(NbrFormationsTF.getText()),Date_Inscri));
           /* Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Error");
            a.setContentText("");
            a.showAndWait();*/
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Formation_Item.fxml"));
                Parent root = loader.load();
                NbrFormationsTF.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        catch (SQLException e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setContentText(e.getMessage());
            a.showAndWait();

        }


    }
    private void updateForm(Participants participants) {
        NbrFormationsTF.setText(String.valueOf(participants.getNbr_formations()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Date_Inscri_TF.setValue(LocalDate.parse(participants.getDate_inscription(),formatter));

    }
    @FXML
    void initialize(){

        ParticipantsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateForm(newSelection);
            }
        });
        try {
            List<Participants> participants=ParticipantService.recuperer();
            ObservableList<Participants> observableList= FXCollections.observableList(participants);
            ParticipantsTableView.setItems(observableList);
            idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
            DateInscri_Col.setCellValueFactory(new PropertyValueFactory<>("date_inscription"));
            NbrFormationsCol.setCellValueFactory(new PropertyValueFactory<>("nbr_formations"));
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    @FXML
    void Modifier(ActionEvent event) {
        Participants selectedPart = ParticipantsTableView.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            selectedPart.setNbr_formations(Integer.parseInt(NbrFormationsTF.getText()));
            selectedPart.setDate_inscription(String.valueOf(Date_Inscri_TF.getValue()));
            try {
                ParticipantService.modifier(selectedPart);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // Update the TableView
            ParticipantsTableView.refresh();

            // Update the database, if necessary
            ParticipantService p =new ParticipantService();
            try {
                p.modifier(selectedPart);
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

    }

    @FXML
    void supprimer(ActionEvent event) {
        Participants selectedParticipant= ParticipantsTableView.getSelectionModel().getSelectedItem();
        if (selectedParticipant != null) {
            // Remove from the observable list
            ParticipantsTableView.getItems().remove(selectedParticipant);

            // Remove from database (if applicable)
            FormationService formationService = new FormationService();
            try {
                formationService.supprimer(selectedParticipant.getId()); // Assuming there's a method to remove the formation
                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Formation has been successfully removed.");
                alert.showAndWait();
            } catch (SQLException e) {
                // Show error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while removing the formation: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a formation to remove.");
            alert.showAndWait();
        }


    }

}

