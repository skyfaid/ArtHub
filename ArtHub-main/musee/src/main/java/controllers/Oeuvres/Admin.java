package controllers.Oeuvres;

import entities.vente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import services.VenteService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class Admin implements Initializable {
    @FXML
    private VBox pane;
    @FXML
    private Label totaleprixvente;

    @FXML
    private Label totalvente;

    @FXML
    private Button deletevente;
    @FXML
    private TextField searchvente;
    private VenteService venteService;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        venteService = new VenteService();
        loadSales();
        afficherTotaux();
        searchvente.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                rechercheVente(newValue); // Appeler la méthode de recherche chaque fois que le texte change
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });


    }
    private void loadSales() {
        List<vente> sales ;
        try {
            sales = venteService.recupererToutesVentes();
            for (vente sale : sales) {
                addSaleToPane(sale);
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Could not load sales: " + e.getMessage());
            alert.showAndWait();
        }

    }

    private void addSaleToPane(vente sale) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiOeuvre/item.fxml"));

            Node saleItemBox = loader.load();  // Use Node here instead of VBox or HBox

            Item itemController = loader.getController();
            itemController.setData(sale, this);

            pane.getChildren().add(saleItemBox); // Add directly without casting
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error loading sale items: " + e.getMessage());
            alert.showAndWait();
        }
    }


    private void afficherTotaux() {
        try {
            double totalVentes = venteService.calculerTotalVentes();
            double totalPrixVentes = venteService.calculerTotalPrixVentes();

            // Mettre à jour l'interface utilisateur
            totalvente.setText(String.valueOf(totalVentes));
            totaleprixvente.setText(String.format("%.2f", totalPrixVentes));
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception ou afficher une alerte d'erreur
        }
    }
    public void supprimerVente(int venteId) {
        try {
            venteService.supprimerVente(venteId);
            // Recharger les ventes pour mettre à jour l'interface utilisateur
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Suppression réussie");
            alert.setHeaderText(null); // Vous pouvez mettre à null ou personnaliser le texte d'en-tête
            alert.setContentText("La vente a été supprimée avec succès.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error deleting sale: " + e.getMessage());
            alert.showAndWait();
        }
    }
    private void rechercheVente(String recherche) throws SQLException {
        try {
            int oeuvreId = recherche.isEmpty() ? -1 : Integer.parseInt(recherche);
            List<vente> ventesFiltrees;
            if (oeuvreId == -1) {
                ventesFiltrees = venteService.recupererToutesVentes();
            } else {
                ventesFiltrees = venteService.recupererVentesParOeuvreId(oeuvreId);
            }
            pane.getChildren().clear(); // Effacer les ventes actuellement affichées
            for (vente v : ventesFiltrees) {
                addSaleToPane(v);
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur lors de la recherche des ventes: " + e.getMessage());
            alert.showAndWait();
        }}
}
