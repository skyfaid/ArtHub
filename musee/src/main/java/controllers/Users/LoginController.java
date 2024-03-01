package controllers.Users;

import controllers.AdminInterfaceController;
import controllers.UserInterfaceController;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.ServiceUtilisateur;
import utils.SessionManager; // Add the import for SessionManager

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private AnchorPane root;

    @FXML
    private HBox titleBar;

    @FXML
    private Button closeButton;

    @FXML
    private ImageView imagelogo;

    @FXML
    private ImageView loginButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private PasswordField mpField;

    @FXML
    private TextField pseudoField;

    private final ServiceUtilisateur ser = new ServiceUtilisateur();

    private double xOffset = 0;

    private double yOffset = 0;

    public void initialize() {
        makeStageDraggable();
        closeButton.setOnAction(event -> closeStage());
        minimizeButton.setOnAction(event -> minimizeStage());
        EventHandler<KeyEvent> enterPressedHandler = event -> {
            if (event.getCode() == KeyCode.ENTER) {
                connecter(new ActionEvent()); // Simulate a click on the connection button
            }
        };
        mpField.setOnKeyPressed(enterPressedHandler);
        pseudoField.setOnKeyPressed(enterPressedHandler);
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
    void connecter(ActionEvent event) {
        try {
            String identifiant = pseudoField.getText();
            String motDePasse = mpField.getText();
            if (identifiant.isEmpty() || motDePasse.isEmpty()) {
                showAlert("Champs manquants", "Veuillez remplir tous les champs.", Alert.AlertType.WARNING);
                return;
            }
            Utilisateur utilisateur = ser.authentifierUtilisateur(identifiant, motDePasse);
            if (utilisateur != null && utilisateur.isEstActif()) {
                // Setting the current user ID in SessionManager
                SessionManager.getInstance().setCurrentUserId(utilisateur.getUtilisateurId());

                Stage currentStage = (Stage) mpField.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                String fxmlPath = "/AdminInterface.fxml"; // Default path
                if ("admin".equals(utilisateur.getRole())) {
                    fxmlPath = "/AdminInterface.fxml"; // Path to the admin interface
                } else {
                    fxmlPath = "/UserInterface.fxml"; // Path to the standard user interface
                }
                loader.setLocation(getClass().getResource(fxmlPath));
                Parent root = loader.load();

                // Setting up the controller with user data
                if (loader.getController() instanceof AdminInterfaceController) {
                    ((AdminInterfaceController) loader.getController()).setUtilisateur(utilisateur);
                } else if (loader.getController() instanceof UserInterfaceController) {
                    ((UserInterfaceController) loader.getController()).setUtilisateur(utilisateur);
                }

                Scene scene = new Scene(root);
                Stage newStage = new Stage();
                newStage.initStyle(StageStyle.UNDECORATED);
                Image icon = new Image("/images/logo.png");
                newStage.getIcons().add(icon);
                newStage.setScene(scene);
                newStage.show();
                currentStage.close();
            } else if (utilisateur != null && !utilisateur.isEstActif()) {
                showAlert("Compte inactif", "Votre compte est inactif. Veuillez contacter l'administrateur.", Alert.AlertType.ERROR);
            } else {
                showAlert("Erreur", "Identifiants incorrects", Alert.AlertType.ERROR);
            }
        } catch (SQLException | IOException e) {
            showAlert("Erreur de connexion", "Une erreur est survenue lors de la connexion : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void opensignup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiUtilisateur/signup.fxml")); // Update the path
            Parent root = loader.load();
            Stage signUpStage = new Stage();
            Image icon = new Image("/images/logo.png");
            signUpStage.getIcons().add(icon);
            signUpStage.initStyle(StageStyle.UNDECORATED);
            signUpStage.setScene(new Scene(root));
            signUpStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open the sign-up form: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void startFacialRecognition(ActionEvent actionEvent) {
    }
}
