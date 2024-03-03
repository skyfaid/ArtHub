package controllers.Users;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import utils.EmailService;
import utils.ValidationUtils;
import entities.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.ServiceUtilisateur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.sql.SQLException;

public class SignupController {

    public TextField phoneNumberField;
    @FXML
    private BorderPane root;
    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private Button closeButton;

    @FXML
    private HBox titleBar;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private Button minimizeButton;

    @FXML
    private TextField nomField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField pseudoField;

    private final ServiceUtilisateur ser=new ServiceUtilisateur();

    private double xOffset = 0;

    private double yOffset = 0;
    @FXML
    private ComboBox<String> countryCodeComboBox;


    public void initialize() {
        makeStageDraggable();
        closeButton.setOnAction(event -> closeStage());
        minimizeButton.setOnAction(event -> minimizeStage());
        genderComboBox.getItems().addAll("Male", "Female");
        EventHandler<KeyEvent> enterKeyHandler = event -> {
            if (event.getCode() == KeyCode.ENTER) {
                ajouterUtilisateur();
            }
        };
        pseudoField.setOnKeyPressed(enterKeyHandler);
        nomField.setOnKeyPressed(enterKeyHandler);
        prenomField.setOnKeyPressed(enterKeyHandler);
        emailField.setOnKeyPressed(enterKeyHandler);
        passwordField.setOnKeyPressed(enterKeyHandler);
        confirmPasswordField.setOnKeyPressed(enterKeyHandler);


        countryCodeComboBox.setCellFactory(lv -> new ListCell<String>() {
            private ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Here, you should set the image view based on the item (the country code)
                    // For example, if you have images stored in a map by country code:
                    Image image = new Image(getClass().getResourceAsStream("/images/"+item+".png"  ));
                    imageView.setImage(image);
                    imageView.setFitWidth(20);
                    imageView.setFitHeight(15);
                    setText(item);
                    setGraphic(imageView);
                }
            }
        });
    }
    // ...
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
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void ajouterUtilisateur() {
        try{
            String pseudo = pseudoField.getText();
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String gender = genderComboBox.getSelectionModel().getSelectedItem();
            String countryCode = countryCodeComboBox.getSelectionModel().getSelectedItem();
            String phoneNumber = phoneNumberField.getText();
            boolean isValid = true;
            String errormesg="";

            if (!ValidationUtils.isValidName(nom)) {
                nomField.getStyleClass().add("text-field-invalid");
                isValid = false;
                errormesg+="Invalid first name. Please enter a valid name \n";
            }

            if (!ValidationUtils.isValidName(prenom)) {
                prenomField.getStyleClass().add("text-field-invalid");
                isValid = false;
                errormesg+=" Invalid last name. Please enter a valid name \n";
            }

            if (!ValidationUtils.isValidPseudo(pseudo)) {
                pseudoField.getStyleClass().add("text-field-invalid");
                isValid = false;
                errormesg+="pseudo should start with at least 3 letters and can include numbers \n";

            }

            if (!ser.estPseudoUnique(pseudo)) {
                pseudoField.getStyleClass().add("text-field-invalid");
                isValid = false;
                errormesg+=" Pseudo already exists. Please choose a different pseudo \n";

            }
            if (gender == null) {
                errormesg+=" Gender is required \n";
                isValid = false;
            }

            if (!ValidationUtils.isValidPhoneNumber(countryCode, phoneNumber)) {
                phoneNumberField.getStyleClass().add("text-field-invalid");
                isValid = false;
                errormesg += "Invalid phone number.\n";
            }

            if (!ValidationUtils.isValidEmail(email)) {
                emailField.getStyleClass().add("text-field-invalid");
                isValid = false;
                errormesg+=" Invalid email format. \n";
            }

            if (!ser.estEmailUnique(email)) {
                emailField.getStyleClass().add("text-field-invalid");
                isValid = false;
                errormesg+="Email already exists. Please use a different email.\n";
            }

            if (!ValidationUtils.isValidPassword(password, confirmPassword)) {
                passwordField.getStyleClass().add("text-field-invalid");
                confirmPasswordField.getStyleClass().add("text-field-invalid");
                isValid = false;
                errormesg+="Passwords do not match. \n";

            }
            if (!isValid) {
                showAlert("Validation Error", errormesg);
                return;
            }
            String verificationCode = String.valueOf((int) (Math.random() * 9000) + 1000);

            // Send the verification code to the user's email
            EmailService.sendEmail("smtp.office365.com", "wajdi.bouallegui@esprit.tn", "W@JDATAskills123",
                    "wajdi.bouallegui@esprit.tn", email.trim(), "Email Verification Code", "Your verification code is: " + verificationCode);

            // Open the code verification stage
            Utilisateur usr=new Utilisateur(pseudoField.getText(),prenomField.getText(),nomField.getText(),emailField.getText(),passwordField.getText(),gender,countryCode+phoneNumber);
            ser.ajouter(usr);
            ser.updateResetCode(usr.getEmail(),verificationCode);
            showAlert("Success", "Account Created :)");
            openCodeVerificationStage(usr);
            closeStage();
        }catch (SQLException e){
            showAlert("Eroor", e.getMessage());
        }
    }
    private void openCodeVerificationStage(Utilisateur u) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiUtilisateur/ActivateAcount.fxml"));
            Parent codeVerificationParent = loader.load();

            // Set the controller for CodeVerificationScene
            ActivateAccountController act = loader.getController();
            act.setPrimaryStage(new Stage());
            act.setUser(u);

            // Set the scene on the new stage
            Scene codeVerificationScene = new Scene(codeVerificationParent);
            Stage codeVerificationStage = new Stage();
            codeVerificationStage.setScene(codeVerificationScene);
            codeVerificationStage.show(); // Show the stage
        } catch (IOException e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

}
