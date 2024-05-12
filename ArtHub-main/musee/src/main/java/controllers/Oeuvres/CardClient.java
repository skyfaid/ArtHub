package controllers.Oeuvres;

import entities.Oeuvre;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import services.OeuvreService;

import java.io.IOException;

public class CardClient {

    @FXML
    private Label datecard;

    @FXML
    private Label descriptiobCard;

    @FXML
    private Label disponibilitecard;

    @FXML
    private ImageView imageCard;

    Oeuvre oeuvres = new Oeuvre();
    private OeuvreService oeuvreService = new OeuvreService();
    @FXML
    private Label prixcard;

    @FXML
    private Label titreCard;

    @FXML
    private Label typecard;
    private PanierClient panierClient = new PanierClient();
    private Utilisateur currUser;

    public void setCurrUser( Utilisateur u) {
        currUser=u;
    }
    @FXML
    void addCard(ActionEvent event) {
        try {
            //setData();

            FXMLLoader loader = new FXMLLoader((getClass().getResource("/guiOeuvre/DetaileOeuvre.fxml")));
            Parent root = loader.load();
            DetaileOeuvre detaileOeuvre = loader.getController();

// Passer les données à cette instance
            detaileOeuvre.getDataFromCard(oeuvres);
            detaileOeuvre.setCurrUser(currUser);

// Afficher la vue
            prixcard.getScene().setRoot(root);
            //prixcard.getScene().setRoot(root);
            //panierClient.getDataFromCard(oeuvres);
        } catch (IOException e) {
            throw new RuntimeException(e);
    }
    }



    public CardClient() {

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

    void setData(){
        oeuvres.setPrix(Double.valueOf(prixcard.getText()));
    }
    public ImageView getImageView() {
        return imageCard;
    }
}


