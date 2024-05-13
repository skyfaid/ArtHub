package controllers.Reclamations;

import entities.Reclamation;
import entities.Solution;
import entities.Utilisateur;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import services.ServiceReclamation;
import services.ServiceSolution;
import services.ServiceUtilisateur;
import utils.EmailService;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class AjouterSolution {


    public Button viewSolutionButton;
    public VBox affichersolutions;
    public VBox ajoutersolution1;
    @FXML
    private TableView<Reclamation> tableViewReclamations;
    @FXML private TableColumn<Reclamation, Integer> columnReclamationID;
    @FXML private TableColumn<Reclamation, Integer> columnUtilisateurID;
    @FXML
    private TableColumn<Reclamation, Integer> columnReference;
    @FXML
    private TableColumn<Reclamation, String> columnDescription;
    @FXML
    private TableColumn<Reclamation, String> columnProductPNG;
    @FXML
    private TableColumn<Reclamation, String> columnStatus;
    @FXML
    private TableColumn<Reclamation, LocalDateTime> columnDateSubmitted;

    @FXML
    private Button replyButton;
    public ImageView productImageView;


    private ServiceReclamation ServiceReclamation = new ServiceReclamation();

    private ServiceSolution ServiceSolution = new ServiceSolution();

    @FXML
    private void initialize() {
        ServiceReclamation = new ServiceReclamation();
        columnReclamationID.setCellValueFactory(new PropertyValueFactory<>("ReclamationID"));
        columnUtilisateurID.setCellValueFactory(new PropertyValueFactory<>("utilisateur_id"));
        columnReference.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        columnProductPNG.setCellValueFactory(new PropertyValueFactory<>("productPNG"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        columnDateSubmitted.setCellValueFactory(new PropertyValueFactory<>("DateSubmitted"));

        loadReclamationData();
    }
    private void loadReclamationData() {
        try {
            ServiceReclamation reclamation = new ServiceReclamation();
            List<Reclamation> reclamations = reclamation.recuperer();
            tableViewReclamations.setItems(FXCollections.observableArrayList(reclamations));
            System.out.println("TableView should now display " + tableViewReclamations.getItems().size() + " items.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void showReplyDialog(Reclamation reclamation) {
        Dialog<Solution> dialog = new Dialog<>();
        dialog.setTitle("Reply to Reclamation");
        dialog.setHeaderText("Provide a reply to the selected reclamation");

        ButtonType replyButtonType = new ButtonType("Reply", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(replyButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Accepted", "Declined");
        TextField refundAmountField = new TextField();
        TextField adminFeedbackField = new TextField();

        grid.add(new Label("Status:"), 0, 0);
        grid.add(statusComboBox, 1, 0);
        grid.add(new Label("Refund Amount:"), 0, 1);
        grid.add(refundAmountField, 1, 1);
        grid.add(new Label("Admin Feedback:"), 0, 2);
        grid.add(adminFeedbackField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> statusComboBox.requestFocus());

        Solution solution = new Solution();

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == replyButtonType) {
                // Assign values to solution object
                solution.setReclamationID(reclamation.getReclamationID());
                solution.setUtilisateur_id(reclamation.getUtilisateur_id());
                solution.setStatus(statusComboBox.getValue());
                solution.setRefundAmount(Float.parseFloat(refundAmountField.getText()));
                solution.setAdminFeedback(adminFeedbackField.getText());
                solution.setDateResolved(Timestamp.valueOf(LocalDateTime.now()));
                return solution;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            try {
                Utilisateur user = new ServiceUtilisateur().recupererById(reclamation.getUtilisateur_id());
                String userEmail = user.getEmail();
                EmailService.sendEmail("smtp.office365.com", "wajdi.bouallegui@esprit.tn", "W@JDATAskills123", "wajdi.bouallegui@esprit.tn", userEmail, "Solution to your reclamation", "Your reclamation status is: " + statusComboBox.getValue());
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle SQL errors (e.g., user not found)
            }

            try {
                // Add solution to the database
                ServiceSolution.ajouter(result);
                // Update the status of the corresponding reclamation
                ServiceReclamation.updateStatus(reclamation.getReclamationID(), result.getStatus());

                showAlert("Success", "Solution added successfully.", Alert.AlertType.INFORMATION);
                loadReclamationData(); // Reload data to reflect the updated status
            } catch (SQLException e) {
                e.printStackTrace(); // Prints the SQL exception details to the console
                showAlert("Error", "Failed to add solution: " + e.getMessage(), Alert.AlertType.ERROR);
            } catch (Exception e) {
                e.printStackTrace(); // Prints generic exception details to the console
                showAlert("Error", "Failed to add solution: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    public void onReply(ActionEvent actionEvent) {
        Reclamation selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            showReplyDialog(selectedReclamation);
        } else {
            showAlert("No Selection", "Please select a Reclamation to reply.", Alert.AlertType.WARNING);
        }
    }

    public void viewSolution(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/guiReclamation/affichersolution.fxml"));
            Parent fxmlRoot = fxmlLoader.load();

            ajoutersolution1.getChildren().clear();

            affichersolutions.getChildren().add(fxmlRoot);

            // Toggle visibility
            ajoutersolution1.setVisible(false);
            affichersolutions.setVisible(true);


        } catch (IOException e) {
            e.printStackTrace();
            showAlert( "Eroo","Failed to load the FXML file",Alert.AlertType.ERROR );
        }


    }
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }}