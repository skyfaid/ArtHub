package controllers.Formations;

import entities.Participants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ParticipantService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AjouterParticipantsUser {

    @FXML
    private TableColumn<?, ?> DateInscri_Col;

    @FXML
    private DatePicker Date_Inscri_TF;

    @FXML
    private TableColumn<?, ?> NbrFormationsCol;

    @FXML
    private TextField NbrFormationsTF;

    @FXML
    private TableView<?> ParticipantsTableView;

    @FXML
    private TableColumn<?, ?> idCol;
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
                Stage currentStage = (Stage) Date_Inscri_TF.getScene().getWindow();
                currentStage.close();
              /*  try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiFormation/userFormDisplay.fxml"));
                    Parent root = loader.load();
                    Date_Inscri_TF.getScene().setRoot(root);
                } catch (IOException e) {
                    throw new RuntimeException();
                }*/
            }
            catch (SQLException e){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setContentText(e.getMessage());
                a.showAndWait();

            }
    }

    @FXML
    void Modifier(ActionEvent event) {

    }

    @FXML
    void supprimer(ActionEvent event) {

    }

}
