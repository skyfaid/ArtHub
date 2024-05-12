package controllers.Users;

import entities.Article;
import entities.Evenement;
import entities.Participant;
import entities.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import services.ServiceArticle;
import services.ServiceEvenement;
import services.ServiceParticipant;
import services.ServiceUtilisateur;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomeAdminController {

    public LineChart eventParticipantLineChart;
    @FXML
    private BarChart<String, Integer> userActivityChart;

    @FXML
    private PieChart userRolesChart;
    @FXML
    private PieChart userGenderChart;


    @FXML
    private LineChart<String, Number> userLastConnectionChart;

    private ServiceUtilisateur serviceUtilisateur = new ServiceUtilisateur();

    private ServiceArticle serviceArticle = new ServiceArticle();
    @FXML
    private PieChart eventGenderChart;

    private final ServiceParticipant serviceParticipant = new ServiceParticipant();
    private final ServiceEvenement serviceEvenement = new ServiceEvenement();





    @FXML
    public void initialize() {
        loadParticipantGenderData();
        loadEventParticipantData();
        loadUserRolesData();
        loadUserRegistrationsData();
        loadUserLastConnectionData();
        loadUserGenderData();
    }
    private void loadUserGenderData() {
        try {
            List<Utilisateur> utilisateurs = serviceUtilisateur.recuperer();
            if (utilisateurs != null && !utilisateurs.isEmpty()) {
                // Initialize counts for known genders
                long maleCount = utilisateurs.stream()
                        .filter(user -> "Male".equals(user.getGender()))
                        .count();
                long femaleCount = utilisateurs.stream()
                        .filter(user -> "Female".equals(user.getGender()))
                        .count();
                // Assuming only 'Male' and 'Female' are valid, count others as 'Unknown'
                long unknownCount = utilisateurs.size() - (maleCount + femaleCount);
                // Prepare the data for the pie chart
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                        new PieChart.Data("Male: " + maleCount, maleCount),
                        new PieChart.Data("Female: " + femaleCount, femaleCount)
                );

                // Only add 'Unknown' segment if there are any
                if (unknownCount > 0) {
                    pieChartData.add(new PieChart.Data("Unknown: " + unknownCount, unknownCount));
                }

                userGenderChart.getData().clear(); // Clear existing data
                userGenderChart.setData(pieChartData);
                userGenderChart.setTitle("User Gender Distribution");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle errors appropriately
        }

    }

    private void loadUserRolesData() {
        try {
            List<Utilisateur> utilisateurs = serviceUtilisateur.recuperer();
            if (utilisateurs != null && !utilisateurs.isEmpty()) {
                // Grouping by role and counting
                Map<String, Long> roleCounts = utilisateurs.stream()
                        .collect(Collectors.groupingBy(Utilisateur::getRole, Collectors.counting()));

                ObservableList<PieChart.Data> pieChartData = roleCounts.entrySet().stream()
                        .map(entry -> new PieChart.Data(entry.getKey() + ": " + entry.getValue(), entry.getValue()))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));

                // Calculating total number of users
                long totalUsers = utilisateurs.size();
                userRolesChart.getData().clear(); // Clear existing data
                userRolesChart.setData(pieChartData);
                // Update the chart title to include the total number of users
                userRolesChart.setTitle("User Roles Distribution - Total Users: " + totalUsers);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUserRegistrationsData() {
        try {
            Map<String, Integer> registrations = serviceUtilisateur.getUserRegistrationsPerMonth();
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName("User Registrations");
            registrations.forEach((monthYear, count) -> series.getData().add(new XYChart.Data<>(monthYear, count)));
            userActivityChart.getData().clear();
            userActivityChart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadUserLastConnectionData() {
        try {
            Map<String, Long> lastConnections = serviceUtilisateur.getLastConnectionDays();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Days Since Last Login");
            lastConnections.forEach((user, days) -> series.getData().add(new XYChart.Data<>(user, days)));
            userLastConnectionChart.getData().clear();
            userLastConnectionChart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void loadParticipantGenderData() {
        try {
            List<Participant> participants = serviceParticipant.afficher(); // Fetch participants data
            if (participants != null && !participants.isEmpty()) {
                // Initialize counts for male, female, and unknown genders
                long maleCount = participants.stream()
                        .filter(participant -> "Male".equals(participant.getGender()))
                        .count();
                long femaleCount = participants.stream()
                        .filter(participant -> "Female".equals(participant.getGender()))
                        .count();
                long unknownCount = participants.size() - (maleCount + femaleCount);

                // Prepare the data for the pie chart
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                        new PieChart.Data("Male: " + maleCount, maleCount),
                        new PieChart.Data("Female: " + femaleCount, femaleCount)
                );

                // Only add 'Unknown' segment if there are any
                if (unknownCount > 0) {
                    pieChartData.add(new PieChart.Data("Unknown: " + unknownCount, unknownCount));
                }

                eventGenderChart.getData().clear(); // Clear existing data
                eventGenderChart.setData(pieChartData);
                eventGenderChart.setTitle("Participant Gender Distribution");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle errors appropriately
        }
    }

    private void loadEventParticipantData() {
        try {
            // Fetch events and their participant counts
            List<Evenement> events = serviceEvenement.afficherback();

            // Create a series to hold event names and participant counts
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Event Participants");

            // Add event names and participant counts to the series
            for (Evenement event : events) {
                series.getData().add(new XYChart.Data<>(event.getNom(), event.getNombreParticipants()));
            }

            // Clear existing data and add the new series to the line chart
            eventParticipantLineChart.getData().clear();
            eventParticipantLineChart.getData().add(series);
            eventParticipantLineChart.setTitle("Event Participants");
        } catch (Exception e) {
            e.printStackTrace();
            // Handle errors appropriately
        }
    }



}
