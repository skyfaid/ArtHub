package controllers.Users;

import entities.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.ServiceUtilisateur;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class afficherUsers implements Initializable {
    @FXML
    private VBox layoutusers;
    @FXML
    private ComboBox<String> roleFilterComboBox;
    private final ServiceUtilisateur ser=new ServiceUtilisateur();
    private ObservableList<Utilisateur> allUsers = FXCollections.observableArrayList(); // Liste de tous les utilisateurs

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleFilterComboBox.getItems().addAll("Tous", "admin", "user"); // Peut être omis si défini dans FXML

        roleFilterComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            filterUsersByRole(newValue);
        });
        try {
            allUsers.addAll(ser.recuperer()); // Remplir la liste de tous les utilisateurs
            displayUsers(allUsers);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void filterUsersByRole(String role) {
        ObservableList<Utilisateur> filteredUsers = FXCollections.observableArrayList();
        if ("Tous".equals(role)) {
            filteredUsers.addAll(allUsers);
        } else {
            for (Utilisateur user : allUsers) {
                if (role.equals(user.getRole())) {
                    filteredUsers.add(user);
                }
            }
        }
        displayUsers(filteredUsers);
    }
    private void displayUsers(ObservableList<Utilisateur> users) {
        // Sauvegarder le premier HBox qui contient les noms des colonnes
        HBox headerHBox = null;
        if (!layoutusers.getChildren().isEmpty()) {
            headerHBox = (HBox) layoutusers.getChildren().get(0);
        }

        // Nettoyer les éléments précédents, en préservant le premier HBox
        layoutusers.getChildren().clear();

        // Remettre le premier HBox s'il existait
        if (headerHBox != null) {
            layoutusers.getChildren().add(headerHBox);
        }

        // Ajouter les nouveaux éléments pour chaque utilisateur
        for (Utilisateur user : users) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/guiUtilisateur/userItem.fxml"));
                HBox hbox = fxmlLoader.load();
                UserItemController itm = fxmlLoader.getController();
                itm.setData(user);
                layoutusers.getChildren().add(hbox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
