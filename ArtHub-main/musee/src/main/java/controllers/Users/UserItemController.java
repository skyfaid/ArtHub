package controllers.Users;

import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import services.ServiceUtilisateur;
import utils.UserConnected;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class UserItemController implements Initializable {
    @FXML
    private Label datesingup;

    @FXML
    private Label emailuser;

    @FXML
    private ImageView imageuser;

    @FXML
    private Label lastlogin;

    @FXML
    private Label nameuser;

    @FXML
    private Label pseudouser;
    @FXML
    private Button blockButton;
    @FXML
    private Button mailbutton;
    private Utilisateur utilisateur;

    public void setData(Utilisateur u){
        this.utilisateur = u;
        String imagePath = (u.getUrlImageProfil() != null && !u.getUrlImageProfil().isEmpty()) ?
                u.getUrlImageProfil() : "/images/nopic.png";
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imageuser.setImage(image);
        nameuser.setText(u.getNom()+" "+u.getPrenom());
        emailuser.setText(u.getEmail());
        pseudouser.setText(u.getPseudo());
        if(u.getDerniereConnexion() != null) {
            lastlogin.setText(new SimpleDateFormat("yyyy-MM-dd").format(u.getDerniereConnexion()));
        } else {
            lastlogin.setText("N/A"); // Or any other placeholder text
        }
        datesingup.setText(new SimpleDateFormat("yyyy-MM-dd").format(u.getDateInscription()));
        int usr=UserConnected.getUser().getUtilisateurId();
        blockButton.setVisible( usr != u.getUtilisateurId());
        updateBlockButtonState(u.isEstActif());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Circle clip = new Circle(25, 25, 25); // Assuming the ImageView is 50x50
        imageuser.setClip(clip);

    }

    public void blocker(ActionEvent actionEvent) {
        try {
            boolean nouvelEtat = !this.utilisateur.isEstActif();
            new ServiceUtilisateur().bloquerDebloquerUtilisateur(this.utilisateur.getUtilisateurId(), nouvelEtat);
            this.utilisateur.setEstActif(nouvelEtat); // Mettre à jour l'état dans l'instance actuelle
            updateBlockButtonState(nouvelEtat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateBlockButtonState(boolean estActif) {
        String imagePath;
        if(estActif) {
            // Utilisateur est actif, montrer l'icône de blocage
            imagePath = "/images/lock-16.png";
        } else {
            // Utilisateur est bloqué, montrer l'icône de déblocage
            imagePath = "/images/unlock-16.png";
        }
        Image buttonImage = new Image(getClass().getResourceAsStream(imagePath));
        blockButton.setGraphic(new ImageView(buttonImage));
    }


    public void sendMail(ActionEvent actionEvent) {
    }
}
