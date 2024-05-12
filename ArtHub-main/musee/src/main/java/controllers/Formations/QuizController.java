package controllers.Formations;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class QuizController {
    @FXML
    private Text quest;
    @FXML
    private Label Question;

    @FXML
    private TextArea ReponseArea;
    public static int i=0;
    public static int suiv=0;
   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuizController that)) return false;
        return Objects.equals(ReponseArea, that.ReponseArea);
    }*/
    @FXML
    void suivant(ActionEvent event)
    {


        if(suiv<3)
            {
                try
                {
                    suiv = suiv + 1;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiFormation/Quizz.fxml"));
                    Parent root = loader.load();
                    Question.getScene().setRoot(root);
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            } else if (suiv==3)
            {
                Stage currentStage = (Stage) Question.getScene().getWindow();
                currentStage.close();
                   /* Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Note : "+i+"/3");*/
               /* try
                {
                   C
                //FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiFormation/userFormDisplay.fxml"));
                  //  Parent root = loader.load();
                  //  Question.getScene().setRoot(root);

                    // Close the current stage


                }
                catch (IOException e) {
                    throw new RuntimeException();
                }*/
        }

    }

    public void initialize(){
        try {
            String userAnswer = ReponseArea.getText().trim();
            String q=chatGPT("1 nouvelle question sur  la musique ");
           // String r=chatGPT(" la réponse en 10 mots à la question:" +q);
            Question.setText(SpecialCharacterHandler.ensureUtf8Encoding(q));
            if (userAnswer.equalsIgnoreCase("y")){
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SpecialCharacterHandler {

        /**
         * Converts a string with potentially incorrectly encoded characters into a correctly encoded UTF-8 string.
         * This function can be used to ensure special characters are displayed correctly in JavaFX.
         *
         * @param originalString The original string that may contain special characters.
         * @return A new string correctly encoded in UTF-8.
         */
        public static String ensureUtf8Encoding(String originalString) {
            // Convert the original string to bytes using the system's default charset,
            // then construct a new string from those bytes using UTF-8 encoding.
            // This helps correct any misinterpreted characters.
            return new String(originalString.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        }
    }

    public static String extractContentFromResponse(String response) {
        int startMarker = response.indexOf("content")+11; // Marker for where the content starts.
        int endMarker = response.indexOf("\"", startMarker); // Marker for where the content ends.
        return response.substring(startMarker, endMarker); // Returns the substring containing only the response.
    }
    public static String chatGPT(String message) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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"; // API key goes here
        String model = "gpt-4"; // current model of chatgpt api

        try {
            // Create the HTTP POST request
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");

            // Build the request body
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Get the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // returns the extracted contents of the response.
            return extractContentFromResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}