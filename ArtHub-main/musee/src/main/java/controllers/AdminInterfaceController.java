package controllers;

import controllers.Users.UpdateAccountController;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import utils.UserUpdateListener;

public class AdminInterfaceController implements UserUpdateListener{

    public  ImageView imageprofile;
    public VBox salesView;
    public Label pseudouser;

    public Button imageButton;
    public VBox messengerView;

    @FXML
    private Button eventsButton;

    @FXML
    private Button activitiesButton;

    @FXML
    private Button formationsButton;

    @FXML
    private Button oeuvresButton;

    @FXML
    private Button usersButton;

    @FXML
    private Button articlesButton;

    @FXML
    private Button reclamationButton;

    @FXML
    private Button logoutButton;
    @FXML

    private Button homeButton;

    @FXML
    private Pane contentPane;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button closeButton;

    private double xOffset = 0;

    private double yOffset = 0;

    @FXML
    private AnchorPane root;

    @FXML
    private HBox titleBar;

    @FXML
    private VBox homeView,eventsView,usersView,articlesView,oeuvresView,activitesView,reclamationsView,formationsView,gestionView,VenteeView1;

    private Utilisateur utilisateurConnecte;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        Image image;
        // Check if the profile URL is not null, not empty, and the file exists
        if(utilisateur.getUrlImageProfil() != null && !utilisateur.getUrlImageProfil().isEmpty()) {
                // Load default image if the file doesn't exist
                image = new Image(getClass().getResourceAsStream(utilisateur.getUrlImageProfil()));
        } else {
            // Load default image if URL is null or empty
            image = new Image(getClass().getResourceAsStream("/images/nopic.png"));
        }

        // Set the image in the ImageView
        imageprofile.setImage(image);
        //pseudouser.setText(utilisateur.getPseudo());
        imageButton.setText(utilisateur.getPseudo());
    }
    public void initialize() {
        makeStageDraggable();
        closeButton.setOnAction(event -> closeStage());
        minimizeButton.setOnAction(event -> minimizeStage());
        onHomeButtonClick(null);

    }
    private void showView(VBox view) {
        contentPane.getChildren().forEach(child -> child.setVisible(false));
        view.setVisible(true);
    }
    private void makeStageDraggable() {
        titleBar.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        titleBar.setOnMouseDragged((MouseEvent event) -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
            stage.setOpacity(1);
        });
        titleBar.setOnMouseReleased((MouseEvent event) -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setOpacity(1.0);
        });
    }
    private void closeStage() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }
    private void minimizeStage() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void minimizeWindow() {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    protected void onHomeButtonClick(ActionEvent event) {
        showView(homeView);
        try {
            AnchorPane homePane = FXMLLoader.load(getClass().getResource("/guiUtilisateur/HomeAdmin.fxml"));
            contentPane.getChildren().setAll(homePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onEventsButtonClick(ActionEvent event) {
        showView(eventsView);

        try {
            // Load ListEventsBack.fxml
            AnchorPane listEventsPane = FXMLLoader.load(getClass().getResource("/guiEvent/ListEventsBack.fxml"));
            contentPane.getChildren().setAll(listEventsPane);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @FXML
    protected void onUsersButtonClick(ActionEvent event) {
        showView(usersView);
        try {
            AnchorPane pane_event = FXMLLoader.load(getClass().getResource("/guiUtilisateur/afficherUtilisateurs.fxml"));
            contentPane.getChildren().setAll(pane_event);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    protected void onActivitiesButtonClick(ActionEvent event) {
        showView(activitesView);
        try {
            AnchorPane pane_event = FXMLLoader.load(getClass().getResource("/guiActivite/AjouterActivite.fxml"));
           contentPane.getChildren().setAll(pane_event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onFormationsButtonClick(ActionEvent event) {
        showView(formationsView);
        try {
            AnchorPane pane_event = FXMLLoader.load(getClass().getResource("/guiFormation/Formation_Item.fxml"));
            contentPane.getChildren().setAll(pane_event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onOeuvresButtonClick(ActionEvent event) {
        showView(oeuvresView);
        try {

            Pane pane_Objectif= FXMLLoader.load(getClass().getResource("/guiOeuvre/Home.fxml"));
            contentPane.getChildren().setAll(pane_Objectif);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void onGestionButtonClick(ActionEvent event) {
        showView(gestionView);
        try {

            Pane pane_Objectif= FXMLLoader.load(getClass().getResource("/guiOeuvre/GestionOeuvre.fxml"));
            contentPane.getChildren().setAll(pane_Objectif);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void onVenteesButtonClick(ActionEvent event) {
        showView(VenteeView1);
        try {

            Pane pane_Objectif= FXMLLoader.load(getClass().getResource("/guiOeuvre/Admin.fxml"));
            contentPane.getChildren().setAll(pane_Objectif);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onArticlesButtonClick(ActionEvent event) {
        showView(articlesView);
    }
    @FXML
    protected void onReclamationButtonClick(ActionEvent event) {
        showView(reclamationsView);
        try {
            AnchorPane pane_event = FXMLLoader.load(getClass().getResource("/guiReclamation/ajoutersolution.fxml"));
            contentPane.getChildren().setAll(pane_event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onlogoutButtonClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiUtilisateur/Login.fxml"));
            Parent loginRoot = loader.load();
            Scene loginScene = new Scene(loginRoot);
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            Image icon = new Image("/images/logo.png");
            loginStage.getIcons().add(icon);
            loginStage.setScene(loginScene);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleImageButtonClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/guiUtilisateur/modfierProfile.fxml")); // Assurez-vous que le chemin est correct
            Parent modifierPageParent = loader.load();
            UpdateAccountController controller = loader.getController();
            controller.setData(utilisateurConnecte);
            controller.setUpdateListener(this);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            Image icon = new Image("/images/logo.png");
            stage.getIcons().add(icon);
            stage.setScene(new Scene(modifierPageParent));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUserUpdated(Utilisateur updatedUser) {
        this.utilisateurConnecte = updatedUser;
        // Now update the UI with new user details
        setUtilisateur(updatedUser);
    }

    public void onMessengerButtonClick(ActionEvent event) {
        showView(messengerView);
        try {
            AnchorPane pane_event = FXMLLoader.load(getClass().getResource("/guiReclamation/server.fxml"));
            contentPane.getChildren().setAll(pane_event);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void onVenteButtonClick(ActionEvent actionEvent) {
        showView(salesView);
        try {

            VBox pane_Objectif= FXMLLoader.load(getClass().getResource("/guiOeuvre/Oeuvres.fxml"));
            contentPane.getChildren().setAll(pane_Objectif);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
