package controllers.Formations;

import controllers.AdminInterfaceController;
import entities.Formations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.FormationService;
import utils.UserConnected;

import java.io.IOException;
import java.sql.SQLException;

public class HboxForm {
    @FXML

    private Label DateDebutHcol;

    @FXML
    private Label DateFinHcol;

    @FXML
    private VBox FormationHbox;

    @FXML
    private Label IdParticipantHCol;

    @FXML
    private Label LienHCol;

    @FXML
    private Label NbrParticpantHCol;
    Formations formations = new Formations();

    FormationService fs=new FormationService();
    AjouterFormation af = new AjouterFormation();
    private static FormationHbox formationHbox;

    public void setFormationHbox(FormationHbox fh) {
        HboxForm.formationHbox = fh;
    }
    public void setFormation(Formations f)
    {
        formations = f;
        IdParticipantHCol.setText(String.valueOf(f.getId()));
        NbrParticpantHCol.setText(String.valueOf(f.getNbr_participants()));
        LienHCol.setText(f.getLien());
        DateDebutHcol.setText(f.getDate_debut());
        DateFinHcol.setText(f.getDate_fin());
    }
    @FXML
    void AjouterFormation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiFormations/AjoutParticipant.fxml"));
            Parent root = loader.load();
            DateDebutHcol.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    @FXML
    void ModifierFormation(ActionEvent event) {
      /*  Formations formationsPrime = new Formations();
        setFormation(af.Modifier(event));*/
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiFormation/AjouterFormation.fxml"));
            Parent root = loader.load(); // Load the FXML content

            // Create a new stage for the popup
            Stage stage = new Stage();
            stage.setTitle("Modifier Formation"); // Set the title of the new stage
            stage.setScene(new Scene(root)); // Set the scene for the stage with the loaded FXML
            stage.initModality(Modality.APPLICATION_MODAL); // Set the window to be modal
            stage.initOwner(DateDebutHcol.getScene().getWindow()); // Set the owner window

            // Show the new stage and wait for it to be closed
            stage.showAndWait();



            //DateDebutHcol.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }



    @FXML
    void SupprimerFormation(ActionEvent event) {
        try {
            fs.supprimer(formations.getId());
            try {
                formationHbox.getall();
            } catch (Exception e) {
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
        }
    }
}
