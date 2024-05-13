package controllers.Formations;

import controllers.AdminInterfaceController;
import entities.Formations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.FormationService;
import utils.UserConnected;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AjouterFormation {

    @FXML
    private TableView<Formations> formationsTableView;
    @FXML
    private TableColumn<Formations, String> DateD_Col;

    @FXML
    private TableColumn<Formations, String> DateF_Col;

    @FXML
    private DatePicker Date_D_TF;

    @FXML
    private DatePicker Date_F_TF;

    @FXML
    private TableColumn<Formations,String> LienCol;

    @FXML
    private TextField LienTF;

    @FXML
    private TableColumn<Formations,Integer> NbParticipantCol;

    @FXML
    private TextField Nbr_ParticipantTF;
    @FXML
    private ImageView imageView;
    @FXML
    private TableColumn<Formations,Integer> idCol;

    LocalDate selected_D;
    LocalDate selected_F;

    public TableView<Formations> getFormationsTableView() {
        return formationsTableView;
    }

    public DatePicker getDate_D_TF() {
        return Date_D_TF;
    }

    public DatePicker getDate_F_TF() {
        return Date_F_TF;
    }

    public TextField getLienTF() {
        return LienTF;
    }

    public TextField getNbr_ParticipantTF() {
        return Nbr_ParticipantTF;
    }

    public LocalDate getSelected_D() {
        return selected_D;
    }

    public LocalDate getSelected_F() {
        return selected_F;
    }


    FormationService FormationService= new FormationService();
    @FXML
    void Ajouter(ActionEvent event) {
        selected_D=Date_D_TF.getValue();
        selected_F=Date_F_TF.getValue();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FormationService FormationService= new FormationService();
        String Date_F=selected_F.format(formatter);
        String Date_D=selected_D.format(formatter);
        if(selected_D.isBefore(selected_F)){
        try {
            FormationService.ajouter(new Formations(Integer.parseInt(Nbr_ParticipantTF.getText()),LienTF.getText(),Date_F,Date_D));
            /*Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Error");
            a.setContentText("");
            a.showAndWait();*/

        }
        catch (SQLException e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setContentText(e.getMessage());
            a.showAndWait();

        }}else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.showAndWait();
        }
    }

    @FXML
    void supprimer(ActionEvent event) {

        Formations selectedFormation = formationsTableView.getSelectionModel().getSelectedItem();
        if (selectedFormation != null) {
            // Remove from the observable list
            formationsTableView.getItems().remove(selectedFormation);

            // Remove from database (if applicable)
            FormationService formationService = new FormationService();
            try {
                formationService.supprimer(selectedFormation.getId()); // Assuming there's a method to remove the formation
                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Formation has been successfully removed.");
                alert.showAndWait();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminInterface.fxml"));
                    Parent root = loader.load(); // Load the FXML

                    // Get the controller and set the current user
                    AdminInterfaceController ctrl = loader.getController();
                    ctrl.setUtilisateur(UserConnected.getUser());
                    LienTF.getScene().setRoot(root);
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            } catch (SQLException e) {
                // Show error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while removing the formation: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a formation to remove.");
            alert.showAndWait();
        }

    }

    private void updateForm(Formations formation) {
        Nbr_ParticipantTF.setText(String.valueOf(formation.getNbr_participants()));
        LienTF.setText(formation.getLien());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Date_D_TF.setValue(LocalDate.parse(formation.getDate_debut(),formatter));
        Date_F_TF.setValue(LocalDate.parse(formation.getDate_fin(),formatter));
    }
    /*@FXML
    void initialize(){

          formationsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    updateForm(newSelection);
                }
          });
        try {

            List<Formations> formations= FormationService.recuperer();
            ObservableList<Formations> observableList= FXCollections.observableList(formations);
            formationsTableView.setItems(observableList);
            idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
            DateD_Col.setCellValueFactory(new PropertyValueFactory<>("date_debut"));
            DateF_Col.setCellValueFactory(new PropertyValueFactory<>("date_fin"));
            LienCol.setCellValueFactory(new PropertyValueFactory<>("Lien"));
            NbParticipantCol.setCellValueFactory(new PropertyValueFactory<>("nbr_participants"));
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }*/



    @FXML
    public void Modifier(ActionEvent actionEvent) {
        selected_D=Date_D_TF.getValue();
        selected_F=Date_F_TF.getValue();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FormationService FormationService= new FormationService();
        String Date_F=selected_F.format(formatter);
        String Date_D=selected_D.format(formatter);
        Formations f =new Formations(Integer.parseInt(Nbr_ParticipantTF.getText()),LienTF.getText(),Date_F,Date_D);

        if(selected_D.isBefore(selected_F)){
            try {
                List<Formations> formations= FormationService.recuperer();
                    FormationService.modifier(f);
            /*Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Error");
            a.setContentText("");
            a.showAndWait();*/

                    Stage currentStage = (Stage) Nbr_ParticipantTF.getScene().getWindow();
                    currentStage.close();
                  /*  FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminInterface.fxml"));
                    Parent root = loader.load(); // Load the FXML

                    // Get the controller and set the current user
                    AdminInterfaceController ctrl = loader.getController();
                    ctrl.setUtilisateur(UserConnected.getUser());
                    Nbr_ParticipantTF.getScene().setRoot(root);*/

            }
            catch (SQLException e){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setContentText(e.getMessage());
                a.showAndWait();

            }
        }
             else
            {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.showAndWait();
             }

    }
    }


