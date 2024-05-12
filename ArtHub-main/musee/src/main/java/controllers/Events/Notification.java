package controllers.Events;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Notification extends StackPane {
    private static final int NOTIFICATION_WIDTH = 350;
    private static final int NOTIFICATION_HEIGHT = 40;
    private static final Duration NOTIFICATION_DURATION = Duration.seconds(1);

    private Label messageLabel;
    private VBox container; // Container for notifications

    public Notification(String message, VBox container) {
        this.container = container;

        Rectangle background = new Rectangle(NOTIFICATION_WIDTH, NOTIFICATION_HEIGHT);
        background.setFill(Color.LIGHTGRAY);
        background.setOpacity(0.8);
        background.setArcWidth(15);
        background.setArcHeight(15);

        messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(NOTIFICATION_WIDTH - 20);
        messageLabel.setTextFill(Color.BLACK);

        setAlignment(Pos.CENTER);
        getChildren().addAll(background, messageLabel);
        // Apply CSS class to the notification
        getStyleClass().add("notification");
    }

    public void show() {
        setTranslateY(-NOTIFICATION_HEIGHT);
        container.getChildren().add(this); // Add the notification to the container

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(translateYProperty(), 0);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);

        timeline.setOnFinished(event -> {
            new Timeline(new KeyFrame(NOTIFICATION_DURATION, e -> hide())).play();
        });

        timeline.play();
    }

    private void hide() {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(translateYProperty(), -NOTIFICATION_HEIGHT);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(event -> container.getChildren().remove(this));
        timeline.play();
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
