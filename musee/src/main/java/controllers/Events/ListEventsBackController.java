package controllers.Events;

import entities.Evenement;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import services.ServiceEvenement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Platform;

import java.awt.image.BufferedImage;
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
    public Button showChartButton;
    public Button showMapViewButton;
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

    @FXML
    void initialize() {
        eventsVBox = new VBox();
        afficherEvenementsBack();
        // Initially hide the event details VBox
        eventDetailsVBox.setVisible(false);
        // Add listener to the searchTextField
        searchTextField.textProperty().addListener((observable, oldValue, newValue) ->filterEventCards(newValue));
    }
    private void afficherEvenementsBack() {
        List<Evenement> evenements = serviceEvenement.afficherfront(); // Get basic event info
        evenements.forEach(event -> {
            int participantCount = serviceEvenement.getParticipantCount(event.getId()); // Get updated participant count for each event
            event.setNombreParticipants(participantCount); // Update the event with this count
        });
        refreshEventList(evenements); // Refresh the UI with the updated list
        //hedhi for reference so you remember your mistakes in the future ya bhim
    /*private void afficherEvenements() {

        eventsTilePane.getChildren().clear(); // Clear existing event cards
        eventsTilePane.setVisible(true); // Show the tile pane
        eventDetailsVBox.setVisible(false); // Hide the editing details initially

        List<Evenement> evenements = serviceEvenement.afficherfront();

        for (Evenement evenement : evenements) {
            VBox card = createEventCard(evenement);
            eventsTilePane.getChildren().add(card);
        }
    }*/
    }
    private void filterEventCards(String searchText) {
        List<Evenement> filteredEvents = serviceEvenement.afficherfront().stream()
                .filter(e -> e.getNom().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
        refreshEventList(filteredEvents);
    }
    public void refreshEventList(List<Evenement> events) {
        eventsTilePane.getChildren().clear(); // Clear existing content
        for (Evenement evenement : events) { // Evenement hia lclass, evenement variable fl loop representing a single instance of Evenement class,events represents a collection of Evenement objects

            VBox card = createEventCard(evenement); // Rebuild each card
            eventsTilePane.getChildren().add(card);
        }
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

    }

    public void loadparticipantslist(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiEvent/ListParticipantsBack.fxml"));
            Parent participantsinterface = loader.load();

            // Get the controller instance
            ListParticipantsController listpartiController = loader.getController();

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
    public void displayEventChart(ActionEvent actionEvent) {
        // Data for the chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Fetch events and their participant counts
        List<Evenement> events = serviceEvenement.afficherback();
        for (Evenement event : events) {
            dataset.addValue(event.getNombreParticipants(), "Participants", event.getNom());
        }
        // Create the chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Event Participants", // Chart title
                "Event Names", // Domain axis label
                "Number of Participants", // Range axis label
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        // Increase the chart size
        BufferedImage chartImage = chart.createBufferedImage(800, 600); // Adjusted size
        WritableImage fxImage = new WritableImage(800, 600); // Adjusted size
        SwingFXUtils.toFXImage(chartImage, fxImage);

        // Display the chart in a new stage with increased size
        ImageView chartImageView = new ImageView(fxImage);
        StackPane pane = new StackPane(chartImageView);
        Scene scene = new Scene(pane, 800, 600); // Adjusted scene size
        Stage chartStage = new Stage();
        chartStage.setScene(scene);
        chartStage.setTitle("Event Participation Chart");
        chartStage.show();
    }















    public void retourner(MouseEvent mouseEvent) {
    }








}
