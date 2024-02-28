package controllers.Articles;

import entities.Article;
import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import services.ServiceUtilisateur;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ArticleItemController implements Initializable {
    @FXML
    private Label description;

    @FXML
    private ImageView imageView;

    @FXML
    private Label nameprenom;

    @FXML
    private Label titre;
    private Article article;
    private final ServiceUtilisateur ser=new ServiceUtilisateur();
    public void setData(Article a){
        this.article = a;
        titre.setText(a.getTitre());
        description.setText(a.getContenu());
        try {
            Utilisateur util = ser.recupererById(a.getUtilisateurId());
            nameprenom.setText(util.getNom() + " " + util.getPrenom());

            if (util.getUrlImageProfil() != null && !util.getUrlImageProfil().isEmpty()) {
                // Vérifiez si l'image existe dans le dossier de ressources
                URL imgUrl = getClass().getResource(util.getUrlImageProfil());
                if (imgUrl != null) {
                    Image image = new Image(imgUrl.toString(), true); // Charge l'image en arrière-plan
                    imageView.setImage(image);
                } else {
                    // Définissez une image par défaut si le fichier n'existe pas
                    imageView.setImage(new Image(getClass().getResourceAsStream("/images/nopic.png")));
                }
            } else {
                imageView.setImage(new Image(getClass().getResourceAsStream("/images/nopic.png")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        double radius = imageView.getFitHeight() / 2;
        Circle clip = new Circle(radius, radius, radius);
        imageView.setClip(clip);

        // Optional: if you want the ImageView to be resized dynamically
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
    }
}
