package controllers.Events;

import entities.Evenement;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import services.ServiceEvenement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import java.util.List;
import java.util.stream.Collectors;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class ListEventsBackController {
    public Button loadajoutinterface;
    public TextField searchTextField;
    public Button searchButton;
    public Button participantsbutton;
    @FXML
    private VBox eventDetailsVBox;
    @FXML
    public VBox eventsVBox;
    @FXML
    private TilePane eventsTilePane;
    @FXML
    private Label error1;
    @FXML
    private ImageView postereventimage;
    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private EditInterfaceController editInterfaceController;


    @FXML
    void initialize() {
       eventsVBox = new VBox();
        afficherEvenements();
        // Initially hide the event details VBox
        eventDetailsVBox.setVisible(false);
        // Add listener to the searchTextField
        searchTextField.textProperty().addListener((observable, oldValue, newValue) ->filterEventCards(newValue));
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
        }

    private void afficherEvenements() {

        eventsTilePane.getChildren().clear(); // Clear existing event cards
        eventsTilePane.setVisible(true); // Show the tile pane
        eventDetailsVBox.setVisible(false); // Hide the editing details initially

        List<Evenement> evenements = serviceEvenement.afficherfront();

        for (Evenement evenement : evenements) {
            VBox card = createEventCard(evenement);
            eventsTilePane.getChildren().add(card);
        }

       /* List<Evenement> evenements = serviceEvenement.afficher();
        for (Evenement evenement : evenements) {
            VBox card = createEventCard(evenement);
            eventsTilePane.getChildren().add(card);
        }*/
    }
    // Method to create the idle animation
    private void applyIdleAnimation(VBox card) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1.5), card);
        translateTransition.setByY(-8); // Adjust the distance for the animation
        //  translateTransition.setByY(10); // Adjust the distance for the animation
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.setAutoReverse(true);
        translateTransition.play();
    }
    private Image getImageForEvent(Evenement evenement) {
        String imageURL = evenement.getPosterUrl();
        if (imageURL != null && !imageURL.isEmpty()) {
            try {
                // Convert the file path to a URL
                return new Image(new File(imageURL).toURI().toURL().toString(), false); // false for background loading
            } catch (MalformedURLException e) {
                System.err.println("Malformed URL for image: " + imageURL);
                return null;
            }
        }
        return null;
    }
    private VBox createEventCard(Evenement evenement) {
        VBox card = new VBox();
        card.getStyleClass().add("card");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(150); // Size set in FXML
        imageView.setFitWidth(250);  // Size set in FXML
        // Fetch and set the image for the event
        Image eventImage = getImageForEvent(evenement);
        imageView.setImage(eventImage); // Set the fetched image to the ImageView
       //imageView.setImage(getImageForEvent(evenement));
        imageView.getStyleClass().add("image-view");

        Label nameLabel = new Label("Nom: " + evenement.getNom());
        nameLabel.getStyleClass().add("label");

        Label dateLabel = new Label("Date: " + evenement.getDatedebut().toString());
        dateLabel.getStyleClass().add("label");

        Label locationLabel = new Label("Lieu: " + evenement.getLieu());
        locationLabel.getStyleClass().add("label");

        // Label to display the number of participants
        Label participantsLabel = new Label("Participants: " + evenement.getNombreParticipants());
        participantsLabel.getStyleClass().add("label");

        // Create an edit button
        Button editButton = new Button("Edit");
        editButton.setOnAction(event -> {
            openEditInterface(evenement);

        });
        // Create a delete button
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            // Remove the selected event from the service
            serviceEvenement.supprimer(evenement);
            eventsTilePane.getChildren().remove(card);
            error1.setText("Événement supprimé avec succès!");
            error1.setStyle("-fx-text-fill: green;");
        });

        // Wrap buttons in an HBox, set spacing and alignment
        HBox buttonsBox = new HBox(10, editButton, deleteButton); // 10 is the spacing between buttons
        buttonsBox.setAlignment(Pos.CENTER);
        // Add buttonsBox to the card
        card.getChildren().addAll(imageView, nameLabel, dateLabel, locationLabel, participantsLabel, buttonsBox);
        // Apply idle animation to the card
        //applyIdleAnimation(card);
        return card;

    }

    @FXML
    void loadajoutinterface(ActionEvent event) {
        try {
            // Load the AjouterEvenement.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiEvent/AjouterEvenement.fxml"));
            Parent ajoutInterface = loader.load();

            // Get the controller instance
            AjouterEvenementController ajoutController = loader.getController();

            // Optionally, set the modifierEvenementController in the ajoutController
            ajoutController.setModifierEvenementController(this);

            // Replace the content of eventDetailsVBox with the AjouterEvenement.fxml interface
            eventDetailsVBox.getChildren().clear();
            eventDetailsVBox.getChildren().add(ajoutInterface);

            // Make sure the eventDetailsVBox is visible and the tile pane with event cards is not
            eventDetailsVBox.setVisible(true);
            eventsTilePane.setVisible(false);
        } catch (IOException e) {
            error1.setText("Erreur lors du chargement de l'interface d'ajout.");
            e.printStackTrace();
        }

    }
    // Method to update the event details and image URL
    public void updateEvent(Evenement event, String imageURL) {
        // Update the image URL of the Evenement object
        event.setPosterUrl(imageURL);

    }
    private void openEditInterface(Evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiEvent/EditInterface.fxml"));
            Parent editContent = loader.load();
            EditInterfaceController controller = loader.getController();
            // Initialize the postereventimage ImageView
            ImageView postereventimage = new ImageView();
            controller.initData(evenement,postereventimage); // Pass the postereventimage ImageView

            // Set the ModifierEvenementController instance in EditInterfaceController
            controller.setModifierEvenementController(this);

            // Clear any previous content in the eventDetailsVBox and add the edit interface content to it
            eventDetailsVBox.getChildren().clear();
            eventDetailsVBox.getChildren().add(editContent);

            // Make sure the eventDetailsVBox is visible and the tile pane with event cards is not
            eventDetailsVBox.setVisible(true);
            eventsTilePane.setVisible(false);

        } catch (IOException e) {
            error1.setText("Erreur lors du chargement de l'interface d'édition.");
            e.printStackTrace();
        }

           /* try {
                // Load the editinterface.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditInterface.fxml"));
                Parent editInterface = loader.load();

                // Get the controller instance
                EditInterfaceController editInterfaceController = loader.getController();

                // Initialize the controller with the selected event's data using initData
                editInterfaceController.initData(evenement);

                // Replace the content of eventsVBox with the edit interface
                eventsVBox.getChildren().clear();
                eventsVBox.getChildren().add(editInterface);
            } catch (IOException e) {
                e.printStackTrace();
            }*/


      /*  try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditInterface.fxml"));
            Parent editInterface = loader.load();
            EditInterfaceController editInterfaceController = loader.getController();
            FieldPopulator.populateFields(evenement, editInterfaceController.getNomTF(), editInterfaceController.getTypeTF(),
                    editInterfaceController.getDatedebutPicker(), editInterfaceController.getDatefinPicker(),
                    editInterfaceController.getLieuTF(), editInterfaceController.getNombrePlacesTF(),
                    editInterfaceController.getDescriptionTF());
            eventsVBox.getChildren().clear();
            eventsVBox.getChildren().add(editInterface);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


     /*// Ensure only the relevant details are visible
        eventDetailsVBox.setVisible(true); // Show the details box for editing

        // Populate fields for the selected event
        populateFields(evenement);

        // Optionally, hide other UI elements that should not be visible during editing
        eventsTilePane.setVisible(false); // Hide the tile pane listing events

        selectedEvent = evenement;*/

      /*  // Clear existing content in the eventsVBox
        eventsVBox.getChildren().clear();

        // Generate the event details VBox
        VBox eventDetailsVBox = generateEventDetailsVBox(evenement);

        // Add the generated event details VBox to the eventsVBox
        eventsVBox.getChildren().add(eventDetailsVBox);

        // Make the eventDetailsVBox visible
        eventDetailsVBox.setVisible(true);

        // Hide the TilePane containing the displayed events
        eventsTilePane.setVisible(false);

        // Initialize data for editing
        initData(evenement);*/

    }
    public void retourner(MouseEvent mouseEvent) {
    }

    public void loadparticipantslist(ActionEvent actionEvent) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiEvent/ListParticipantsBack.fxml"));
            Parent participantsinterface = loader.load();

            // Get the controller instance
            ListParticipantsController listpartiController = loader.getController();

            // Optionally, set the modifierEvenementController in the ajoutController
           // listpartiController.setModifierEvenementController(this);

            // Replace the content of eventDetailsVBox with the ListParticipantsBack.fxml interface
            eventDetailsVBox.getChildren().clear();
            eventDetailsVBox.getChildren().add(participantsinterface);

            // Make sure the eventDetailsVBox is visible and the tile pane with event cards is not
            eventDetailsVBox.setVisible(true);
            eventsTilePane.setVisible(false);
        } catch (IOException e) {
            error1.setText("Erreur lors du chargement de l'interface d'ajout.");
            e.printStackTrace();
        }


    }

    /*  @FXML
    private void modifierEvent(ActionEvent event) {
        if (selectedEvent != null) {
            // Call the method to populate the event details VBox with the selected event's details
            populateEventDetails(selectedEvent);
        } else {
            error1.setText("Veuillez sélectionner un événement à modifier.");
            error1.setStyle("-fx-text-fill: red;");
        }
    }*/
  /*  @FXML
    void modifierEvent(ActionEvent event) {
        if (selectedEvent != null) {
            try {
                LocalDate dateDebut = datedebutPicker1.getValue();
                LocalDate dateFin = datefinPicker1.getValue();
                String lieuText = lieu1.getText();
                String nomText = nom1.getText();
                String nombrePlacesText = nombrePlaces1.getText();
                String typeText = type1.getText();
                String descriptionText = description1.getText();

                if (!nombrePlacesText.matches("\\d+")) {
                    throw new NumberFormatException("Nombre de places must be a number.");
                }
                int nombrePlacesInt = Integer.parseInt(nombrePlacesText);

                if (dateDebut == null || dateFin == null || dateFin.isBefore(dateDebut)) {
                    throw new IllegalArgumentException("Date de fin must be after Date de debut.");
                }

                // Update the selected event with modified attributes
                selectedEvent.setNom(nomText);
                selectedEvent.setType(typeText);
                selectedEvent.setDatedebut(dateDebut);
                selectedEvent.setDatefin(dateFin);
                selectedEvent.setLieu(lieuText);
                selectedEvent.setNombrePlaces(nombrePlacesInt);
                selectedEvent.setDescription(descriptionText);

                // Update the image URL
                imageURL = selectedEvent.getPosterUrl();

                // Update the event in the service
                serviceEvenement.modifier(selectedEvent);

                error1.setText("Événement modifié avec succès!");
                error1.setStyle("-fx-text-fill: green;");
            } catch (NumberFormatException e1) {
                error1.setText("Erreur: Nombre de places must be a number.");
                error1.setStyle("-fx-text-fill: red;");
            } catch (IllegalArgumentException e2) {
                error1.setText("Erreur: " + e2.getMessage());
                error1.setStyle("-fx-text-fill: red;");
            }
        } else {
            error1.setText("Veuillez sélectionner un événement à modifier.");
            error1.setStyle("-fx-text-fill: red;");
        }
    }*/


}
