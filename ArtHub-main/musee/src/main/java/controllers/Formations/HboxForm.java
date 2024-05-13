package controllers.Formations;

import entities.Formations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import services.FormationService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FormationHbox {
    public VBox ajouter;
    public VBox afficher;
    @FXML
    private ImageView imageView;

    @FXML
    private VBox ListeVbox;
    Formations formations=new Formations();
    FormationService fs=new FormationService();
    HboxForm Hf=new HboxForm();
    public void getall(){
        if (!ListeVbox.getChildren().isEmpty()) {
            ListeVbox.getChildren().remove(0, ListeVbox.getChildren().size());
        }
        try {

            List<Formations> formations = fs.recuperer(); // Implement this method to get data

            for (Formations formation : formations) {
                try {
                    // Load the HBox from FXML
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/guiFormation/HboxForm.fxml"));
                    // Now, if you need to set data specific to each formation, you can access the HBox's children
                    // For example, to set the label text
                    VBox hbox = loader.load();
                    HboxForm Hf = loader.getController();

                    Hf.setFormation(formation);
                    ListeVbox.getChildren().add(hbox);


                } catch (Exception e) {
                    e.printStackTrace(); // Handle exception as needed
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void initialize() {
        afficher.setVisible(true);
        ajouter.setVisible(true);
        // Let's assume Formation is a class that holds the details of each formation
       getall();
       Hf.setFormationHbox(this);

    }

    @FXML
    void add(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiFormation/AjouterFormation.fxml"));
            AnchorPane root = loader.load();
            afficher.getChildren().clear();
            ajouter.getChildren().add(root);
            afficher.setVisible(false);
            ajouter.setVisible(true);

            //ListeVbox.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @FXML
    void particpant(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiFormation/AjoutParticipant.fxml"));
            AnchorPane root = loader.load();
            afficher.getChildren().clear();
            ajouter.getChildren().add(root);
            afficher.setVisible(false);
            ajouter.setVisible(true);

            //ListeVbox.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }



}
