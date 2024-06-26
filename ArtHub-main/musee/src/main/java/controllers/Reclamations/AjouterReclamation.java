package controllers.Reclamations;
import entities.Reclamation;
import entities.Serverclass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceReclamation;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import utils.UserConnected;


public class AjouterReclamation {
    public Button ajouter;
    @FXML
    private VBox afficherreclamation2, ajouterreclamation1,affichersolution3;
    @FXML
    private AnchorPane contentPane;
    @FXML
    private TextField oeuvreid,phoneField;
    @FXML
    private TextField description;
    @FXML
    private Button imageButton;

    @FXML
    private Button viewReclamation;

    private String productImagePath;
    private String imageURL = "";
    private static final String pathPhoto = "C:\\Users\\MSI\\Documents\\GitHub\\ArtHub\\ArtHub-main\\musee\\src\\main\\resources\\images";
    private Serverclass server;

    private final ServiceReclamation serviceReclamation = new ServiceReclamation();
    public void initialize() {
        afficherreclamation2.setVisible(false);
        ajouterreclamation1.setVisible(true);
        setupValidationListeners();

        new Thread(() -> {
            try {
                server = Serverclass.getInstance();
            }catch ( IOException e ) {
                e.printStackTrace();
                System.out.println("error creating server");
            }
        }).start();

    }
    private void setupValidationListeners() {
        oeuvreid.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                validateTextField(oeuvreid);
            }
        });

        description.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                validateTextField(description);
            }
        });
    }


    private void validateTextField(TextField field) {
        if (field.getText().isEmpty()) {
            field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        } else {
            if (field == oeuvreid && field.getText().matches("\\d+")) {
                // If oeuvreid and contains only digits
                field.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
            } else if (field == description) {
                // If description is not empty
                field.setStyle("-fx-border-color:green; -fx-border-width: 2px;");
            } else {
                // If neither condition is met, set red border
                field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
        }
    }



    @FXML
    private void showView(VBox view) {
        contentPane.getChildren().forEach(child -> child.setVisible(false));
        view.setVisible(true);
    }
    @FXML
    void ajouter(ActionEvent event) {
        boolean isValid = true;
        if (oeuvreid.getText().length() == 0) {
            validateTextField(oeuvreid);
            isValid = false;
        }
        if (description.getText().length() == 0) {
            validateTextField(description);
            isValid = false;
        }

        if (!isValid) {
            // If any field is invalid, stop the method here
            return;
        }

        try {
            Reclamation reclamation = new Reclamation();
            reclamation.setId(Integer.parseInt(oeuvreid.getText()));
            reclamation.setDescription(description.getText());
            reclamation.setPhoneNumber(phoneField.getText());

            reclamation.setUtilisateur_id(UserConnected.getUser().getUtilisateurId());
            reclamation.setStatus("pending");
            reclamation.setDateSubmitted(LocalDateTime.now());
            reclamation.setProductPNG( imageURL);

            serviceReclamation.ajouter(reclamation);

            showAlert(Alert.AlertType.INFORMATION, "Reclamation added successfully", "✅.");
            oeuvreid.clear();
            description.clear();
            productImagePath = null;
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid input", "Please enter a valid reference.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database error", e.getMessage());
        }
        oeuvreid.clear();
        oeuvreid.setStyle(null);
        description.clear();
        description.setStyle(null);
        productImagePath = null;
    }

    @FXML
    public void imageupload(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
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
    @FXML
    private void viewReclamation(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/guiReclamation/afficherReclamation.fxml"));
            Parent fxmlRoot = fxmlLoader.load();

            afficherreclamation2.getChildren().clear();

            afficherreclamation2.getChildren().add(fxmlRoot);

            ajouterreclamation1.setVisible(false);
            afficherreclamation2.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the FXML file.");
        }
        /*try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherReclamation.fxml"));
            Parent root = loader.load();

            //Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the view reclamation page.");
        }*/
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void viewSolution(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/guiReclamation/affichersolutionuser.fxml"));
            Parent fxmlRoot = fxmlLoader.load();

            affichersolution3.getChildren().clear();

            affichersolution3.getChildren().add(fxmlRoot);

            ajouterreclamation1.setVisible(false);
            afficherreclamation2.setVisible(false);
            affichersolution3.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the FXML file.");
        }

    }
    @FXML
    public void showchat(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/guiReclamation/client.fxml"));

        Client controller = new Client();
        fxmlLoader.setController(controller);
        primaryStage.setScene(new Scene(fxmlLoader.load()));
        primaryStage.setTitle("Client");
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
        //////////////
        Stage primaryStage1 = new Stage();
        FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("/guiReclamation/server.fxml"));
        Server controller1 = new Server();
        fxmlLoader1.setController(controller1);
        primaryStage1.setScene(new Scene(fxmlLoader1.load()));
        primaryStage1.setTitle("Server");
        primaryStage1.setResizable(false);
        primaryStage1.centerOnScreen();
        primaryStage1.show();
    }

}