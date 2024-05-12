package controllers.Oeuvres;

import entities.Oeuvre;
import entities.vente;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import services.IVente;
import services.VenteService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class sucess implements Initializable {
    @FXML
    private Pane pane_Fail = new Pane();

    @FXML
    private Pane pane_Success = new Pane();

    @FXML
    private Label successFX;

    @FXML
    void getrecepeitpayment(ActionEvent event) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Écrire data de la facture dans fichier PDF
            getReceiptPdf(document);

            // Afficher dossier ou je vais select le dossier ou mettre mon pdf
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer la facture");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));

            // Aff path du dossu=ier ou je vais mettre mon pdf
            Window window = pane_Success.getScene().getWindow();
            File file = fileChooser.showSaveDialog(window);

            if (file != null) {
                // Sauvegarder le fichier PDF au path select
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



        private void getReceiptPdf(PDDocument document) throws IOException {
            VenteService venteService = new VenteService();
            List<vente> ventes;


            try  {
                ventes = venteService.recupererToutesVentes();

            } catch (SQLException e) {
                e.printStackTrace();
                throw new IOException("Erreur lors de la récupération des données de vente.");
            }

            vente derniereVente = ventes.isEmpty() ? null : ventes.get(ventes.size() - 1);

            if (derniereVente == null) {
                throw new IOException("Aucune vente disponible pour générer un reçu.");
            }
            if (document.getNumberOfPages() == 0) {
                PDPage page = new PDPage();
                document.addPage(page);
            }
             // Remplacez getOeuvre par votre méthode réelle



            // Utilisez la première page du document pour écrire le contenu
            PDPage firstPage = document.getPage(0);
//            PDPage page = new PDPage();
//            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, firstPage, PDPageContentStream.AppendMode.APPEND, true, true)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.setLeading(20f);
                contentStream.newLineAtOffset(100, 750);
                contentStream.showText("Reçu de Vente");
                contentStream.newLine();

                // Afficher les détails de la vente
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.showText("ID de la vente: " + derniereVente.getID());
                contentStream.newLine();
                contentStream.showText("Date de la vente: " + derniereVente.getDateVente());
                contentStream.newLine();
                contentStream.showText("Total de la vente: " + derniereVente.getPrixVente());
                contentStream.newLine();
                contentStream.showText("Mode de paiement: " + derniereVente.getModePaiement());
                contentStream.newLine();
                contentStream.showText("Quantité vendue: " + derniereVente.getQuantite());
                contentStream.endText();

            }
    }

    public void redirect_to_successPage() {
        pane_Fail.setVisible(false);
        pane_Success.setVisible(true);
    }

    public void redirect_to_FailPage() {
        pane_Success.setVisible(false);
        pane_Fail.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}