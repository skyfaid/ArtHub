package controllers.Formations;

import entities.Formations;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import services.FormationService;

import java.sql.SQLException;
import java.util.List;

public class UserFormDisplay {

    @FXML
    private VBox Vboxuser;
    Formations formations=new Formations();
    FormationService fs=new FormationService();
    public void initialize() {
        // Let's assume Formation is a class that holds the details of each formation
        try {
            List<Formations> formations = fs.recuperer(); // Implement this method to get data

            for (Formations formation : formations) {
                try {
                    // Load the HBox from FXML
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/guiFormation/userForm.fxml"));
                    // Now, if you need to set data specific to each formation, you can access the HBox's children
                    // For example, to set the label text
                    VBox hbox = loader.load();
                    UserForm Hf = loader.getController();

                    Hf.setFormation(formation);
//                        ((Label)hbox.lookup("#IdParticipantHCol")).setText(String.valueOf(formation.getId())); // Replace 'labelId' with actual FXML ID
//                        ((Label)hbox.lookup("#NbrParticpantHCol")).setText(String.valueOf(formation.getNbr_participants()));
//                        ((Label)hbox.lookup("#LienHCol")).setText(formation.getLien());
//                        ((Label)hbox.lookup("#DateDebutHcol")) .setText(formation.getDate_debut());// Add the configured HBox to the VBox
//                        ((Label)hbox.lookup("#DateFinHcol")) .setText(formation.getDate_fin());
                    Vboxuser.getChildren().add(hbox);


                } catch (Exception e) {
                    e.printStackTrace(); // Handle exception as needed
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }}