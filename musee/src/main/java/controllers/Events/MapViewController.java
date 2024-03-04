package controllers.Events;

import com.google.maps.model.LatLng;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import utils.GeocodingService;
import services.ServiceEvenement;

import java.util.List;

public class MapViewController {
    @FXML
    private WebView mapWebView;

    private ServiceEvenement serviceEvenement;
    private GeocodingService geocodingService;

    // Static location (You can set this to your desired static location)
    private final LatLng staticLocation = new LatLng(STATIC_LATITUDE, STATIC_LONGITUDE);

    // Store event coordinates
    private List<LatLng> eventCoordinates;

    private String nearestEvent;
    public MapViewController() {
        // This constructor is needed by FXMLLoader
    }
    // Inject ServiceEvenement and GeocodingService instances via constructor
    public MapViewController(ServiceEvenement serviceEvenement) {
        this.serviceEvenement = serviceEvenement;
    }

    public void initialize() {
        // Initialize ServiceEvenement and GeocodingService
        serviceEvenement = new ServiceEvenement();
        geocodingService = new GeocodingService();

        WebEngine webEngine = mapWebView.getEngine();
        // Enable JavaScript
        webEngine.setJavaScriptEnabled(true);
        // Load Google Maps URL
        webEngine.load("https://www.google.com/maps");

        // Wait for the web page to finish loading before marking event locations
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                // Once the page is loaded, retrieve event coordinates
                retrieveEventCoordinates();
                // Calculate and display the nearest event
                displayNearestEvent();
            }
        });
    }
    public void retrieveEventCoordinates() {
        // Retrieve event coordinates using the ServiceEvenement
        eventCoordinates = serviceEvenement.getEventCoordinates();
    }
    private void displayNearestEvent() {
       // Initialize variables to keep track of the nearest event and its distance
       nearestEvent = null;
       double shortestDistance = Double.MAX_VALUE;

       // Calculate distance for each event and find the nearest one
       for (int i = 0; i < eventCoordinates.size(); i++) {
           LatLng eventLocation = eventCoordinates.get(i);
           double distance = calculateDistance(staticLocation, eventLocation);
           if (distance < shortestDistance) {
               shortestDistance = distance;
               nearestEvent = serviceEvenement.getEventAddresses().get(i); // Get address for nearest event
           }
       }
       // Display the nearest event
       System.out.println("Nearest event to static location: " + nearestEvent);
   }

    @FXML
    void showNearestEvent(ActionEvent event) {
        if (nearestEvent != null) {
            // Execute JavaScript to paste the nearest event address into the search bar and simulate a click to search
            WebEngine engine = mapWebView.getEngine();
            engine.executeScript("document.querySelector('input[id=\"searchboxinput\"]').value = '" + nearestEvent + "';");
            engine.executeScript("document.querySelector('button[id=\"searchbox-searchbutton\"]').click();");
        } else {
            System.out.println("No nearest event found.");
        }
    }

    // Calculate distance between two LatLng points (Haversine formula)
    private double calculateDistance(LatLng point1, LatLng point2) {
        double lat1 = Math.toRadians(point1.lat);
        double lon1 = Math.toRadians(point1.lng);
        double lat2 = Math.toRadians(point2.lat);
        double lon2 = Math.toRadians(point2.lng);

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of Earth in kilometers
        double radius = 6371;

        return radius * c;
    }

    // Your static location coordinates
    private static final double STATIC_LATITUDE = 36.89925085681154;
    private static final double STATIC_LONGITUDE = 10.189273675563127;
}
