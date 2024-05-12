package controllers.Reclamations;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import entities.Solution;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import services.ServiceSolution;

import java.io.File;
import java.io.IOException;
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
    @FXML
    void imprimer(ActionEvent event)
    {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            writeInvoiceDataToPDF(document);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer la facture");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));

            Window window = solutionsTableView.getScene().getWindow();
            File file = fileChooser.showSaveDialog(window);

            if (file != null) {
                document.save(file);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Impression PDF");
                alert.setHeaderText(null);
                alert.setContentText("La facture a été enregistrée dans un fichier PDF.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de la génération du fichier PDF");
            alert.setContentText("Une erreur s'est produite lors de la création du fichier PDF.");
            alert.showAndWait();
        }
    }


    private void writeInvoiceDataToPDF(PDDocument document) throws IOException {
        PDPage page = document.getPage(0);
        PDRectangle pageSize = page.getMediaBox();
        float width = pageSize.getWidth();
        float startY = pageSize.getHeight() - 30; // Start a bit down from the top
        float startX = 70; // Margin from the left

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // Set a vibrant color for the "Solution" title
            contentStream.setNonStrokingColor(29, 233, 182); // #1DE9B6 color
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contentStream.beginText();
            contentStream.newLineAtOffset((width / 2) - 50, startY); // Center the title
            contentStream.showText("Solution");
            contentStream.endText();

            // Set a matching color for "Solution Details" subtitle
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, startY - 40); // Continue a bit down for the subtitle
            contentStream.showText("Solution Details");
            contentStream.endText();

            // Table Headers and Details
            String[] headers = {"Status", "Refund Amount", "Admin Feedback", "Date Resolved"};
            float yPosition = startY - 60; // Start position for the table
            float tableWidth = width - startX * 2; // Use the width of the page minus margins
            float cellMargin = 5f;
            float cellHeight = 20f;
            float cellWidth = tableWidth / headers.length;

            // Drawing Table and Headers
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            for (int i = 0; i < headers.length; i++) {
                if (!headers[i].equals("Status")) { // Skip background for "Status"
                    contentStream.setNonStrokingColor(33, 150, 243); // Deep sky blue for header background
                    contentStream.addRect(startX + i * cellWidth, yPosition, cellWidth, cellHeight);
                    contentStream.fill();
                }
                // Draw header text
                contentStream.beginText();
                contentStream.setNonStrokingColor(0, 0, 0); // Black text
                contentStream.newLineAtOffset(startX + i * cellWidth + cellMargin, yPosition + cellMargin);
                contentStream.showText(headers[i]);
                contentStream.endText();
            }

            // Drawing the rows
            ObservableList<Solution> solutionList = FXCollections.observableArrayList(serviceSolution.getAllSolutions());
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.setNonStrokingColor(0, 0, 0); // Black for regular text
            for (Solution solution : solutionList) {
                yPosition -= cellHeight; // Move to the next row position
                String[] details = {solution.getStatus(), String.valueOf(solution.getRefundAmount()), solution.getAdminFeedback(), solution.getDateResolved().toString()};
                for (int i = 0; i < details.length; i++) {
                    // Write row details
                    contentStream.beginText();
                    contentStream.newLineAtOffset(startX + i * cellWidth + cellMargin, yPosition + cellMargin);
                    contentStream.showText(details[i]);
                    contentStream.endText();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while fetching solution details from database.", e);
        }
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

