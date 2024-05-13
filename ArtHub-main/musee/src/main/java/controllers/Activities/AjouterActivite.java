package controllers.Activities;

import com.itextpdf.text.*;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.draw.LineSeparator;
import entities.Activite;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceActivite;
import utils.Validation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;



public class AjouterActivite {
    public VBox ActVBox;
    @FXML
    private TilePane ActTilePane;
    @FXML
    public TextField modifyFX;
    public VBox activitesView;
    @FXML
    private VBox participationview;
    @FXML
    private Button closebutton;
    @FXML
    private TextField LieuTF;
    @FXML
    private Label error1;
    @FXML
    private DatePicker date_debFX;
    @FXML
    private DatePicker date_finFX;
    @FXML
    private TextField nbreTF;
    @FXML
    private TextField nomTF;
    @FXML
    private TextField typeFX;
    private String imageURL = "";
    @FXML
    private ImageView posterActimage;
    private  ServiceActivite serviceActivite = new ServiceActivite();
    private Activite selectedACT;
    @FXML
    private AnchorPane contentPane;
    @FXML
    private  TextField searchTextField ;
    @FXML
    private Button searchButton ;




    private static AjouterActivite instance;

    public AjouterActivite() {
    }

    public static AjouterActivite getInstance() {
        return instance;
    }


    @FXML
    private void initialize() {
        afficheracti();

    }


    private void filterEventCards(String searchText) {
        List<Activite> filteredActivites = serviceActivite.recuperer().stream()
                .filter(a -> a.getNom_act().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
        refreshEventList(filteredActivites);
    }


    public void refreshEventList(List<Activite> acts) {
        ActTilePane.getChildren().clear(); // Clear existing content
        for (Activite activite : acts) { // Evenement hia lclass, evenement variable fl loop representing a single instance of Evenement class,events represents a collection of Evenement objects

            VBox card = createActCard(activite); // Rebuild each card
            ActTilePane.getChildren().add(card);
        }
    }
    @FXML
    void generateActivitesPDF(ActionEvent event) {
        Document document = new Document();
        try {
            // FileChooser to select the location to save the PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("Activities_List.pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // Set title font and color
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.BLUE);
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
                Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

                // Add title with background color
                Paragraph title = new Paragraph("List of Activities", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);

                // Loop through each activity and create a section
                for (Activite activite : serviceActivite.recuperer()) {
                    // Create a table for activity details
                    PdfPTable table = new PdfPTable(5); // 5 columns
                    table.setWidthPercentage(100);
                    table.setSpacingBefore(10f);
                    table.setSpacingAfter(10f);

                    // Set custom column widths
                    float[] columnWidths = {1f, 3f, 2f, 2f, 2f};
                    table.setWidths(columnWidths);

                    // Set background color and font for header cells
                    PdfPCell cell = new PdfPCell(new Phrase("ID", headerFont));
                    cell.setBackgroundColor(BaseColor.DARK_GRAY);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Name", headerFont));
                    cell.setBackgroundColor(BaseColor.DARK_GRAY);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Start Date", headerFont));
                    cell.setBackgroundColor(BaseColor.DARK_GRAY);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("End Date", headerFont));
                    cell.setBackgroundColor(BaseColor.DARK_GRAY);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase("Type", headerFont));
                    cell.setBackgroundColor(BaseColor.DARK_GRAY);
                    table.addCell(cell);

                    // Add activity details
                    table.addCell(new Phrase(String.valueOf(activite.getId_activite()), normalFont));
                    table.addCell(new Phrase(activite.getNom_act(), normalFont));
                    table.addCell(new Phrase(String.valueOf(activite.getDateDebut()), normalFont));
                    table.addCell(new Phrase(String.valueOf(activite.getDateFin()), normalFont));
                    table.addCell(new Phrase(activite.getType_act(), normalFont));

                    // Add table to document
                    document.add(table);
                    // Add a line separator
                    LineSeparator separator = new LineSeparator();
                    separator.setPercentage(80);
                    separator.setLineColor(BaseColor.LIGHT_GRAY);
                    document.add(new Chunk(separator));
                    // Add space after each activity section
                    document.add(new Paragraph(" ", normalFont));
                }

                document.close();
                showAlert(Alert.AlertType.INFORMATION, "Success", "PDF Creation", "PDF file generated successfully.");
            }
        } catch (DocumentException | FileNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "PDF Generation Error", "An error occurred", e.getMessage());
        }
    }



    @FXML
    void handleSearch(ActionEvent event) {
        String searchText = searchTextField.getText();
        filterEventCards(searchText);
    }


    @FXML
    void ajouter(ActionEvent event) {
        String nomAct = nomTF.getText();
        String lieu = LieuTF.getText();
        String nbrePlacesStr = nbreTF.getText();
        String typeStr = typeFX.getText();
        try {
            int nbre_places = Integer.parseInt(nbreTF.getText());
            LocalDate dateDebut = date_debFX.getValue();
            LocalDate dateFin = date_finFX.getValue();
            if (Validation.validateDates(dateDebut, dateFin)) {
                if (Validation.validateNomAct(nomAct)) {
                    if (Validation.validateLieu(lieu)) {
                        if (Validation.validateNbrePlaces(nbrePlacesStr)) {
                            if(Validation.validateType(typeStr)) {
                                Activite activite = new Activite(nomTF.getText(), dateDebut, dateFin, LieuTF.getText(), nbre_places, typeFX.getText(), imageURL);
                                serviceActivite.ajouter(activite);
                                showAlert(Alert.AlertType.INFORMATION, "Success", null, "Activité ajoutée avec succès!");
                                afficheracti();
                                loadActivitiesData();
                            }else{showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Activity Type", "The Type must consist of only letters and be no longer than 15 characters.");}
                        }else{showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Number of Places", "The number of places must be a valid, positive integer.");}
                    }else{showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Location", "The location must be a non-empty string and can only contain letters, numbers, spaces, and certain punctuation (,.-).");}
                }else{showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Activity Name", "The name must consist of only letters and be no longer than 15 characters.");}
            } else{showAlert("Validation Error", "The end date must be after the start date.");}
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de Format", "Le nombre de places doit être un nombre valide.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l'ajout de l'activité", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur Inattendue", "Une erreur inattendue s'est produite", e.getMessage());
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        try {
            String currentNomActivite = modifyFX.getText();
            String newNomActivite = nomTF.getText();
            int nbrePlaces = Integer.parseInt(nbreTF.getText());
            Activite activite = new Activite();
            activite.setNom_act(newNomActivite);
            activite.setLieu(LieuTF.getText());
            activite.setDateFin(date_debFX.getValue());
            activite.setDateFin(date_finFX.getValue());
            activite.setNbre_places(nbrePlaces);
            activite.setType_act(typeFX.getText());
            activite.setPosterUrl(imageURL);
            serviceActivite.modifier(currentNomActivite, activite); // Pass the current name and the activite object.
            showAlert(Alert.AlertType.INFORMATION, "Succès", null, "L'activité a été modifiée avec succès.");
            loadActivitiesData(); // Reload data from the database
            afficheracti();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de Format", null, "Le nombre de places doit être un nombre entier.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", null, e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur Inattendue", null, e.getMessage());
        }
    }

    @FXML
    void supprimer(ActionEvent event) {
        try {
            String nomActivite = nomTF.getText();
            serviceActivite.supprimer(nomActivite);
            showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", null, "The activity has been successfully deleted.");
            afficheracti();
            loadActivitiesData();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Deletion Error", e.getMessage());
        }
    }

    private void afficheracti(){
        List<Activite> activites = serviceActivite.recuperer();
        ActTilePane.getChildren().clear();
        for (Activite activite : activites) {
            VBox card = createActCard(activite);
            ActTilePane.getChildren().add(card);
        }
    }

    private VBox createActCard(Activite activite) {
        VBox card = new VBox(10);
        //card.getStyleClass().add("card");
        ImageView imageView = new ImageView();
        Image image = new Image(getClass().getResourceAsStream(activite.getPosterUrl()));
        imageView.setImage(image);
        imageView.setFitHeight(150); // Size set in FXML
        imageView.setFitWidth(250);  // Size set in FXML
        Label nameLabel = new Label("Nom: " + activite.getNom_act());
        nameLabel.getStyleClass().add("label");
        Label dateLabel = new Label("Date: " + activite.getDateDebut().toString());
        dateLabel.getStyleClass().add("label");
        Label locationLabel = new Label("Lieu: " + activite.getLieu());
        locationLabel.getStyleClass().add("label");
        //Button editButton = new Button("Edit");
        card.getChildren().addAll(imageView, nameLabel, dateLabel, locationLabel /*editButton*/);
        return card;
    }

    public void afficherparticipants(MouseEvent mouseEvent) {
        //showView(participationview);*
        participationview.getChildren().clear();
        try {
            Pane pane_act = FXMLLoader.load(getClass().getResource("/guiActivite/participation-fianle.fxml"));

            contentPane.getChildren().add(pane_act);

            // Make sure the eventsView is visible and set as the content to be displayed
            // Make sure the eventsView is visible and set as the content to be displayed
            // Assuming showView method makes the view visible within the contentPane.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void loadActivitiesData(){
        List<Activite> activities = serviceActivite.recuperer(); // This method should return a list of all activities.
        //Table_actFX.setItems(FXCollections.observableArrayList(activities));
    }


    public void close(ActionEvent actionEvent) {
        // Get the stage of the button through the event source
        Stage stage = (Stage) closebutton.getScene().getWindow();
        stage.close(); // Close the current stage
    }


    public void showView(VBox view) {
        contentPane.getChildren().forEach(child -> child.setVisible(false));
        view.setVisible(true);
    }

    public void showViewTile(TilePane viewTile) {
        contentPane.getChildren().forEach(child -> child.setVisible(false));
        viewTile.setVisible(true);
    }




    private Image getImageForAct(Activite activite) {
        String imageURL = activite.getPosterUrl();
        return new Image(new File(imageURL).toURI().toString(), true);
    }
    @FXML
    void chargerImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image de profil");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers image", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                String newImagePath = copyFileToImagesFolder(selectedFile);
                if (newImagePath != null) {
                    imageURL = "/images/" + newImagePath;
                } else {
                    // Handle the case where newImagePath is null
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String copyFileToImagesFolder(File sourceFile) throws IOException {
        // Define the target directory and construct the new file path
        File destDir = new File("C:\\Users\\MSI\\Documents\\GitHub\\ArtHub\\ArtHub-main\\musee\\src\\main\\resources\\images");
        File destFile = new File(destDir, sourceFile.getName());

        // Copy file to the new location (This is just an example method call)
        Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // Return the absolute path as a String
        return destFile.getName();
    }


    private void displayEventImage(Activite activite) {
        // Get the image URL from the selected event
        String imageURL = activite.getPosterUrl();
        if (imageURL != null && !imageURL.isEmpty()) {
            // Load and display the image
            Image image = new Image(new File(imageURL).toURI().toString());
            posterActimage.setImage(image);
        } else {
            // Clear the image view if no image is available
            posterActimage.setImage(null);
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void initData(Activite selectedACT) {
        this.selectedACT = selectedACT;
        populateFields(selectedACT);
    }

    private void populateFields(Activite Act) {
        nomTF.setText(Act.getNom_act());
        date_debFX.setValue(Act.getDateDebut());
        date_finFX.setValue(Act.getDateFin());
        //  lieuFX.setText(Act.getLieu());
        nbreTF.setText(String.valueOf(Act.getNbre_places()));
        // typeeFX.setText(Act.getType_act());
    }

    public void afficherActs()
    { try {
        AnchorPane pane_event = FXMLLoader.load(getClass().getResource("/ListAct.fxml"));
        ActTilePane.getChildren().clear();
        // Add the loaded pane to the eventsView VBox
        ActTilePane.getChildren().add(pane_event);
        // Make sure the eventsView is visible and set as the content to be displayed
        // Make sure the eventsView is visible and set as the content to be displayed
        showViewTile(ActTilePane); // Assuming showView method makes the view visible within the contentPane.
    } catch (IOException e) {
        e.printStackTrace();
    }}






    /* @FXML
   public void afficherparticipants(MouseEvent event) {
       // Check if the source of the event is the button itself
       participationview.setVisible(false);
       if (event.getSource() instanceof Button) {
           Button btn = (Button) event.getSource();
           if ("afficherparticipants".equals(btn.getId())) {
               try {
                   Pane pane_act = FXMLLoader.load(getClass().getResource("/participation-fianle.fxml"));
                   contentPane.getChildren().add(pane_act);
                   //participationview.setVisible(true); // Make sure the VBox is visible
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
       // Consume the event to prevent propagation
       event.consume();
   }*/



/*
public void afficherActs()
{
    try {
        // Load the ListAct.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListAct.fxml"));
        Pane listActPane = loader.load();

        // Clear the children of ActTilePane
        ActTilePane.getChildren().clear();

        // Add the loaded pane to ActTilePane
        ActTilePane.getChildren().add(listActPane);

        // Ensure that ActTilePane is visible
        showViewTile(ActTilePane);
    } catch (IOException e) {
        e.printStackTrace();
    }
}*/



    /*public void chargerImage(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image ou vidéo");
        // Add video file extension filter
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Media Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.mp4")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imageURL = selectedFile.toURI().toString(); // Store the URI as a string
        }
    }*/

/*
    private void openEditInterface(Activite activite) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditInterface.fxml"));
            Parent editInterface = loader.load();
            // Pass the event information to the controller of the edit interface
            Controllers.EditInterfaceController controller = loader.getController();
            controller.initData(activite);
            // Get the stage and set the scene with the loaded interface
            Stage stage = new Stage();
            stage.setScene(new Scene(editInterface));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

    /*
    public void initialize() {
        // Initialize columns here...
        showView( activitesView);
        id_actFX.setCellValueFactory(new PropertyValueFactory<>("id_activite"));
        nom_actFX.setCellValueFactory(new PropertyValueFactory<>("nom_act"));
        date_debbFX.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        date_finnFX.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        lieuFX.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        nbr_plcFX.setCellValueFactory(new PropertyValueFactory<>("nbre_places"));
        typeeFX.setCellValueFactory(new PropertyValueFactory<>("type_act"));
        loadActivitiesData();
    }
*/



    /*
    public void initialize(){
        afficheracti();
    }*/

}





