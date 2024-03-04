package utils;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import java.io.IOException;

public class GeocodingService {

    // Initialize your Google Maps API key here
    private static final String API_KEY = "AIzaSyBb8H-l3A5bV1VhbRunv5-SSEogVGFrJEw";

    // Create a GeoApiContext object with your API key
    private final GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(API_KEY)
            .build();

    public LatLng geocodeAddress(String address) throws ApiException, InterruptedException, IOException {
        // Call the Geocoding API to geocode the given address
        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
        // Extract latitude and longitude from the first result
        if (results != null && results.length > 0) {
            return results[0].geometry.location;
        } else {
            return null;
        }
    }
}
