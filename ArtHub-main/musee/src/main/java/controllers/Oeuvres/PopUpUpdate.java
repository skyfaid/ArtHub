package controllers.Oeuvres;

import entities.Oeuvre;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import services.OeuvreService;
import services.Refreshable;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PopUpUpdate implements Initializable {


    private Oeuvre oeuvres= new Oeuvre();
    private OeuvreService oeuvreService = new OeuvreService();
    @FXML
    private TextField modifprix;
    private Refreshable refreshable;

    @FXML
    void updatebtn(ActionEvent event) {
        oeuvres.setPrix(Double.valueOf(modifprix.getText()));
        try {
            oeuvreService.modifierOeuvre(oeuvres);
            if (refreshable != null) {
                refreshable.refreshData(); // Rafraîchir les données sur l'écran principal
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setRefreshable(Refreshable refreshable) {
        this.refreshable = refreshable;
    }

    void getData(Oeuvre o) {
        oeuvres = o;
        //modifprix.setText(oeuvres.getDisponibilite());
        modifprix.setText(String.valueOf(oeuvres.getPrix()));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
