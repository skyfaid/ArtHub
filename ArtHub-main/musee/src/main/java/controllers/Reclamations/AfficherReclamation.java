package controllers.Reclamations;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import entities.Reclamation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.ServiceReclamation;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.layout.VBox;



public class AfficherReclamation {
    public ImageView productImageView;
    @FXML
    private TableView<Reclamation> tableViewReclamations;
    @FXML
    private TableColumn<Reclamation, Integer> columnReference;
    @FXML
    private TableColumn<Reclamation, String> columnDescription;
    @FXML
    private TableColumn<Reclamation, String> columnPhoneNumber;
    @FXML
    private TableColumn<Reclamation, String> columnProductPNG;
    @FXML
    private TableColumn<Reclamation, String> columnStatus;
    @FXML
    private TableColumn<Reclamation, String> columnDateSubmitted;
    @FXML
    private Button modify, supprimer,returnBtn;
    @FXML
    private VBox homeView,eventsView,usersView,articlesView,oeuvresView,activitesView,reclamationsView,formationsView;
    private final ServiceReclamation serviceReclamation = new ServiceReclamation();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadReclamationData();
        setupSelectionModel();
    }
    private void setupSelectionModel() {
        tableViewReclamations.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String imagePath = newSelection.getProductPNG();
                if (imagePath != null && !imagePath.isEmpty()) {
                    // Update the ImageView with the image from the selected item
                    Image image = new Image(imagePath);
                    productImageView.setImage(image);
                } else {
                    // Clear the ImageView if no image is present
                    productImageView.setImage(null);
                }
            }
        });
    }

    private void setupTableColumns() {
        columnReference.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        columnDateSubmitted.setCellValueFactory(new PropertyValueFactory<>("dateSubmitted"));
        columnPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    }

    private void loadReclamationData() {
        try {
            List<Reclamation> reclamations = serviceReclamation.recuperer();
            tableViewReclamations.setItems(FXCollections.observableArrayList(reclamations));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Load Failed", "Could not load reclamations: " + e.getMessage());
        }
    }
    @FXML
    private void deleteReclamation() {
        Reclamation selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            try {
                serviceReclamation.supprimer(selectedReclamation.getReclamationID());
                tableViewReclamations.getItems().remove(selectedReclamation);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Reclamation deleted successfully.");
                loadReclamationData(); // Refresh data
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Could not delete the reclamation: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a reclamation to delete.");
        }
    }

    @FXML
    private void modifyReclamation() {
        Reclamation selected = tableViewReclamations.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showModifyDialog(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a reclamation to modify.");
        }
    }

    private String productPNGPath;

    private void showModifyDialog(Reclamation reclamation) {
        Dialog<Reclamation> dialog = new Dialog<>();
        dialog.setTitle("Modify Reclamation");
        dialog.setHeaderText("Modify the selected reclamation");
        ButtonType modifyButtonType = new ButtonType("Modify", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField description = new TextField(reclamation.getDescription());
        TextField reference = new TextField(String.valueOf(reclamation.getId()));
        Label productPNGPathLabel = new Label(); // Initialize the label to display the selected image path
        if (reclamation.getProductPNG() != null) {
            productPNGPath = reclamation.getProductPNG();
            productPNGPathLabel.setText(productPNGPath);
        }
        Button uploadImageButton = new Button("Upload Image");
        uploadImageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                productPNGPath = "/images/"+file.getName();
               // productPNGPathLabel.setText("/images/"+productImagePath); // Update label with file path
                productPNGPathLabel.setText(productPNGPath);
            }
        });

        grid.add(new Label("Description:"), 0, 0);
        grid.add(description, 1, 0);
        grid.add(new Label("Reference:"), 0, 1);
        grid.add(reference, 1, 1);
        grid.add(new Label("Product PNG:"), 0, 2);
        grid.add(uploadImageButton, 1, 2);
        grid.add(productPNGPathLabel, 2, 2); // Place the label next to the upload button

        dialog.getDialogPane().setContent(grid);

        javafx.application.Platform.runLater(description::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                reclamation.setDescription(description.getText());
                reclamation.setId(Integer.parseInt(reference.getText()));
                reclamation.setProductPNG(productPNGPath); // Use the path from the label
                return reclamation;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            try {
                serviceReclamation.modifier(reclamation.getReclamationID(), result);
                loadReclamationData(); // Refresh the table view
                showAlert(Alert.AlertType.INFORMATION, "Modification Successful", "Your reclamation has been updated successfully.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Modification Error", "Failed to modify the reclamation: " + e.getMessage());
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
   }