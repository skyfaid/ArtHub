package controllers.Users;

import entities.Utilisateur;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.ServiceUtilisateur;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Collectors;

import utils.UserUpdateListener;
import utils.ValidationUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Window;


public class UpdateAccountController {
    @FXML
    private AnchorPane root;

    @FXML
    private HBox titleBar;

    @FXML
    private Button closeButton;
    @FXML
    private Button minimizeButton;

    @FXML
    private PasswordField confpsField;

    @FXML
    private TextField emailField;

    @FXML
    private Button imageButton;

    @FXML
    private ImageView imageprofile;

    @FXML
    private Button modifierButton;

    @FXML
    private PasswordField newpsField;

    @FXML
    private TextField nomField;

    @FXML
    private PasswordField oldpsField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField pseudoField;
    private Utilisateur utilisateur;
    private final ServiceUtilisateur serviceUtilisateur=new ServiceUtilisateur();
    private double xOffset = 0;

    private double yOffset = 0;

    private UserUpdateListener updateListener;

    public void setUpdateListener(UserUpdateListener listener) {
        this.updateListener = listener;
    }


    public void initialize() {
        makeStageDraggable();
        closeButton.setOnAction(event -> closeStage());
        minimizeButton.setOnAction(event -> minimizeStage());
        setFieldEditableOnDoubleClick(nomField);
        setFieldEditableOnDoubleClick(prenomField);
        setFieldEditableOnDoubleClick(emailField);
        setFieldEditableOnDoubleClick(pseudoField);
        setFieldEditableOnDoubleClick(oldpsField);
        setFieldEditableOnDoubleClick(newpsField);
        setFieldEditableOnDoubleClick(confpsField);

        // Initialiser vos autres composants ici
    }

    private void setFieldEditableOnDoubleClick(TextField textField) {
        textField.setEditable(false); // Rendre le champ non éditable par défaut
        textField.setOnMouseClicked((MouseEvent event) -> {
            if(event.getClickCount() == 2) {
                textField.setEditable(true); // Rendre le champ éditable après un double-clic
            }
        });
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

    public void setData(Utilisateur u) {
        this.utilisateur = u;
        Image image;
        if(utilisateur.getUrlImageProfil() != null && !utilisateur.getUrlImageProfil().isEmpty()) {
            // Load default image if the file doesn't exist
            image = new Image(getClass().getResourceAsStream(utilisateur.getUrlImageProfil()));
        } else {
            // Load default image if URL is null or empty
            image = new Image(getClass().getResourceAsStream("/images/nopic.png"));
        }
        imageprofile.setImage(image);
        nomField.setText(u.getNom());
        prenomField.setText(u.getPrenom());
        emailField.setText(u.getEmail());
        pseudoField.setText(u.getPseudo());
    }

    @FXML
    void ajouterImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image de profil");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers image", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                // Copy the file to the /images/ folder and get the new relative path
                String newImagePath = copyFileToImagesFolder(file);
                Image image = new Image(getClass().getResourceAsStream(newImagePath));
                imageprofile.setImage(image);
                // Update the profile image path in the database

               /* if (updateListener != null) {
                    updateListener.onUserUpdated(utilisateur); // Notify the main window
                }*/
                // Update the user object
                utilisateur.setUrlImageProfil(newImagePath);
                //showAlert("Image ajoutée", "L'image de profil a été mise à jour avec succès.", Alert.AlertType.INFORMATION);

            } catch (Exception e) {
                showAlert("Erreur", "Une erreur est survenue lors de la mise à jour de l'image de profil: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Aucune image sélectionnée", "Veuillez choisir une image.", Alert.AlertType.WARNING);
        }
    }

    /**
     * Copies the selected image file to the /images/ folder and returns the new relative path.
     * Implement this method to suit your application's file handling logic.
     */
    private String copyFileToImagesFolder(File sourceFile) throws IOException {
        // Define the target directory and construct the new file path
        File destDir = new File("C:\\Users\\arij\\Desktop\\backup\\ArtHub-main\\musee\\src\\main\\resources\\images");
        File destFile = new File(destDir, sourceFile.getName());
        // Copy file to the new location (This is just an example method call)
        Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        // Return the relative path as a String
        return "/images/" + sourceFile.getName();
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void modifier(ActionEvent event) {
        try {
            String email = emailField.getText();
            String oldPassword = oldpsField.getText();
            String newPassword = newpsField.getText();
            String confirmPassword = confpsField.getText();
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String pseudo = pseudoField.getText();

            // Validate fields
            if (!ValidationUtils.isValidEmail(email)) {
                showAlert("Validation Error", "Invalid email format.", Alert.AlertType.ERROR);
                return;
            }
            if (!ValidationUtils.isValidName(nom)) {
                showAlert("Validation Error", "Invalid first name. Please enter a valid name.",Alert.AlertType.ERROR);
                return;
            }
            if (!ValidationUtils.isValidName(prenom)) {
                showAlert("Validation Error", "Invalid last name. Please enter a valid name.",Alert.AlertType.ERROR);
                return;
            }
            if (!ValidationUtils.isValidPseudo(pseudo)) {
                showAlert("Validation Error", "Invalid pseudo.",Alert.AlertType.ERROR);
                return;
            }
            if ( !newPassword.equals(confirmPassword)) {
                showAlert("Validation Error", "Passwords do not match.",Alert.AlertType.ERROR);
                return;
            }

            if (serviceUtilisateur.verifyPassword(utilisateur.getUtilisateurId(), oldPassword)) {
                if (!newPassword.trim().isEmpty()) {
                    utilisateur.setMotDePasseHash(newPassword); // Update the password if new password is provided
                }
                else{
                    utilisateur.setMotDePasseHash(oldPassword);
                }
                utilisateur.setEmail(email);
                utilisateur.setNom(nom);
                utilisateur.setPrenom(prenom);
                utilisateur.setPseudo(pseudo);
                utilisateur.setMotDePasseHash(oldPassword);
                serviceUtilisateur.modifier(utilisateur); // Update the user in the database
                showAlert("Success", "User information updated successfully.",Alert.AlertType.INFORMATION);
                if (updateListener != null) {
                    updateListener.onUserUpdated(utilisateur); // Notify the main window
                }
                Stage currentStage = (Stage) root.getScene().getWindow();
                currentStage.close();

            } else {
                showAlert("Validation Error", " verify Old password .",Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to update user information: " + e.getMessage(),Alert.AlertType.ERROR);
        }
    }

    public void delete(ActionEvent actionEvent) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer votre compte ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent()  && result.get() == ButtonType.YES) {
            try {
                serviceUtilisateur.supprimer(utilisateur.getUtilisateurId());
                // Fermez toutes les fenêtres ouvertes
                Platform.runLater(() -> {
                    Stage stage = (Stage) root.getScene().getWindow();
                    stage.close(); // Ferme la fenêtre actuelle

                    // Fermer toutes les fenêtres ouvertes (sauf pour la principale qui va afficher la page de connexion)

                    for (Stage openStage : Stage.getWindows().stream().filter(Window::isShowing).map(window -> (Stage) window).collect(Collectors.toList())) {
                            openStage.close();

                    }

                    // Ouvrir la fenêtre de connexion
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiUtilisateur/Login.fxml")); // Mettez le chemin correct vers votre fichier FXML de connexion
                        Parent root = loader.load();
                        Stage loginStage = new Stage();
                        loginStage.initStyle(StageStyle.UNDECORATED);
                        Image icon = new Image("/images/logo.png");
                        loginStage.getIcons().add(icon);
                        loginStage.setScene(new Scene(root));
                        loginStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (SQLException e) {
                showAlert("Erreur", "Une erreur est survenue lors de la suppression du compte: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void handleFacialRecognition(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/guiUtilisateur/Webcamview.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Capture Facial Expression");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions
        }
    }
}


