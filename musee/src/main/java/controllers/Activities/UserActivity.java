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
import utils.MyDataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
private void handleParticipation(Activite activite) {
    int userId = user.getUtilisateurId();
    // Get the ID of the activity
    int activityId = activite.getId_activite();
    // Get the current date
    LocalDate participationDate = LocalDate.now();
    // Check the value of nom_act
   try {
       // Check if the user has already participated in the activity
       if (hasParticipated(userId, activityId)) {
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Warning");
           alert.setHeaderText(null);
           alert.setContentText("You have already participated in this activity.");
           alert.showAndWait();
           return; // Exit the method if the user has already participated
       }
       // You may need to adjust this SQL query based on your database schema
       if (activite.getNom_act().equals("Puzzle")) {
           String insertQuery = "INSERT INTO Participation (id_activite, utilisateur_id, score, participation_date) VALUES (?, ?, ?, ?)";
           // Assuming you have a method to execute SQL queries in your ServiceParticipation class
           ServiceParticipation.executeUpdate(insertQuery, activityId, userId, 0, participationDate);
           handlePuzzleCompletion(userId , activityId);
           // Load the Puzzle.fxml file
           loadPuzzleInterface();
       } else if (activite.getNom_act().equals("enigme")) {
           String insertQuery = "INSERT INTO Participation (id_activite, utilisateur_id, score, participation_date) VALUES (?, ?, ?, ?)";
           // Assuming you have a method to execute SQL queries in your ServiceParticipation class
           ServiceParticipation.executeUpdate(insertQuery, activityId, userId, 0, participationDate);
           // Load the interface for enigme activity
           loadEnigmeInterface();
       } else {
           // Handle other activities or display an error message
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Error");
           alert.setHeaderText(null);
           alert.setContentText("Invalid activity: " + activite.getNom_act());
           alert.showAndWait();
       }
   }catch (SQLException e)
   {e.printStackTrace();}
}


    private boolean hasParticipated(int userId, int activityId) throws SQLException {
        // Query to check if the user has participated in the activity
        String query = "SELECT COUNT(*) FROM Participation WHERE utilisateur_id = ? AND id_activite = ?";
        try (PreparedStatement preparedStatement = MyDataBase.getInstance().getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, activityId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // Return true if the user has participated, false otherwise
                }
            }
        }
        return false;
    }

    private void loadEnigmeInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiActivite/enigme.fxml"));
            Parent puzzleRoot = loader.load();
            Stage puzzleStage = new Stage();
            puzzleStage.setTitle("Enigme Game");
            puzzleStage.setScene(new Scene(puzzleRoot));
            puzzleStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Handle any exceptions gracefully
        }
    }
    private void loadPuzzleInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiActivite/Puzzle.fxml"));
            Parent puzzleRoot = loader.load();
            Stage puzzleStage = new Stage();
            puzzleStage.setTitle("Puzzle Game");
            puzzleStage.setScene(new Scene(puzzleRoot));
            puzzleStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Handle any exceptions gracefully
        }
    }
    private void handlePuzzleCompletion(int userId, int activityId) throws SQLException {
        // Update the user's score when puzzle is completed
       // String updateQuery = "UPDATE Participation SET score = 2500 WHERE utilisateur_id = ? AND id_activite = ?";
        // Assuming you have a method to execute SQL queries in your ServiceParticipation class
        ServiceParticipation.updateUserScoreInParticipation(userId, 2500);
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
