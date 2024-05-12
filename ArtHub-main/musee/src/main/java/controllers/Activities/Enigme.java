package controllers.Activities;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Enigme {
    @FXML
    public Label question;
    @FXML
    public Button opt1, opt2, opt3, opt4;
    static int counter = 0;
    static int correct = 0;
    static int wrong = 0;
    @FXML
    public ImageView image;
    @FXML
    private Button minimizeButton , closeButton ;

    @FXML
    private void initialize() {
       // closeButton.setOnAction(event -> closeStage());
      //  minimizeButton.setOnAction(event -> minimizeStage());
        loadQuestions();
    }

    private void loadQuestions() {

        if (counter == 0) { //Question 1
            question.setText("Who painted the famous artwork 'Starry Night'?");
            opt1.setText("Pablo Picasso");
            opt2.setText("Leonardo da Vinci");
            opt3.setText("Vincent van Gogh ");
            opt4.setText("Claude Monet");
        }
        if (counter == 1) { //Question 2
            question.setText("2. Which art movement was characterized by its focus on geometric shapes and bold colors?");
            opt1.setText("Fauvism");
            opt2.setText("Abstract Expressionism");
            opt3.setText("Surrealism");
            opt4.setText("Cubism");
        }
        if (counter == 2) { //Question 3
            question.setText("3. Who is considered the father of modern Tunisian painting?");
            opt1.setText("Nja Mahdaoui");
            opt2.setText("Hatem El Mekki");
            opt3.setText("Ammar Farhat");
            opt4.setText("Ali Bellagha ");
        }
        if (counter == 3) { //Question 4
            question.setText("4. What is the name of the oldest museum in Tunisia, known for its extensive collection of Roman mosaics?");
            opt1.setText("National Museum of Carthage");
            opt2.setText("Bardo Museum");
            opt3.setText("Museum of Islamic Art");
            opt4.setText("Sidi Bou Said Museum");
        }
        if (counter == 4) {//Question 5
            question.setText("5.Who is the Tunisian artist known for his impressionist paintings capturing scenes of Tunisian daily life?");
            opt1.setText("Ammar Farhat");
            opt2.setText("Nja Mahdaoui");
            opt3.setText("Aly Ben Salem");
            opt4.setText("Hatem El Mekki");
        }
        if (counter == 5) { //Question 6
            question.setText("6. In which country did Vincent van Gogh spend the majority of his painting career?");
            opt1.setText("France");
            opt2.setText("Spain");
            opt3.setText("Netherlands");
            opt4.setText("Italy");
        }
        if (counter == 6) { //Question 7
            question.setText("7. What was Bob Ross's famous catchphrase that he often used during his painting demonstrations?");
            String imagePath = "C:\\Users\\arij\\Desktop\\backup\\ArtHub-main\\musee\\src\\main\\resources\\images\\Bob-Ross.jpg";
            try {
                Image img = new Image(new FileInputStream(imagePath));
                image.setImage(img);
            }catch (FileNotFoundException e)
            {e.printStackTrace();}
            opt1.setText("Painting with passion");
            opt2.setText("Brush with greatness");
            opt3.setText("Let's get creative!");
            opt4.setText("Happy little clouds");
        }
        if (counter == 7) { //Question 8
            question.setText("8. Which of Vincent van Gogh's paintings is considered one of the most valuable artworks ever sold?");
            String imagePath = "C:\\Users\\arij\\Desktop\\backup\\ArtHub-main\\musee\\src\\main\\resources\\images\\portrait-of-dr-gachet-van-gogh-reproduction.jpg";
            try {
                Image img = new Image(new FileInputStream(imagePath));
                image.setImage(img);
            }catch (FileNotFoundException e)
            {e.printStackTrace();}
            opt1.setText("The Starry Night");
            opt2.setText("Irises");
            opt3.setText("Portrait of Dr. Gachet");
            opt4.setText("Sunflowers");
        }
        if (counter == 8) { //Question 9
            question.setText("9. Where is the Van Gogh Museum located?");
            String imagePath = "C:\\Users\\arij\\Desktop\\backup\\ArtHub-main\\musee\\src\\main\\resources\\images\\van-gogh-museum.jpg";
            try {
                Image img = new Image(new FileInputStream(imagePath));
                image.setImage(img);
            }catch (FileNotFoundException e)
            {e.printStackTrace();}
            opt1.setText("Paris, France");
            opt2.setText("Amsterdam, Netherlands");
            opt3.setText(" Madrid, Spain");
            opt4.setText("London, England");
        }
        if (counter == 9) { //Question 10
            question.setText("10. Which famous museum houses Leonardo da Vinci's masterpiece, the Mona Lisa?");
            String imagePath = "C:\\Users\\arij\\Desktop\\backup\\ArtHub-main\\musee\\src\\main\\resources\\images\\mona-lisa.jpg";
            try {
                Image img = new Image(new FileInputStream(imagePath));
                image.setImage(img);
            }catch (FileNotFoundException e)
            {e.printStackTrace();}
            opt1.setText("The Louvre");
            opt2.setText("The Metropolitan Museum of Art");
            opt3.setText("The British Museum");
            opt4.setText("The Vatican Museums");
        }

    }


    @FXML
    public void opt1clicked(ActionEvent event) {
        checkAnswer(opt1.getText().toString());
        if (checkAnswer(opt1.getText().toString())) {
            correct = correct + 1;
        } else {
            wrong = wrong + 1;
        }
        if (counter == 9) {
            try {
                System.out.println(correct);
                Stage thisstage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                thisstage.close();
                FXMLLoader quiz = new FXMLLoader(getClass().getResource("/guiActivite/ResultEnigme.fxml"));
                Scene quizscene = new Scene(quiz.load());
                quizscene.setFill(Color.TRANSPARENT);
                Stage quizstage = new Stage();
                quizstage.setScene(quizscene);
                quizstage.initStyle(StageStyle.TRANSPARENT);
                quizstage.show();
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            counter++;
            loadQuestions();
        }

    }
    @FXML
    private void minimizeStage() {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    private void closeStage() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    boolean checkAnswer(String answer) {

        if (counter == 0) {
            if (answer.equals("Vincent van Gogh ")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 1) {
            if (answer.equals("Fauvism")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 2) {
            if (answer.equals("Ali Bellagha")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 3) {
            if (answer.equals("Bardo Museum")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 4) {
            if (answer.equals("Nja Mahdaoui")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 5) {
            if (answer.equals("France")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 6) {
            if (answer.equals("Happy little clouds")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 7) {
            if (answer.equals("Portrait of Dr. Gachet")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 8) {
            if (answer.equals("Amsterdam, Netherlands")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 9) {
            if (answer.equals("The Louvre")) {
                return true;
            } else {
                return false;
            }
        }
        return false;


    }

    @FXML
    public void opt2clicked(ActionEvent event) {
        checkAnswer(opt2.getText().toString());
        if (checkAnswer(opt2.getText().toString())) {
            correct = correct + 1;
        } else {
            wrong = wrong + 1;
        }
        if (counter == 9) {
            try {
                System.out.println(correct);
                Stage thisstage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                thisstage.close();
                FXMLLoader quiz = new FXMLLoader(getClass().getResource("/guiActivite/ResultEnigme.fxml"));
                Scene quizscene = new Scene(quiz.load());
                quizscene.setFill(Color.TRANSPARENT);
                Stage quizstage = new Stage();
                quizstage.setScene(quizscene);
                quizstage.initStyle(StageStyle.TRANSPARENT);
                quizstage.show();
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            counter++;
            loadQuestions();
        }
    }

    @FXML
    public void opt3clicked(ActionEvent event) {
        checkAnswer(opt3.getText().toString());
        if (checkAnswer(opt3.getText().toString())) {
            correct = correct + 1;
        } else {
            wrong = wrong + 1;
        }
        if (counter == 9) {
            try {
                System.out.println(correct);
                Stage thisstage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                thisstage.close();
                FXMLLoader quiz = new FXMLLoader(getClass().getResource("/guiActivite/ResultEnigme.fxml"));
                Scene quizscene = new Scene(quiz.load());
                quizscene.setFill(Color.TRANSPARENT);
                Stage quizstage = new Stage();
                quizstage.setScene(quizscene);
                quizstage.initStyle(StageStyle.TRANSPARENT);
                quizstage.show();
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            counter++;
            loadQuestions();
        }
    }

    @FXML
    public void opt4clicked(ActionEvent event) {
        checkAnswer(opt4.getText().toString());
        if (checkAnswer(opt4.getText().toString())) {
            correct = correct + 1;
        } else {
            wrong = wrong + 1;
        }
        if (counter == 9) {
            try {
                System.out.println(correct);
                Stage thisstage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                thisstage.close();
                FXMLLoader quiz = new FXMLLoader(getClass().getResource("/guiActivite/ResultEnigme.fxml"));
                Scene quizscene = new Scene(quiz.load());
                quizscene.setFill(Color.TRANSPARENT);
                Stage quizstage = new Stage();
                quizstage.setScene(quizscene);
                quizstage.initStyle(StageStyle.TRANSPARENT);
                quizstage.show();
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            counter++;
            loadQuestions();
        }
    }

}

