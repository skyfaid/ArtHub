package controllers.Activities;

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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class DeleteActivite {
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
    /*
   @FXML
    private Pane contentPanee;

    */

    private String imageURL = "";

    @FXML
    private ImageView posterActimage;

    private  ServiceActivite serviceActivite = new ServiceActivite();
    private Activite selectedACT;

    @FXML
    private AnchorPane contentPane;
    private static AjouterActivite instance;

    public DeleteActivite() {
    }

    public static AjouterActivite getInstance() {
        return instance;
    }


    @FXML
    private void initialize() {
        afficheracti();

    }
    @FXML
    void supprimer(ActionEvent event) {
        try {
            // Similarly, assume a way to obtain the ID of the Activite to delete.
            String nomActivite = nomTF.getText(); // Use the name from the TextField to identify the activity to delete.

            // Call a new method in ServiceActivite that deletes by name
            serviceActivite.supprimer(nomActivite);
            showAlert(Alert.AlertType.INFORMATION, "Deletion Successful", null, "The activity has been successfully deleted.");
            afficheracti();
            //update table :
            loadActivitiesData(); // Reload data from the database
            //  Table_actFX.refresh(); // Refresh the TableView
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Deletion Error", e.getMessage());
        }
    }








    public void afficherparticipants(MouseEvent mouseEvent) {
        //showView(participationview);*
        participationview.getChildren().clear();
        try {
            Pane pane_act = FXMLLoader.load(getClass().getResource("/participation-fianle.fxml"));

            contentPane.getChildren().add(pane_act);

            // Make sure the eventsView is visible and set as the content to be displayed
            // Make sure the eventsView is visible and set as the content to be displayed
            // Assuming showView method makes the view visible within the contentPane.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



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



    private void afficheracti(){
        List<Activite> activites = serviceActivite.recuperer();

        //hedhizedthaaaaaa
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
        //imageView.setImage(getImageForAct(activite));
        //imageView.getStyleClass().add("image-view");

        Label nameLabel = new Label("Nom: " + activite.getNom_act());
        nameLabel.getStyleClass().add("label");

        Label dateLabel = new Label("Date: " + activite.getDateDebut().toString());
        dateLabel.getStyleClass().add("label");

        Label locationLabel = new Label("Lieu: " + activite.getLieu());
        locationLabel.getStyleClass().add("label");
        // Create an edit button
        Button editButton = new Button("Edit");
        /*editButton.setOnAction(event -> {
            openEditInterface(activite);
        });*/

        card.getChildren().addAll(imageView, nameLabel, dateLabel, locationLabel /*editButton*/);

        return card;
    }

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

    private Image getImageForAct(Activite activite) {
        String imageURL = activite.getPosterUrl();
        return new Image(new File(imageURL).toURI().toString(), true);
    }


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
                    // Save the image path with the prefix '/actimage/'
                    imageURL = "/actimage/" + newImagePath;
                } else {
                    // Handle the case where newImagePath is null
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the IOException
            }
        }
    }


    private String copyFileToImagesFolder(File sourceFile) throws IOException {
        // Define the target directory and construct the new file path
        File destDir = new File("C:\\Users\\bouch\\IdeaProjects\\PIDEV3A9\\src\\main\\resources\\actimage");
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
}





