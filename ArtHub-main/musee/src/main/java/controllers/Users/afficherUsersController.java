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

public class afficherUsersController implements Initializable {
    @FXML
    private VBox layoutusers;
    @FXML
    private ComboBox<String> roleFilterComboBox;
    private final ServiceUtilisateur ser=new ServiceUtilisateur();
    private ObservableList<Utilisateur> allUsers = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleFilterComboBox.getItems().addAll("Tous", "admin", "user");

        roleFilterComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            filterUsersByRole(newValue);
        });
        try {
            allUsers.addAll(ser.recuperer());
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
        HBox headerHBox = null;
        if (!layoutusers.getChildren().isEmpty()) {
            headerHBox = (HBox) layoutusers.getChildren().get(0);
        }
        layoutusers.getChildren().clear();

        if (headerHBox != null) {
            layoutusers.getChildren().add(headerHBox);
        }

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
