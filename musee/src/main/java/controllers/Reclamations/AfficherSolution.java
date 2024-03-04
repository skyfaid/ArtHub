package controllers.Reclamations;

import entities.Solution;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import services.ServiceReclamation;
import services.ServiceSolution;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class AfficherSolution {




    @FXML
    private TableView<Solution> solutionTableView;
    @FXML
    private TableColumn<Solution, Integer> solutionIDColumn;
    @FXML
    private TableColumn<Solution, Integer> reclamationIDColumn;
    @FXML
    private TableColumn<Solution, Integer> utilisateurIDColumn;
    @FXML
    private TableColumn<Solution, String> statusColumn;
    @FXML
    private TableColumn<Solution, Float> refundAmountColumn;
    @FXML
    private TableColumn<Solution, String> adminFeedbackColumn;
    @FXML
    private TableColumn<Solution, Timestamp> dateResolvedColumn;

        private ServiceSolution serviceSolution = new ServiceSolution();
    private ServiceReclamation serviceReclamation = new ServiceReclamation();

    @FXML
    public void initialize() {
        solutionIDColumn.setCellValueFactory(new PropertyValueFactory<>("SolutionID"));
        reclamationIDColumn.setCellValueFactory(new PropertyValueFactory<>("ReclamationID"));
        utilisateurIDColumn.setCellValueFactory(new PropertyValueFactory<>("utilisateur_id"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("Status"));
        refundAmountColumn.setCellValueFactory(new PropertyValueFactory<>("RefundAmount"));
        adminFeedbackColumn.setCellValueFactory(new PropertyValueFactory<>("AdminFeedback"));

        dateResolvedColumn.setCellValueFactory(new PropertyValueFactory<>("DateResolved"));
        dateResolvedColumn.setCellFactory(column -> new TableCell<Solution, Timestamp>() {
            @Override
            protected void updateItem(Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
            }
        });

        loadSolutions();
    }


    private void loadSolutions() {
        try {
            List<Solution> solutions = serviceSolution.getAllSolutions();
            System.out.println("Number of solutions fetched: " + solutions.size()); // Debug print
            solutionTableView.setItems(FXCollections.observableArrayList(solutions));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void refreshSolutions() {
        loadSolutions();
    }

    public void modifySolution(ActionEvent actionEvent) {
        Solution selectedSolution = solutionTableView.getSelectionModel().getSelectedItem();
        if (selectedSolution == null) {
            showAlert("Modify Solution", "No solution selected!", Alert.AlertType.WARNING);
            return;
        }

        Dialog<Solution> dialog = new Dialog<>();
        dialog.setTitle("Modify Solution");
        dialog.setHeaderText("Modify the selected solution");

        ButtonType modifyButtonType = new ButtonType("Modify", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Accepted", "Declined", "Pending");
        statusComboBox.setValue(selectedSolution.getStatus());

        TextField refundAmountField = new TextField(String.valueOf(selectedSolution.getRefundAmount()));
        TextField adminFeedbackField = new TextField(selectedSolution.getAdminFeedback());

        grid.add(new Label("Status:"), 0, 0);
        grid.add(statusComboBox, 1, 0);
        grid.add(new Label("Refund Amount:"), 0, 1);
        grid.add(refundAmountField, 1, 1);
        grid.add(new Label("Admin Feedback:"), 0, 2);
        grid.add(adminFeedbackField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                selectedSolution.setStatus(statusComboBox.getValue());
                selectedSolution.setRefundAmount(Float.parseFloat(refundAmountField.getText()));
                selectedSolution.setAdminFeedback(adminFeedbackField.getText());
                return selectedSolution;
            }
            return null;
        });

        Optional<Solution> result = dialog.showAndWait();

        result.ifPresent(solution -> {
            try {
                serviceSolution.modifier(solution); // This is correct; assuming it updates the solution in your database.
                // Now also update the corresponding reclamation status
                serviceReclamation.updateStatus(solution.getReclamationID(), solution.getStatus()); // This line was corrected
                loadSolutions(); // Reload solution data.
                refreshSolutions(); // Refresh the solutions in the UI, if this is different from loadSolutions.
                // If you have a method to refresh the reclamations list, call it here to reflect the status update.
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Database Error", "Failed to modify the solution: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }




    public void deleteSolution(ActionEvent actionEvent) {

        Solution selectedSolution = solutionTableView.getSelectionModel().getSelectedItem();

        if (selectedSolution == null) {
            showAlert("Delete Solution", "No solution selected!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this solution?", ButtonType.YES, ButtonType.NO);
        confirmAlert.setTitle("Confirm Deletion");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                serviceSolution.supprimer(selectedSolution.getSolutionID());

                loadSolutions();

                showAlert("Delete Solution", "Solution successfully deleted.", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Delete Solution", "Failed to delete solution: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }


private void showAlert(String title, String header, Alert.AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.showAndWait();
}
}

