package controllers.Events;

import entities.Evenement;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import services.ServiceEvenement;
import services.ServiceParticipant;
import utils.SessionManager;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

public class ListEventsFrontController {

    @FXML
    public TilePane eventsTilePane;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    private final IntegerProperty participantCount = new SimpleIntegerProperty();
    private final ServiceParticipant serviceParticipant = new ServiceParticipant(); // Ensure you have this instance

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

    public void refreshEventList(List<Evenement> events) {
            eventsTilePane.getChildren().clear(); // Clear existing content
            for (Evenement evenement : events) {
                VBox card = createEventCard(evenement); // Rebuild each card
                eventsTilePane.getChildren().add(card);
            }
            /* eventsTilePane.getChildren().clear();
        for (Evenement evenement : events) {
            VBox card = createEventCard(evenement);
            Label participantsLabel = (Label) card.getChildren().get(card.getChildren().size() - 2);
            participantsLabel.setText("Participants: " + evenement.getNombreParticipants());
            eventsTilePane.getChildren().add(card);
        }*/
        }

    private void afficherEvenements() {
        List<Evenement> evenements = serviceEvenement.afficherfront();
        refreshEventList(evenements);
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

    private void applyIdleAnimation(VBox card) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1.5), card);
        translateTransition.setByY(-8); // Adjust the distance for the animation
        //  translateTransition.setByY(10); // Adjust the distance for the animation
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.setAutoReverse(true);
        translateTransition.play();
    }

  /*  private VBox createEventCard(Evenement evenement) {
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

        Button participateButton = new Button("Participate");
        participateButton.setOnAction(actionEvent -> handleParticipateButtonAction(evenement, participateButton));
        // Apply idle animation to the card
        applyIdleAnimation(card);
        card.getChildren().addAll(imageView, nameLabel, dateLabel, locationLabel, participantsLabel, participateButton);
        return card;
    }  old */

   /*old one private void handleParticipateButtonAction(Evenement evenement, Button participateButton) {
        boolean success = serviceEvenement.participer(evenement.getId());
        if (success) {
            // Update the event with the new number of participants
            evenement.setNombreParticipants(serviceEvenement.getParticipantCount(evenement.getId()));
            afficherEvenements(); // This will call refreshEventList internally
            participateButton.setText("Participated");
        } else {
            participateButton.setText("Full");
            participateButton.setDisable(true);
        }
    }*/

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

        Button participateButton = new Button("Participate");
        participateButton.setOnAction(actionEvent -> handleParticipateButtonAction(evenement, participateButton, participantsLabel));
        applyIdleAnimation(card);
        card.getChildren().addAll(imageView, nameLabel, dateLabel, locationLabel, participantsLabel, participateButton);
        return card;
    }

    private void handleParticipateButtonAction(Evenement evenement, Button participateButton, Label participantsLabel) {
        // Disable the button immediately to prevent multiple clicks
        participateButton.setDisable(true);

        int currentUserId = SessionManager.getInstance().getCurrentUserId(); // Assuming this gets the current user's ID correctly

        // Check if the user has already participated
        boolean alreadyParticipated = serviceParticipant.hasUserParticipated(evenement.getId(), currentUserId);

        if (alreadyParticipated) {
            Platform.runLater(() -> {
                participateButton.setText("Already Participated");
                participateButton.setDisable(true);
            });
        } else {
            // Execute the participation logic if the user hasn't participated yet
            boolean success = serviceEvenement.participer(evenement.getId(), currentUserId); // Updated to include user ID
            if (success) {
                Platform.runLater(() -> {
                    int newCount = serviceEvenement.getParticipantCount(evenement.getId());
                    participantsLabel.setText("Participants: " + newCount); // Update the label with the new count
                    participateButton.setText("Participated"); // Change button text
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






}



