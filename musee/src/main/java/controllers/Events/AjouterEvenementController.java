package controllers.Events;

import entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import services.ServiceEvenement;
import utils.DuplicateEventException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AjouterEvenementController {

    public Pane ajouterevent;
    public Button chargerVideo;
    @FXML
    private TextField nom;

    @FXML
    private TextField type;

    @FXML
    private DatePicker datedebutPicker;

    @FXML
    private DatePicker datefinPicker;

    @FXML
    private Label error;

    @FXML
    private TextField lieu;

    @FXML
    private TextField nombrePlaces;

    @FXML
    private Button retour;

    @FXML
    private TextField description;

    @FXML
    private Button chargerImage;

    @FXML
    private VBox notificationContainer;

    private String imageURL = "";
    private String trailerURL = "";

    private Notification notification;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    private List<Evenement> evenements = new ArrayList<>();

    private ListEventsBackController listEventsBackController;
    @FXML
    private VBox eventsView;
    @FXML
    private Pane contentPane;

    @FXML
    void initialize() {}
    private void showView(VBox view) {
        contentPane.getChildren().forEach(child -> child.setVisible(false));
        view.setVisible(true);
    }
    public void setModifierEvenementController(ListEventsBackController controller) {
        this.listEventsBackController = controller;
    }

    @FXML
    public void ajouterNouveauEvenement(ActionEvent event) {
        String nomText = nom.getText();
        String typeText = type.getText();
        LocalDate dateDebut = datedebutPicker.getValue();
        LocalDate dateFin = datefinPicker.getValue();
        String lieuText = lieu.getText();
        String nombrePlacesText = nombrePlaces.getText();
        String descriptionText = description.getText();

        // Check if all text fields are empty
        if (nomText.isEmpty() && typeText.isEmpty() && dateDebut == null && dateFin == null && lieuText.isEmpty() && nombrePlacesText.isEmpty() && descriptionText.isEmpty()) {
            Notification notification = new Notification("Tous les champs sont vides.",  notificationContainer);
            notification.show();
            return;
        }
        // Check if the image URL is empty
        if (imageURL.isEmpty()) {
            Notification notification = new Notification("Poster image is not added", notificationContainer);
            notification.show();
            return;
        }
        try {
            if (!nombrePlacesText.matches("\\d+")) {
                throw new NumberFormatException("Nombre de places doit être un nombre.");
            }
            int nombrePlacesInt = Integer.parseInt(nombrePlacesText);

            if (dateDebut == null || dateFin == null || dateFin.isBefore(dateDebut)) {
                throw new IllegalArgumentException("Date de fin doit être après Date de début.");
            }

            // Check if an event with similar details already exists
            boolean eventExists = evenements.stream()
                    .anyMatch(e -> e.getNom().equals(nomText) &&
                            e.getDatedebut().equals(dateDebut) &&
                            e.getDatefin().equals(dateFin) &&
                            e.getLieu().equals(lieuText) &&
                            e.getType().equals(typeText) &&
                            e.getDescription().equals(descriptionText) &&
                            e.getNombrePlaces() == nombrePlacesInt);

            if (eventExists) {
                Notification notification = new Notification( "this event already exists", notificationContainer);
                notification.show();
                return;
            }

           //Evenement newEvent = new Evenement(nomText, dateDebut, dateFin, lieuText, typeText, descriptionText, nombrePlacesInt, imageURL);

            Evenement newEvent = new Evenement(nomText, dateDebut, dateFin, lieuText, typeText, descriptionText, nombrePlacesInt, imageURL, trailerURL);

            // Add the new event to the list
            evenements.add(newEvent);

            // Add the new event to the service
            serviceEvenement.ajouter(newEvent);

            Notification notification = new Notification( "Événement ajouté avec succès!", notificationContainer);
            notification.show();
        } catch (DuplicateEventException e) {
            Notification notification = new Notification("Duplicate event detected! " + e.getMessage(), notificationContainer);
            notification.show();
        } catch (NumberFormatException e) {
            Notification notification = new Notification("Nombre de places doit être un nombre.", notificationContainer);
            notification.show();
        } catch (IllegalArgumentException e) {
            Notification notification = new Notification("Date debut doit être avant la date fin", notificationContainer);
            notification.show();
        }
    }

    @FXML
    void retourtolistevents(ActionEvent event) throws IOException {










    }

   @FXML
    void chargerImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imageURL = selectedFile.getAbsolutePath();
        }
    }
    @FXML
    void chargerVideo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier vidéo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv")); // Add video file extensions here
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            trailerURL = selectedFile.getAbsolutePath();
        }
    }




}


