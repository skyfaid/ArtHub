package controllers.Oeuvres;

import com.stripe.exception.StripeException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import entities.vente;
import services.VenteService;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentController implements Initializable {
    @FXML
    private Label total;
    @FXML
    private Button pay_btn;
    @FXML
    private TextField email;
    @FXML
    private TextField num_card;
    @FXML
    private Spinner<Integer> MM;
    @FXML
    private Spinner<Integer> YY;
    @FXML
    private Spinner<Integer> cvc; //********************numéro de sécurité de votre carte bancaire.   qui se compose de 4 num

    private double total_pay;
    private PanierClient panierClient;
    @FXML
    private TextField client_name;
    @FXML
    private Button back_btn;
    private TextField spinnerTextField;
    private SpinnerValueFactory<Integer> spinMM;
    private SpinnerValueFactory<Integer> spinYY;
    private SpinnerValueFactory<Integer> spinCVC;



    public void setMM()
    {
        spinMM = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1);
        MM.setValueFactory(spinMM);
    }

    public void setYY()
    {
        spinYY = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1);
        YY.setValueFactory(spinYY);
    }

    public void setCVC()
    {
        spinCVC = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1);
        cvc.setValueFactory(spinCVC);
    }


    public void setData(double totalFromPanier) {
        Platform.runLater(() -> total.setText(String.format("Total : %.2f €", totalFromPanier)));
        this.total_pay = totalFromPanier;
        int mm = LocalDate.now().getMonthValue();
        int yy = LocalDate.now().getYear();
        SpinnerValueFactory<Integer> valueFactory_month = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, mm, 1);// (min,max,startvalue,incrementValue)
        SpinnerValueFactory<Integer> valueFactory_year = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 9999999, yy, 1);// (min,max,startvalue,incrementValue)
        SpinnerValueFactory<Integer> valueFactory_cvc = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1, 1);// (min,max,startvalue,incrementValue)
        MM.setValueFactory(valueFactory_month);
        YY.setValueFactory(valueFactory_year);
        cvc.setValueFactory(valueFactory_cvc);
        String total_txt = "Total : " + String.valueOf(total_pay) + " Dt.";
        total.setText(total_txt);
        spinnerTextField= cvc.getEditor();
        spinnerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                cvc.getValueFactory().setValue(Integer.parseInt(newValue));
            } catch (NumberFormatException e) {
                // Handle invalid input
            }
        });

    }

    @FXML
    private void payment(ActionEvent event) throws StripeException {
//        try {
//            FXMLLoader loader = new FXMLLoader((getClass().getResource("/guiOeuvre/success.fxml")));
//            Parent root = loader.load();
//            total.getScene().setRoot(root);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        if (total_pay < 50) {
            // Afficher un message d'erreur à l'utilisateur, ne pas procéder au paiement
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Le montant total doit être d'au moins 0.50 pour procéder au paiement.");
            alert.showAndWait();
            return; // Sortir de la méthode pour éviter de faire l'appel de paiement
        }
        if (client_name.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You need to input your Name");
            alert.setTitle("Problem");
            alert.setHeaderText(null);
            alert.showAndWait();
            client_name.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(client_name).play();
        } else if (email.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You need to input your Email");
            alert.setTitle("Problem");
            alert.setHeaderText(null);
            alert.showAndWait();
            email.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(email).play();
        } else if (!isValidEmail(email.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid Email address.");
            alert.setTitle("Problem");
            alert.setHeaderText(null);
            alert.showAndWait();
            email.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(email).play();
        } else if (num_card.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You need to input your Card Number");
            alert.setTitle("Problem");
            alert.setHeaderText(null);
            alert.showAndWait();
            num_card.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(num_card).play();
        } else if (!check_cvc(cvc.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("The CVC number should contain three digits");
            alert.setTitle("Problem");
            alert.setHeaderText(null);
            alert.showAndWait();
            cvc.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(cvc).play();
        } else if (!check_expDate(YY.getValue(), MM.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid expiration date");
            alert.setTitle("Problem");
            alert.setHeaderText(null);
            alert.showAndWait();
            MM.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            YY.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            new animatefx.animation.Shake(MM).play();
            new animatefx.animation.Shake(YY).play();
        } else {
            client_name.setStyle(null);
            email.setStyle(null);
            num_card.setStyle(null);
            cvc.setStyle(null);
            MM.setStyle(null);
            YY.setStyle(null);
            boolean isValid = check_card_num(num_card.getText());
            if (!isValid) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter a valid Card number");
                alert.setTitle("Problem");
                alert.setHeaderText(null);
                alert.showAndWait();
                num_card.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                new animatefx.animation.Shake(num_card).play();
            } else {
                num_card.setStyle(null);
                String name = client_name.getText();
                String email_txt = email.getText();
                String num = num_card.getText();
                int yy = YY.getValue();
                int mm = MM.getValue();
                String cvc_num = String.valueOf(cvc.getValue());
                if (total_pay < 50) {
                    // Afficher un message d'erreur à l'utilisateur, ne pas procéder au paiement
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Le montant total doit être d'au moins 0.50 pour procéder au paiement.");
                    alert.showAndWait();
                    return; // Sortir de la méthode pour éviter de faire l'appel de paiement
                }

// Continuer avec l'appel de paiement si le montant est suffisant
                boolean payment_result = PaymentProcessor.processPayment(name, email_txt, 500, "4242424242424242", 12, 24, "100");
                sucess s =new sucess();

                if (payment_result) {
                    vente nouvelleVente = new vente();

// Assurez-vous de remplir les détails de la nouvelle vente correctement
                    nouvelleVente.setID_OeuvreVendue(1); // Remplacez 1 avec l'ID réel de l'œuvre vendue
                    nouvelleVente.setDateVente(new java.sql.Date(System.currentTimeMillis()));
                    nouvelleVente.setPrixVente(total_pay); // Assurez-vous que ce montant est correct
                    nouvelleVente.setModePaiement("Carte"); // Ou tout autre mode de paiement que vous utilisez
                    nouvelleVente.setQuantite(3); // Remplacez 3 avec la quantité réelle vendue

                    try {
                        VenteService venteService = new VenteService();
                        venteService.ajouterVente(nouvelleVente);
                       // System.out.println("Vente ajoutée avec succès.");

                        // Afficher un message de confirmation à l'utilisateur
                       // Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION, "La vente a été ajoutée avec succès.");
                       // confirmationAlert.showAndWait();

                        // Afficher la page de succès
                        sucess ss = new sucess();
                        ss.redirect_to_successPage();

                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("Erreur lors de l'ajout de la vente.");

                        // Afficher un message d'erreur à l'utilisateur
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'ajout de la vente.");
                        errorAlert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Payment Failed.");
                    alert.setTitle("Problem");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    //***************************
                    s.redirect_to_FailPage();
                }
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/guiOeuvre/success.fxml")));
            Parent root = loader.load();
            total.getScene().setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean check_cvc(int value) {
        String cvc_txt = String.valueOf(value);
        boolean valid = false;
        if (cvc_txt.length() == 3) {
            valid = true;
        }
        return valid;
    }

    private boolean check_expDate(int value_y, int value_mm) {
        boolean valid = false;
        LocalDate date = LocalDate.now();
        if ((value_y >= date.getYear()) && (value_mm >= date.getMonthValue())) {
            valid = true;
        }
        return valid;
    }

    private boolean check_card_num(String cardNumber) {
        // Trim the input string to remove any leading or trailing whitespace
        cardNumber = cardNumber.trim();
        // Step 1: Check length
        int length = cardNumber.length();
        if (length < 13 || length > 19) {
            return false;
        }
        String regex = "^(?:(?:4[0-9]{12}(?:[0-9]{3})?)|(?:5[1-5][0-9]{14})|(?:6(?:011|5[0-9][0-9])[0-9]{12})|(?:3[47][0-9]{13})|(?:3(?:0[0-5]|[68][0-9])[0-9]{11})|(?:((?:2131|1800|35[0-9]{3})[0-9]{11})))$";
        // Create a Pattern object with the regular expression
        Pattern pattern = Pattern.compile(regex);

        // Match the pattern against the credit card number
        Matcher matcher = pattern.matcher(cardNumber);

        // Return true if the pattern matches, false otherwise
        return matcher.matches();
    }

    public boolean isValidEmail(String email) {
        // Trim the input string to remove any leading or trailing whitespace
        email = email.trim();
        // Regular expression pattern to match an email address
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);

        // Match the pattern against the email address
        Matcher matcher = pattern.matcher(email);

        // Return true if the pattern matches, false otherwise
        return matcher.matches();
    }


    /*
    private void redirect_to_successPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/reservation/Success_page.fxml"));
            Parent root = loader.load();
            //UPDATE The Controller with Data :
            Success_pageController controller = loader.getController();
            controller.setData(this.reservation);
            Scene scene = new Scene(root);
            Stage stage = (Stage) pay_btn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void redirect_to_FailPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/reservation/Fail_page.fxml"));
            Parent root = loader.load();
            //UPDATE The Controller with Data :
            Fail_pageController controller = loader.getController();
            controller.setData(this.reservation);
            Scene scene = new Scene(root);
            Stage stage = (Stage) pay_btn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void redirectToListReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/reservation/Reservation_view_client.fxml"));
            Parent root = loader.load();
            //UPDATE The Controller with Data :
            Reservation_view_client controller = loader.getController();
            controller.setData(this.reservation.getClient_id());
            Scene scene = new Scene(root);
            Stage stage = (Stage) back_btn.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMM();
        setYY();
        setCVC();

        // Initialiser d'autres composants d'interface utilisateur si nécessaire
        // Par exemple, réinitialiser les champs de texte ou définir des valeurs par défaut
        client_name.clear();
        email.clear();
        num_card.clear();
        cvc.getValueFactory().setValue(LocalDate.now().getYear()); // Juste un exemple, assurez-vous que cela correspond à votre logique

        // Si vous avez d'autres logiques d'initialisation, ajoutez-les ici
        // Par exemple, si vous voulez pré-remplir le total du paiement, vous pourriez le faire ici si vous avez cette information
        // Note: Assurez-vous que panierClient est initialisé avant de l'utiliser ici, sinon, vous pourrie z avoir besoin de le faire dans une méthode distincte après l'initialisation.
        // Ceci est juste un exemple, assurez-vous que 'total_pay' est défini correctement avant d'essayer de l'utiliser
        if (panierClient != null) {
            total_pay = panierClient.totalPrix; // Assurez-vous que getTotalPrix() est une méthode valide dans PanierClient
            total.setText(String.format("Total: %.2f", total_pay)); // Formater et afficher le total
        }

    }


}
