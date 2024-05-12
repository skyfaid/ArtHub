package controllers.Events;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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


public class ListParticipantsController implements Initializable {

    public Button searchButton;
    public Button deletepart;
    @FXML
    private VBox layoutparticipants;

    @FXML
    private TableView<Participant> participantsTableView;

    @FXML
    private TextField searchTextField;

    private final ServiceParticipant service = new ServiceParticipant();
    private final ObservableList<Participant> allParticipants = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateParticipants();
        setupSearchListener();
    }

    private void populateParticipants() {
        List<Participant> participants = service.afficher();
        allParticipants.addAll(participants);
        participantsTableView.setItems(allParticipants);
    }

    private void setupSearchListener() {
        searchTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filterParticipants(newValue);
            }
        });
    }

    private void filterParticipants(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            participantsTableView.setItems(allParticipants);
        } else {
            ObservableList<Participant> filteredParticipants = FXCollections.observableArrayList();
            for (Participant participant : allParticipants) {
                if (participant.getNom().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredParticipants.add(participant);
                }
            }
            participantsTableView.setItems(filteredParticipants);
        }
    }

    @FXML
    void deleteparticipant(ActionEvent event) {
        Participant selectedParticipant = participantsTableView.getSelectionModel().getSelectedItem();
        if (selectedParticipant != null) {
            service.supprimer(selectedParticipant);
            allParticipants.remove(selectedParticipant);
            participantsTableView.refresh();
            // Decrement the participant count in the event
            service.decrementEventParticipantCount(selectedParticipant.getEvent_id());
            // Update UI or notify the event list controller to update the participant count
            // This could be achieved through an observer pattern or direct method call if accessible
            System.out.println("Participant deleted successfully");
        } else {
            System.out.println("Please select a participant to delete.");
        }
    }
    @FXML
    void generateParticipantsPDF(ActionEvent event) {
        ObservableList<Participant> participants = participantsTableView.getItems();
        Document document = new Document();
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("participants_list.pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
                Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

                document.add(new Paragraph("List of Participants", titleFont));
                document.add(Chunk.NEWLINE);

                PdfPTable table = new PdfPTable(new float[]{3, 3, 3});
                table.setWidthPercentage(100);

                table.addCell(new Phrase("Name", titleFont));
                table.addCell(new Phrase("Events Participated", titleFont));
                table.addCell(new Phrase("Event Name", titleFont));

                for (Participant participant : participants) {
                    int numberOfEvents = service.getNumberOfEventsParticipated(participant.getParticipant_id());
                    String eventName = participant.getEventName(); // Corrected to use Participant's getEventName
                    table.addCell(new Phrase(participant.getNom() + " " + participant.getPrenom(), normalFont));
                    table.addCell(new Phrase(String.valueOf(numberOfEvents), normalFont));
                    table.addCell(new Phrase(eventName, normalFont));
                }

                document.add(table);
                document.close();

                System.out.println("PDF file generated successfully.");
            }
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

}




