package controllers.Oeuvres;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class HomeController {

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
    private HBox titleBar;

    @FXML
    private StackPane contentStackPane;
    @FXML
    private VBox homeView,eventsView,usersView,articlesView,oeuvresView,activitesView,reclamationsView,formationsView;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML private Button closeButton;
    @FXML private Button minimizeButton;
    @FXML
    private BorderPane root;

    private void closeStage() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    private void minimizeStage() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setIconified(true);
    }

    public void initialize() throws IOException {
        makeStageDraggable();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/guiOeuvre/GestionOeuvre.fxml"));
        VBox vBox = fxmlLoader.load();
        oeuvresView.getChildren().setAll(vBox);
        showView(oeuvresView);

        closeButton.setOnAction(event -> closeStage());
        minimizeButton.setOnAction(event -> minimizeStage());
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

    @FXML
    protected void onButtonHover(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #2E86C1; -fx-text-fill: white;");
    }

    @FXML
    protected void onButtonExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
    }

    @FXML
    protected void onHomeButtonClick(ActionEvent event) {
        showView(homeView);
    }

    @FXML
    protected void onEventsButtonClick(ActionEvent event) {
        showView(eventsView);
    }

    @FXML
    protected void onUsersButtonClick(ActionEvent event) {
        showView(usersView);
    }

    @FXML
    protected void onActivitiesButtonClick(ActionEvent event) {
        showView(activitesView);
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
    }
    @FXML
    protected void onReclamationButtonClick(ActionEvent event) {
        showView(reclamationsView);
    }

    private void showView(VBox view) {
        // Hide all views
        contentStackPane.getChildren().forEach(child -> child.setVisible(false));
        // Show the specified view
        view.setVisible(true);
    }
}
