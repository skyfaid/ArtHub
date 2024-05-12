package controllers.Formations;

import entities.Formations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class UserForm {

    @FXML
    private Label DateDebutHcol;

    @FXML
    private Label DateFinHcol;

    @FXML
    private VBox HboxForm;

    @FXML
    private Label IdParticipantHCol;

    @FXML
    private Label LienHCol;

    @FXML
    private Label NbrParticpantHCol;
    @FXML
    void Participer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiFormation/AjoutParticipantUser.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Participation Form"); // Set the title of the new stage
            stage.setScene(new Scene(root)); // Set the scene for the stage with the loaded FXML
            //stage.initModality(Modality.APPLICATION_MODAL); // Set the window to be modal
            stage.initOwner(((Node) event.getSource()).getScene().getWindow()); // Set the owner window

            // Show the new stage
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void setFormation(Formations f)
    {
        Formations formations = f;

        LienHCol.setText(f.getLien());
        DateDebutHcol.setText(f.getDate_debut());
        DateFinHcol.setText(f.getDate_fin());
    }
    @FXML
    void Quizz(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiFormation/Quizz.fxml"));
            Parent root = loader.load();
            Stage quizStage = new Stage();
            quizStage.setTitle("Quiz"); // Set the title of the new stage
            quizStage.setScene(new Scene(root)); // Set the scene for the stage with the loaded FXML, width 400 and height 300

            // Optional: If you want to make this window modal (blocks input to other windows), uncomment the following lines
            // quizStage.initModality(Modality.APPLICATION_MODAL);
            // quizStage.initOwner(DateDebutHcol.getScene().getWindow());

            // Show the new stage
            quizStage.show();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
