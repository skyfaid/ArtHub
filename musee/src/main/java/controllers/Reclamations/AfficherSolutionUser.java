package controllers.Reclamations;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import entities.Solution;
import services.ServiceSolution;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class AfficherSolutionUser {



    @FXML
        private TableView<Solution> solutionsTableView;
        @FXML
        private TableColumn<Solution, Integer> oeuvreIdColumn;
        @FXML
        private TableColumn<Solution, String> statusColumn;

        @FXML
        private TableColumn<Solution, Float> refundAmountColumn;
        @FXML
        private TableColumn<Solution, String> adminFeedbackColumn;
        @FXML
        private TableColumn<Solution, Timestamp> dateResolvedColumn;

    private ServiceSolution serviceSolution = new ServiceSolution();

        @FXML
        public void initialize() {
            oeuvreIdColumn.setCellValueFactory(new PropertyValueFactory<>("oeuvreId"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            refundAmountColumn.setCellValueFactory(new PropertyValueFactory<>("refundAmount"));
            adminFeedbackColumn.setCellValueFactory(new PropertyValueFactory<>("adminFeedback"));
            dateResolvedColumn.setCellValueFactory(new PropertyValueFactory<>("dateResolved"));
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
            solutionsTableView.setItems(FXCollections.observableArrayList(solutions));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }

