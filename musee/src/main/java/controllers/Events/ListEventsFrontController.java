package controllers.Events;

import entities.Evenement;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import services.ServiceEvenement;
import services.ServiceParticipant;
import utils.SessionManager;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;



public class ListEventsFrontController {
    @FXML
    public TilePane eventsTilePane;
    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private final IntegerProperty participantCount = new SimpleIntegerProperty();
    private final ServiceParticipant serviceParticipant = new ServiceParticipant(); // Ensure you have this instance
    public Button showMapViewButton;
    public Button showcalendar;
    @FXML
    private VBox notificationContainer;
    @FXML
    private VBox eventDetailsVBox;
    private Notification notification;

    @FXML
    void initialize() {
        afficherEvenements();
        // Bind the participant count label to the property
        participantCount.addListener((obs, oldValue, newValue) -> {
            // Update participant count directly
            participantCount.set(newValue.intValue());
        });

    }
    private void filterEventCards(String searchText) {
        List<Evenement> filteredEvents = serviceEvenement.afficherfront().stream()
                .filter(e -> e.getNom().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
        refreshEventList(filteredEvents);
    }
    private void afficherEvenements() {
        List<Evenement> evenements = serviceEvenement.afficherfront();
        refreshEventList(evenements);
    }
    public void refreshEventList(List<Evenement> events) {
            eventsTilePane.getChildren().clear(); // Clear existing content
            for (Evenement evenement : events) {
                VBox card = createEventCard(evenement); // Rebuild each card
                eventsTilePane.getChildren().add(card);
            }
    }
    private Image getImageForEvent(Evenement evenement) {
        String imageURL = evenement.getPosterUrl();
        if (imageURL != null && !imageURL.isEmpty()) {
            try {
                return new Image(new File(imageURL).toURI().toURL().toString(), false);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
    /*private Media getVideoForEvent(Evenement evenement) {
        String trailerURL = evenement.getVideoUrl();
        if (trailerURL != null && !trailerURL.isEmpty()) {
            try {
                File videoFile = new File(trailerURL);
                return new Media(videoFile.toURI().toURL().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }*/

    private void applyIdleAnimation(VBox card) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1.5), card);
        translateTransition.setByY(-8); // Adjust the distance for the animation
        //  translateTransition.setByY(10); // Adjust the distance for the animation
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.setAutoReverse(true);
        translateTransition.play();
    }

    private VBox createEventCard(Evenement evenement) {
        VBox card = new VBox();
        card.getStyleClass().add("card");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(150);
        imageView.setFitWidth(250);
        Image eventImage = getImageForEvent(evenement);
        imageView.setImage(eventImage);
        imageView.getStyleClass().add("image-view");

        Label nameLabel = new Label("Nom: " + evenement.getNom());
        nameLabel.getStyleClass().add("label");

        Label dateLabel = new Label("Date: " + evenement.getDatedebut().toString());
        dateLabel.getStyleClass().add("label");

        Label locationLabel = new Label("Lieu: " + evenement.getLieu());
        locationLabel.getStyleClass().add("label");

        Label participantsLabel = new Label("Participants: " + serviceEvenement.getParticipantCount(evenement.getId()));
        participantsLabel.getStyleClass().add("label");

        // Countdown timer label
        Label countdownLabel = new Label();
        countdownLabel.getStyleClass().add("countdown-label"); // Ensure this class is defined in your CSS

        // Create the HBox for participate and voir trailer buttons
        HBox participateTrailerBox = new HBox(10);
        participateTrailerBox.setAlignment(Pos.CENTER);

        // Participate Button
        Button participateButton = new Button("Participate");
        participateButton.getStyleClass().add("participate-button");
        participateButton.setOnAction(actionEvent -> handleParticipateButtonAction(evenement, participateButton, participantsLabel));

        // Voir Trailer Button with play icon
        Button trailerButton = new Button("Voir Trailer");
        trailerButton.getStyleClass().add("trailer-button");
        ImageView playIcon = new ImageView(new Image("/images/playicon.png"));
        playIcon.setFitWidth(20);
        playIcon.setFitHeight(20);
        trailerButton.setGraphic(playIcon);
        trailerButton.setOnAction(event -> playVideo(evenement.getVideoUrl()));
        trailerButton.setStyle("-fx-font-weight: bold;");
        trailerButton.setOnMouseEntered(event -> trailerButton.setStyle("-fx-font-weight: normal;"));
        trailerButton.setOnMouseExited(event -> trailerButton.setStyle("-fx-font-weight: bold;"));
        participateTrailerBox.getChildren().addAll(participateButton, trailerButton);

        // Create a new HBox for the Show Location button
        HBox locationBox = new HBox();
        locationBox.setAlignment(Pos.CENTER);
        Button locationButton = new Button("Show Location");
        locationButton.getStyleClass().add("location-button");
        System.out.println("Geocoding address: " + evenement.getLieu());

        locationButton.setOnAction(event -> showMap(evenement.getLieu()));
        locationBox.getChildren().add(locationButton);

        // Dynamic countdown timer
        setupCountdownTimer(evenement, countdownLabel);
        applyIdleAnimation(card);
        // Add all components to the VBox
        card.getChildren().addAll(imageView, nameLabel, dateLabel, locationLabel, participantsLabel, countdownLabel, participateTrailerBox, locationBox);
        return card;
    }
    private void showMap(String location) {
        Stage mapStage = new Stage();
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Load the Google Maps page with the specified location
        webEngine.load("https://www.google.com/maps/place/" + location);

        // Set up the stage and scene
        Scene scene = new Scene(webView, 1200, 800);
        mapStage.setTitle("Event Location");
        mapStage.setScene(scene);
        mapStage.show();
    }

    private void setupCountdownTimer(Evenement evenement, Label countdownLabel) {
      final Timeline timeline = new Timeline(
              new KeyFrame(
                      javafx.util.Duration.seconds(0),
                      actionEvent -> {
                          // Convert LocalDate to LocalDateTime by assuming the event starts at the beginning of the day
                          LocalDateTime startDateTime = LocalDateTime.of(evenement.getDatedebut(), LocalTime.MIDNIGHT);
                          // Calculate duration between now and the start of the event
                          java.time.Duration duration = java.time.Duration.between(LocalDateTime.now(), startDateTime);
                          if (!duration.isNegative()) {
                              long seconds = duration.getSeconds();
                              String time = String.format("%dd %dh %dm %ds left",
                                      seconds / 86400,
                                      (seconds % 86400) / 3600,
                                      (seconds % 3600) / 60,
                                      (seconds % 60));
                              countdownLabel.setText(time);
                              countdownLabel.setStyle(""); // Reset to default style if it's not the event time yet
                          } else {
                              countdownLabel.setText("Event is happening now!");
                              countdownLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red; -fx-font-weight: bold;");
                              // Apply a pulsing effect
                              FadeTransition fadeTransition = new FadeTransition(javafx.util.Duration.seconds(1), countdownLabel);
                              fadeTransition.setFromValue(1.0);
                              fadeTransition.setToValue(0.3);
                              fadeTransition.setCycleCount(Timeline.INDEFINITE);
                              fadeTransition.setAutoReverse(true);
                              fadeTransition.play();
                          }
                      }
              ),
              new KeyFrame(javafx.util.Duration.seconds(1))
      );
      timeline.setCycleCount(Animation.INDEFINITE);
      timeline.play();
  }
    private void playVideo(String videoUrl) {
        Platform.runLater(() -> {
            try {
                File videoFile = new File(videoUrl);
                Media media = new Media(videoFile.toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);
                MediaView mediaView = new MediaView(mediaPlayer);
                StackPane root = new StackPane();
                root.getChildren().add(mediaView);
                Scene scene = new Scene(root, 1200, 800);
                Stage videoStage = new Stage();
                videoStage.setScene(scene);
                videoStage.setTitle("Event Trailer");

                // Add event handler to close the stage when media playback ends
                mediaPlayer.setOnEndOfMedia(() -> {
                    mediaPlayer.stop();
                    mediaPlayer.dispose(); // Release resources
                    videoStage.close();
                });

                videoStage.show();
            } catch (Exception e) {
                e.printStackTrace(); // Or handle the error appropriately
            }
        });
    }

   private void handleParticipateButtonAction(Evenement evenement, Button participateButton, Label participantsLabel) {
       // Disable the button immediately to prevent multiple clicks
       participateButton.setDisable(true);

       int currentUserId = SessionManager.getInstance().getCurrentUserId(); // Assuming this gets the current user's ID correctly

       // Check if the user has already participated
       boolean alreadyParticipated = serviceParticipant.hasUserParticipated(evenement.getId(), currentUserId);

       if (alreadyParticipated) {
           Platform.runLater(() -> {
               participateButton.setText("Participated");
               participateButton.setDisable(true);

               // Show notification
               Notification notification = new Notification("You have already participated in this event!", notificationContainer);
               notification.setTranslateX(notificationContainer.getWidth()); // Start off-screen
               notification.setTranslateY(notificationContainer.getHeight() - notification.getHeight()); // Bottom right corner
               notificationContainer.getChildren().add(notification);

               // Slide in animation
               TranslateTransition slideIn = new TranslateTransition(Duration.seconds(1), notification);
               slideIn.setToX(0);
               slideIn.play();

               // Hide notification after 3 seconds
               slideIn.setOnFinished(event -> {
                   PauseTransition pause = new PauseTransition(Duration.seconds(1));
                   pause.setOnFinished(p -> notificationContainer.getChildren().remove(notification));
                   pause.play();
               });
           });
       } else {
           // Execute the participation logic if the user hasn't participated yet
           boolean success = serviceEvenement.participer(evenement.getId(), currentUserId); // Updated to include user ID
           if (success) {
               Platform.runLater(() -> {
                   int newCount = serviceEvenement.getParticipantCount(evenement.getId());
                   participantsLabel.setText("Participants: " + newCount); // Update the label with the new count
                   participateButton.setText("Participated"); // Change button text

                   // Show notification
                   Notification notification = new Notification("You have successfully participated in this event!", notificationContainer);
                   notification.setTranslateX(notificationContainer.getWidth()); // Start off-screen
                   notification.setTranslateY(notificationContainer.getHeight() - notification.getHeight()); // Bottom right corner
                   notificationContainer.getChildren().add(notification);

                   // Slide in animation
                   TranslateTransition slideIn = new TranslateTransition(Duration.seconds(1), notification);
                   slideIn.setToX(0);
                   slideIn.play();

                   // Hide notification after 3 seconds
                   slideIn.setOnFinished(event -> {
                       PauseTransition pause = new PauseTransition(Duration.seconds(1));
                       pause.setOnFinished(p -> notificationContainer.getChildren().remove(notification));
                       pause.play();
                   });
               });
           } else {
               // If the event is full, update the button accordingly
               Platform.runLater(() -> {
                   participateButton.setText("Full");
                   participateButton.setDisable(true);

               });
           }
       }
   }
    public void showMapView(ActionEvent actionEvent) {
        try {
            // Load the AjouterEvenement.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiEvent/MapView.fxml"));
            Parent mapinterface = loader.load();

            // Get the controller instance
            MapViewController ajoutController = loader.getController();

            // Replace the content of eventDetailsVBox with the AjouterEvenement.fxml interface
            eventsTilePane.getChildren().clear();
            eventsTilePane.getChildren().add(mapinterface);

            // Make sure the eventDetailsVBox is visible and the tile pane with event cards is not
            //eventDetailsVBox.setVisible(true);
            eventsTilePane.setVisible(true);
            showMapViewButton.setVisible(false); // Set the button invisible

            //old display
            // Replace the content of eventDetailsVBox with the AjouterEvenement.fxml interface
           /* eventDetailsVBox.getChildren().clear();
            eventDetailsVBox.getChildren().add(mapinterface);

            // Make sure the eventDetailsVBox is visible and the tile pane with event cards is not
            eventDetailsVBox.setVisible(true);
            eventsTilePane.setVisible(false);*/

        } catch (IOException e) {
            e.printStackTrace();
        }

    }













    }




