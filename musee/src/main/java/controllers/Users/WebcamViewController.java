package controllers.Users;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import utils.Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebcamViewController {

    @FXML
    private ComboBox<String> webcamSelector;

    @FXML
    private ImageView webcamFeed;

    @FXML
    private Label statusLabel;

    @FXML
    private Button startStopButton;

    private VideoCapture capture;
    private ScheduledExecutorService timer;
    private boolean isCameraRunning = false;

    @FXML
    public void initialize() {
        int numCameras = 10; // You can adjust this value as needed
        for (int i = 0; i < numCameras; i++) {
            capture = new VideoCapture(i);
            if (capture.isOpened()) {
                webcamSelector.getItems().add("Camera " + i);
                capture.release();
            }
        }
        if (webcamSelector.getItems().isEmpty()) {
            statusLabel.setText("No cameras found");
            startStopButton.setDisable(true);
        } else {
            webcamSelector.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void startStopCamera() {
        if (isCameraRunning) {
            stopCamera();
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        String selectedCamera = webcamSelector.getSelectionModel().getSelectedItem();
        if (selectedCamera != null) {
            int cameraIndex = Integer.parseInt(selectedCamera.split(" ")[1]);
            capture = new VideoCapture(cameraIndex);
            if (capture.isOpened()) {
                startStopButton.setText("Stop Camera");
                isCameraRunning = true;
                Runnable frameGrabber = this::grabFrame;
                timer = Executors.newSingleThreadScheduledExecutor();
                timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
                statusLabel.setText("Camera started");
            } else {
                statusLabel.setText("Failed to open camera");
            }
        }
    }

    private void grabFrame() {
        Mat frame = new Mat();
        if (capture.read(frame)) {
            Image imageToShow = Utils.mat2Image(frame);
            updateImageView(webcamFeed, imageToShow);
        }
    }

    private void stopCamera() {
        if (timer != null && !timer.isShutdown()) {
            try {
                timer.shutdown();
                timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (capture != null && capture.isOpened()) {
            capture.release();
            startStopButton.setText("Start Camera");
            statusLabel.setText("Camera stopped");
            isCameraRunning = false;
        }
    }

    private void updateImageView(ImageView view, Image image) {
        view.setImage(image);
    }
}
