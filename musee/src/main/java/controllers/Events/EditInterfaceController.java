package controllers.Events;

import entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceEvenement;
import javafx.stage.FileChooser;
import java.io.File;
import java.time.LocalDate;

import static utils.FieldPopulator.populateFields;

public class EditInterfaceController {

    public VBox eventDetailsVBox;
    public Button confirmermodif;
    public Button chargerposter;
    public VBox notificationContainer;
    public Button chargervideo;
    @FXML
    private TextField nomEditTF;
    @FXML
    private TextField typeEditTF;
    @FXML
    private DatePicker datedebutEditPicker;
    @FXML
    private DatePicker datefinEditPicker;
    @FXML
    private TextField lieuEditTF;
    @FXML
    private TextField nbrePlacesEditTF;
    @FXML
    private TextField descriptionEditTF;
    @FXML
    private Label error1;
    private Evenement selectedEvent;
    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    private ListEventsBackController listEventsBackController;

    public void setModifierEvenementController(ListEventsBackController listEventsBackController) {
        this.listEventsBackController = listEventsBackController;
    }
    private ImageView postereventimage; // ImageView to display the event image in the event card

    // Method to initialize data in the controller
    public void initData(Evenement selectedEvent, ImageView postereventimage) {
        this.selectedEvent = selectedEvent;
        this.postereventimage = postereventimage; // Assign the postereventimage ImageView
        // Populate fields with event details
        populateFields(selectedEvent, nomEditTF, typeEditTF,
                datedebutEditPicker, datefinEditPicker,
                lieuEditTF, nbrePlacesEditTF,
                descriptionEditTF);
        // Display event image
        displayEventImage(selectedEvent);
    }

    private void displayEventImage(Evenement selectedEvent) {
        // Get the image URL from the selected event
        String imageURL = selectedEvent.getPosterUrl();
        if (imageURL != null && !imageURL.isEmpty()) {
            // Load and display the image
            Image image = new Image(new File(imageURL).toURI().toString());
            postereventimage.setImage(image);
        } else {
            // Clear the image view if no image is available
            postereventimage.setImage(null);
        }
    }

    @FXML
    void confirmermodif(ActionEvent event) {
        // Check if an event is selected
        if (selectedEvent != null) {
            // Get the values from the input fields
            String nomText = nomEditTF.getText();
            String typeText = typeEditTF.getText();
            LocalDate dateDebut = datedebutEditPicker.getValue();
            LocalDate dateFin = datefinEditPicker.getValue();
            String lieuText = lieuEditTF.getText();
            String nombrePlacesText = nbrePlacesEditTF.getText();
            String descriptionText = descriptionEditTF.getText();

            // Check if any field is empty
            if (nomText.isEmpty() || typeText.isEmpty() || dateDebut == null || dateFin == null ||
                    lieuText.isEmpty() || nombrePlacesText.isEmpty() || descriptionText.isEmpty()) {
                Notification notification = new Notification("Tous les champs sont obligatoires.", notificationContainer);
                notification.show();
                return;
            }

            try {
                // Validate the nombre de places field
                int nombrePlacesInt = Integer.parseInt(nombrePlacesText);
                if (nombrePlacesInt <= 0) {
                    throw new IllegalArgumentException("Nombre de places doit être un nombre positif.");
                }

                // Validate the dates
                if (dateFin.isBefore(dateDebut)) {
                    throw new IllegalArgumentException("Date de fin doit être après la date de début.");
                }

                // Update the event details with values from text fields
                selectedEvent.setNom(nomText);
                selectedEvent.setType(typeText);
                selectedEvent.setDatedebut(dateDebut);
                selectedEvent.setDatefin(dateFin);
                selectedEvent.setLieu(lieuText);
                selectedEvent.setNombrePlaces(nombrePlacesInt);
                selectedEvent.setDescription(descriptionText);

                // Update the event in the database or wherever you store the events
                serviceEvenement.modifier(selectedEvent);

                // Show success notification
                Notification successNotification = new Notification("Événement modifié avec succès!", notificationContainer);
                successNotification.show();
            } catch (NumberFormatException e) {
                // Show error notification for invalid number format
                Notification numberFormatErrorNotification = new Notification("Nombre de places doit être un nombre valide.", notificationContainer);
                numberFormatErrorNotification.show();
            } catch (IllegalArgumentException e) {
                // Show error notification for other validation errors
                Notification validationErrorNotification = new Notification(e.getMessage(), notificationContainer);
                validationErrorNotification.show();
            } catch (Exception e) {
                // Show error notification for general exceptions
                Notification errorNotification = new Notification("Erreur lors de la modification de l'événement.", notificationContainer);
                errorNotification.show();
            }
        } else {
            // Show error message if no event is selected
            Notification selectEventNotification = new Notification("Veuillez sélectionner un événement à modifier.", notificationContainer);
            selectEventNotification.show();
        }
    }


    @FXML
    void chargerposter(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        // Set extension filters, for example, for images
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);

        // Show open file dialog to select an image
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            // Use absolute path instead of URI
            String imagePath = file.getAbsolutePath();
            selectedEvent.setPosterUrl(imagePath); // Update the event object with the new image path
            // Load the image with the new path
            Image image = new Image(file.toURI().toString());
            postereventimage.setImage(image);
        }
        }
    public void chargervideo(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        // Set extension filters for video files
        FileChooser.ExtensionFilter videoFilter = new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv");
        fileChooser.getExtensionFilters().add(videoFilter);

        // Show open file dialog to select a video
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            // Use absolute path instead of URI
            String videoPath = file.getAbsolutePath();
            selectedEvent.setVideoUrl(videoPath); // Update the event object with the new video path
            // You can add logic here to handle the video, such as displaying a thumbnail or preview
        }



    }

    @FXML
    private void cancelEdit() {
        // Close the window or do other necessary actions
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nomEditTF.getScene().getWindow();
        stage.close();
    }

    public TextField getNomTF() {
        return nomEditTF;
    }

    public TextField getTypeTF() {
        return typeEditTF;
    }

    public DatePicker getDatedebutPicker() {
        return datedebutEditPicker;
    }

    public DatePicker getDatefinPicker() {
        return datefinEditPicker;
    }

    public TextField getLieuTF() {
        return lieuEditTF;
    }

    public TextField getDescriptionTF() {
        return descriptionEditTF;
    }

    public TextField getNombrePlacesTF() {
        return nbrePlacesEditTF;
    }


}
