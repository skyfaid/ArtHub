package controllers;

import controllers.Activities.UserActivity;
import controllers.Activities.WelcomeActivityController;
import controllers.Articles.ArticlesController;
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
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.UserUpdateListener;

import java.io.IOException;

public class UserInterfaceController implements UserUpdateListener {

    public ImageView imageprofile;

    public Label pseudouser;

    @FXML
    private Button eventsButton;

    @FXML
    private Button activitiesButton;

    @FXML
    private Button formationsButton;

    @FXML
    private Button oeuvresButton;

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

    public Button imageButton;

    private double xOffset = 0;

    private double yOffset = 0;

    @FXML
    private AnchorPane root;

    @FXML
    private HBox titleBar;

    @FXML
    private VBox homeView,eventsView,articlesView,oeuvresView,activitesView,reclamationsView,formationsView,MessangerView;

    private Utilisateur utilisateurConnecte;


    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        Image image;
        if(utilisateur.getUrlImageProfil() != null && !utilisateur.getUrlImageProfil().isEmpty()) {

            image = new Image(getClass().getResourceAsStream(utilisateur.getUrlImageProfil()));
        } else {

            image = new Image(getClass().getResourceAsStream("/images/nopic.png"));
        }
        imageprofile.setImage(image);
        imageButton.setText(utilisateur.getPseudo());
    }
    public void initialize() {
        makeStageDraggable();
        closeButton.setOnAction(event -> closeStage());
        minimizeButton.setOnAction(event -> minimizeStage());
        showView(homeView);
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
    }
    @FXML
    protected void onEventsButtonClick(ActionEvent event) {
        showView(eventsView);
        try {
            // Load ListEventsBack.fxml
            AnchorPane listEventsPane = FXMLLoader.load(getClass().getResource("/guiEvent/ListEventsFront.fxml"));

            // Clear existing content in the target view (optional, based on your needs)
            //eventsView.getChildren().clear();

            // Add the loaded pane to the eventsView VBox
            contentPane.getChildren().setAll(listEventsPane);
            // Make sure the eventsView is visible and set as the content to be displayed

            // Assuming showView method makes the view visible within the contentPane.
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    protected void onActivitiesButtonClick(ActionEvent event) {
        showView(activitesView);
        try {
            //Pane pane_event = FXMLLoader.load(getClass().getResource("/guiActivite/UserActivity.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiActivite/UserActivity.fxml"));
            AnchorPane pane_event = loader.load();
            UserActivity controller = loader.getController();
            controller.setActivityUser(utilisateurConnecte);
            contentPane.getChildren().setAll(pane_event);
             // Assuming showView method makes the view visible within the contentPane.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onFormationsButtonClick(ActionEvent event) {
        showView(formationsView);
    }

    @FXML
    protected void onOeuvresButtonClick(ActionEvent event) {
        showView(oeuvresView);
    }

    @FXML
    protected void onArticlesButtonClick(ActionEvent event) {
        showView(articlesView);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiArticle/Articles.fxml"));
            AnchorPane pane_event = loader.load();
            ArticlesController controller = loader.getController();
            controller.setUser(utilisateurConnecte);
            contentPane.getChildren().setAll(pane_event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onReclamationButtonClick(ActionEvent event) {
        showView(reclamationsView);
        try {
            AnchorPane pane_event = FXMLLoader.load(getClass().getResource("/guiReclamation/ajouterreclamation.fxml"));
            contentPane.getChildren().setAll(pane_event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onlogoutButtonClick(ActionEvent actionEvent) {
        Stage currentStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiUtilisateur/Login.fxml")); // Update the path to your FXML file
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

    public void onchatButtonClick(ActionEvent event) {

        showView(MessangerView);
        try {
            AnchorPane pane_event = FXMLLoader.load(getClass().getResource("/guiReclamation/client.fxml"));
            contentPane.getChildren().setAll(pane_event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
