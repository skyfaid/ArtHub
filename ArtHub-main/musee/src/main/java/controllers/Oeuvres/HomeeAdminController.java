package controllers.Oeuvres;
import javafx.application.Platform;

import entities.Oeuvre;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.XYChart.Series;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import entities.vente;
import entities.Oeuvre;
import javafx.scene.layout.AnchorPane;
import services.VenteService;
import services.OeuvreService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class HomeeAdminController {
    @FXML
    private PieChart oeuvresTypeChart;


    @FXML
    private PieChart oeuvresdisponibilitechart;

    @FXML
    private PieChart oeuvresstockchart;
    @FXML
    private LineChart<String, Number> venteChart;



    @FXML
    private BarChart<String, Number> ventesMonthChart;


    public void initialize() {
        updateOeuvresTypeChart();
        updateVentesMonthChart();
        updateOeuvresStockChart();
        updateVentesDateChart();
    }

    private void updateOeuvresTypeChart() {
        Map<String, Integer> typeCount = new HashMap<>();
        OeuvreService oeuvreService = new OeuvreService();
        List<Oeuvre> oeuvres = oeuvreService.recupererOeuvres();
        for (Oeuvre o : oeuvres) {
            typeCount.merge(o.getType(), 1, Integer::sum);
        }
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        typeCount.forEach((type, count) -> pieChartData.add(new PieChart.Data(type, count)));
        Platform.runLater(() -> oeuvresTypeChart.setData(pieChartData));
    }

    private void updateVentesMonthChart() {
        // Assume your vente has a getMonth() method or similar; if not, you'll need to adjust.
        Map<String, Integer> monthCount = new HashMap<>();
        try {

            VenteService venteService = new VenteService();
            List<vente> ventes = venteService.recupererToutesVentes();
            for (vente v : ventes) {
                if (v.getDateVente() != null) {
                    String month = v.getDateVente().toLocalDate().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
                    monthCount.merge(month, 1, Integer::sum);
                }
            }

            Platform.runLater(() -> {
                // Crée une nouvelle série pour le graphique
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("Ventes par mois"); // Vous pouvez nommer la série comme vous voulez

                monthCount.forEach((month, count) -> {
                    XYChart.Data<String, Number> data = new XYChart.Data<>(month, count);
                    series.getData().add(data);
                });

                ventesMonthChart.getData().clear();
                ventesMonthChart.getData().add(series);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void updateOeuvresStockChart() {
        try {
            // Obtention de la liste des œuvres depuis le service
            OeuvreService oeuvreService = new OeuvreService();
            List<Oeuvre> oeuvres = oeuvreService.recupererOeuvres();

            // Calcul du nombre total d'œuvres
            int totalOeuvres = oeuvres.size();

            // Préparation des données pour le PieChart
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            pieChartData.add(new PieChart.Data("Total", totalOeuvres));

            // Mise à jour du PieChart dans le fil d'exécution de l'interface utilisateur
            Platform.runLater(() -> {
                oeuvresstockchart.setData(pieChartData);
                oeuvresstockchart.setTitle("Stock des Oeuvres"); // Vous pouvez définir un titre si nécessaire
            });
        } catch (Exception e) {
            e.printStackTrace();
            // Gestion des erreurs (optionnel)
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Impossible de charger les données");
                alert.setContentText("Une erreur est survenue lors du chargement des données des œuvres.");
                alert.showAndWait();
            });
        }}

    private void updateVentesDateChart() {
        // Format pour afficher les mois
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault());

        // Structure pour compter les ventes par mois
        Map<YearMonth, Integer> monthCount = new TreeMap<>();

        try {
            VenteService venteService = new VenteService();
            List<vente> ventes = venteService.recupererToutesVentes();

            // Compter les ventes pour chaque mois
            for (vente v : ventes) {
                if (v.getDateVente() != null) {
                    YearMonth month = YearMonth.from(v.getDateVente().toLocalDate());
                    monthCount.merge(month, 1, Integer::sum);
                }
            }


            // Préparation des données pour le LineChart
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Ventes par Mois"); // Changez ceci pour correspondre à vos données

// Supposons que vous ayez une Map<String, Integer> où la clé est le mois et la valeur est le nombre d'utilisateurs
            Map<String, Integer> userLastLoginData = new HashMap<>();
// Remplissez votre map avec les données réelles

// Convertissez vos données en séries pour le LineChart
            for (Map.Entry<String, Integer> entry : userLastLoginData.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            monthCount.forEach((month, count) -> {
                String monthLabel = formatter.format(month);
                series.getData().add(new XYChart.Data<>(monthLabel, count));
            });

            // Mise à jour du LineChart dans le fil d'exécution de l'interface utilisateur
            Platform.runLater(() -> {
                venteChart.setData(FXCollections.observableArrayList()); // Réinitialise complètement les données
                venteChart.getData().add(series); // Ajoute la nouvelle série
            });

        } catch (SQLException e) {
            e.printStackTrace();
            // Gestion des erreurs
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Impossible de charger les données des ventes");
                alert.setContentText("Une erreur est survenue lors du chargement des données des ventes.");
                alert.showAndWait();
            });
        }
}



    }





