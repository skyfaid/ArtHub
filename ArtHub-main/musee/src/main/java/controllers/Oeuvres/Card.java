package controllers.Oeuvres;

import entities.Oeuvre;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import services.OeuvreService;
import services.Refreshable;

import java.io.IOException;
import java.sql.SQLException;

public class Card implements Refreshable {
    @FXML
    private TextField chercher;
    @FXML
    private Button updatebtn;
    @FXML
    private VBox loadModif;
    @FXML
    private Button deletebtn;
    @FXML
    private Label descriptiobCard;

    @FXML
    private Label disponibilitecard;

    @FXML
    private ImageView imageCard;

    @FXML
    private Label prixcard;

    @FXML
    private Label titreCard;

    @FXML
    private Label typecard;

    @FXML
    private Label typecard1;
    @FXML
    private Label datecard;


    Oeuvre oeuvres = new Oeuvre();
    private OeuvreService oeuvreService = new OeuvreService();

    public Card() {
    }
    @FXML
    void deleteOeuvre(){
        try {
            oeuvreService.supprimerOeuvre(oeuvres.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Oeuvre supprimée avec succès");
            alert.show();
            refreshData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateOeuvre() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/guiOeuvre/PopUp.fxml"));

            loadModif = loader.load();

            PopUpUpdate upc = loader.getController();
            upc.getData(oeuvres);
            upc.setRefreshable(this);
            ScaleTransition st = new ScaleTransition(Duration.millis(100), loadModif);
            st.setInterpolator(Interpolator.EASE_IN);
            st.setFromX(0);
            st.setFromY(0);
            st.setToX(1);
            st.setToY(1);
            Stage stage = new Stage();
            stage.setTitle("Update Post");
            Scene scene = new Scene(loadModif, 413, 190);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            scene.setFill(Color.TRANSPARENT);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void getData(Oeuvre o) {
        oeuvres = o;
        descriptiobCard.setText(oeuvres.getDescription());
        titreCard.setText(oeuvres.getTitre());
        disponibilitecard.setText(oeuvres.getDisponibilite());
        typecard.setText(oeuvres.getType());
        prixcard.setText(String.valueOf(oeuvres.getPrix()));
        datecard.setText(String.valueOf(oeuvres.getDateCreation()));
    }


    public ImageView getImageView() {
        return imageCard;
    }

    @Override
    public void refreshData() {
        prixcard.setText(String.valueOf(oeuvres.getPrix()));
        loadModif.getChildren().clear();
    }
}

