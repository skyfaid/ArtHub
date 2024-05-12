package controllers.Activities;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class WelcomeActivityController {
    @FXML
    private Label welcomeMessage;
    public void setActivityName(String activityName) {
        welcomeMessage.setText("Welcome to " + activityName + "!");
    }
}
