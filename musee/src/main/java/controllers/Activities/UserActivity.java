package controllers.Activities;

import entities.Activite;
import entities.Participation;
import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceActivite;
import services.ServiceParticipation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UserActivity{
    @FXML
    private TilePane tilePaneActivities;
    private ServiceActivite serviceActivite;
    @FXML
    private VBox topParticipantsContainer ;
    private  Utilisateur user;

    public UserActivity() {
        serviceActivite = new ServiceActivite();
        // Initialize the serviceActivite here or via dependency injection
    }

        public void setActivityUser(Utilisateur userNow){
        this.user=userNow;
}
    @FXML
    private void initialize() {
        List<Activite> activities = serviceActivite.recuperer();
        for (Activite activite : activities) {
            VBox activityCard = createActivityCard(activite);
            tilePaneActivities.getChildren().add(activityCard);
        }
    }

    private VBox createActivityCard(Activite activite) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("activity-card");
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(activite.getPosterUrl())));
        imageView.setFitHeight(150);
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(true);
        Label nameLabel = new Label("Name: " + activite.getNom_act());
        Label dateLabel = new Label("Date: " + activite.getDateDebut().toString());
        Label locationLabel = new Label("Location: " + activite.getLieu());
        Button participateButton = new Button("Participate");
        participateButton.setVisible(false); // Initially invisible
        StackPane stackPane = new StackPane(imageView, participateButton);
        stackPane.setAlignment(Pos.BOTTOM_CENTER);
        stackPane.setOnMouseEntered(e -> participateButton.setVisible(true));
        stackPane.setOnMouseExited(e -> participateButton.setVisible(false));
        participateButton.setOnAction(e -> handleParticipation(activite));
        card.getChildren().addAll(stackPane, nameLabel, dateLabel, locationLabel);
        return card;
    }
/*
    private void handleParticipation(Activite activite) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiActivite/WelcomeActivity.fxml"));
            Parent root = loader.load();
            WelcomeActivityController controller = loader.getController();
            controller.setActivityName(activite.getNom_act() );
            Stage stage = new Stage();
            stage.setTitle("Welcome");
            stage.setScene(new Scene(root, 600, 400)); // Set the size of the welcome window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
@FXML
private void handleParticipation() {
    // Check the value of nom_act
    if (nom_act.equals("Puzzle")) {
        // Load the Puzzle.fxml file
        loadPuzzleInterface();
    } else if (nom_act.equals("enigme")) {
        // Load the interface for enigme activity
        loadEnigmeInterface();
    } else {
        // Handle other activities or display an error message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Invalid activity: " + nom_act);
        alert.showAndWait();
    }
}
    private void loadEnigmeInterface() {
        // Implement loading of enigme interface
        // Replace this with your code to load the interface for the enigme activity
    }
    private void loadPuzzleInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Puzzle.fxml"));
            Parent puzzleRoot = loader.load();
            Stage puzzleStage = new Stage();
            puzzleStage.setTitle("Puzzle Game");
            puzzleStage.setScene(new Scene(puzzleRoot));
            puzzleStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Handle any exceptions gracefully
        }
    }
    public void setActivityDetails(Activite activite) throws SQLException {
        displayTopParticipants(activite);
    }

    private void displayTopParticipants(Activite activite) {
        // Ensure you have an instance of ServiceParticipation
        ServiceParticipation serviceParticipation = new ServiceParticipation();

        try {
            // Now call the method on the instance
            List<Participation> topParticipants = serviceParticipation.getTopParticipantsForActivity(activite.getId_activite(), 3);
            topParticipantsContainer.getChildren().clear(); // Clear previous content
            for (Participation participation : topParticipants) {
                Label participantLabel = new Label(participation.getUtilisateur_id() + " - Score: " + participation.getScore());
                topParticipantsContainer.getChildren().add(participantLabel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception, possibly show an error message to the user
        }
    }


}
