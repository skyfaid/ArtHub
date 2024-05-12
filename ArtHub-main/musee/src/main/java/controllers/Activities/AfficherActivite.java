package controllers.Activities;


import com.itextpdf.text.pdf.PdfPCell;
import entities.Participation;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import services.ServiceParticipation;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entities.Participant;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceParticipant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
public class AfficherActivite {
    public VBox Ton;
    @FXML
    private TableView<Participation> participationTableView;
    @FXML
    private TableColumn<Participation, Integer> Id_participationColumn;
    @FXML
    private TableColumn<Participation, Integer> id_activiteColumn;
    @FXML
    private TableColumn<Participation, Integer> utilisateur_idColumn;
    @FXML
    private TableColumn<Participation, Integer> ScoreColumn;
    @FXML
    private TableColumn<Participation, Date> Participation_dateColumn;
    @FXML
    public VBox Test;
    @FXML
    private AnchorPane contentPane;

    @FXML
    private TextField searchTextField ;
    @FXML
    private Button searchButton ;


    // Reference to the service class
    private  ServiceParticipation serviceParticipation = new ServiceParticipation() {
    };

    @FXML
    protected void handleShowParticipations() {
        participationTableView.setVisible(true);
    }
    /*
        private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            alert.setContentText(contentText);
            alert.showAndWait();
        }
    */
    @FXML
    private void initialize() {
        // Initialize your service class

        // Set up the columns to use the Participation property names
        Id_participationColumn.setCellValueFactory(new PropertyValueFactory<>("id_participation"));
        id_activiteColumn.setCellValueFactory(new PropertyValueFactory<>("id_activite"));
        utilisateur_idColumn.setCellValueFactory(new PropertyValueFactory<>("utilisateur_id"));
        ScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        Participation_dateColumn.setCellValueFactory(new PropertyValueFactory<>("participation_date"));

        // Load and display the list of participations
        loadParticipationsData();
    }
/*
        private void loadParticipationsData() {
            try {
                ObservableList<Participation> participations = FXCollections.observableArrayList(serviceParticipation.recuperer());
                participationTableView.setItems(participations);
            } catch (SQLException e) {
                e.printStackTrace(); // Or handle the error as appropriate for your application
            }
        }
 */




    @FXML
    void handleSearchScore(ActionEvent event) {
        String searchText = searchTextField.getText();
        filterParticipationsByScore(searchText);
    }

    private void filterParticipationsByScore(String searchText) {
        try {
            int score = Integer.parseInt(searchText);
            List<Participation> filteredParticipations = serviceParticipation.searchByScore(score);
            participationTableView.getItems().clear();
            participationTableView.getItems().addAll(filteredParticipations);
        } catch (NumberFormatException e) {
            // Handle the case where searchText is not a valid integer
            e.printStackTrace();
        } catch (SQLException e) {
            // Handle SQL exception
            e.printStackTrace();
        }
    }
    @FXML
    void generateParticipationPDF(ActionEvent event) {
        ObservableList<Participation> participations = participationTableView.getItems();
        Document document = new Document();
        try {
            // FileChooser to select the location to save the PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("participation_list.pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.DARK_GRAY);
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.WHITE);
                Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
                Paragraph title = new Paragraph("List of Participations", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);
                PdfPTable table = new PdfPTable(new float[]{2, 3, 2}); // Columns for ID, Activities Participated, and Score
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                // Set header cells with styling
                PdfPCell cell = new PdfPCell(new Phrase("ID", headerFont));
                cell.setBackgroundColor(BaseColor.BLUE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Activities Participated", headerFont));
                cell.setBackgroundColor(BaseColor.BLUE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Score", headerFont));
                cell.setBackgroundColor(BaseColor.BLUE);
                table.addCell(cell);
                // Loop through each participation and add details to the PDF
                for (Participation participation : participations) {
                    table.addCell(new Phrase(String.valueOf(participation.getId_participation()), normalFont));
                    table.addCell(new Phrase(String.valueOf(participation.getId_activite()), normalFont));
                    table.addCell(new Phrase(String.valueOf(participation.getScore()), normalFont));
                }
                document.add(table);
                document.close();
                System.out.println("PDF file generated successfully.");
                // Send email with the generated PDF attachment if required
            }
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }



    // Method to send email with PDF attachment
    void sendEmailWithAttachment(File attachment) {
        // Implement email sending logic here
    }




    public void handleTopScoresButtonClick(ActionEvent actionEvent) {
        try {
            List<Participation> top3Participations = serviceParticipation.recuperera(3);
            participationTableView.getItems().clear();
            participationTableView.getItems().addAll(top3Participations);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately.
        }
    }

    private void loadParticipationsData() {
        try {
            List<Participation> participations = serviceParticipation.recuperer(); // This method should return a list of all activities.
            participationTableView.setItems(FXCollections.observableArrayList(participations));
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately.
        }
    }

    public void handleResetButtonClick(ActionEvent actionEvent) {
        loadParticipationsData();
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        Test.setVisible(true);
        Ton.setVisible(false);
        try {
            AnchorPane pane_act = FXMLLoader.load(getClass().getResource("/guiActivite/AjouterActivite.fxml"));
            Test.getChildren().add(pane_act);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void showView(VBox view) {
        contentPane.getChildren().forEach(child -> child.setVisible(false));
        view.setVisible(true);
    }


    // Other methods for your controller...

}