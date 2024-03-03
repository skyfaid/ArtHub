package controllers.Activities;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private void initialize() {
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
            question.setText("6. Who discovered Pasteurisation?");
            opt1.setText("Alexander Fleming");
            opt2.setText("Louis Pasteur");
            opt3.setText("Simon Pasteur");
            opt4.setText("William Pasteur");
        }
        if (counter == 6) { //Question 7
            question.setText("7. Hydrochloric acid (HCl) is produced by -?");
            opt1.setText("Small Intestine");
            opt2.setText("Liver");
            opt3.setText("Oesophagus");
            opt4.setText("Stomach");
        }
        if (counter == 7) { //Question 8
            question.setText("8. The fastest animal in the world is -");
            opt1.setText("Lion");
            opt2.setText("Blackbuck");
            opt3.setText("Cheetah");
            opt4.setText("Quarter Horse");
        }
        if (counter == 8) { //Question 9
            question.setText("9. Complementary colour of Red is -");
            opt1.setText("Blue");
            opt2.setText("Green");
            opt3.setText("Yellow");
            opt4.setText("Pink");
        }
        if (counter == 9) { //Question 10
            question.setText("10. World Environment Day is on -");
            opt1.setText("5th June");
            opt2.setText("5th July");
            opt3.setText("15th June");
            opt4.setText("25th June");
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
            if (answer.equals("Neptune")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 3) {
            if (answer.equals("Ganymede")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 4) {
            if (answer.equals("Non Ductile")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 5) {
            if (answer.equals("Louis Pasteur")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 6) {
            if (answer.equals("Stomach")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 7) {
            if (answer.equals("Cheetah")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 8) {
            if (answer.equals("Green")) {
                return true;
            } else {
                return false;
            }
        }
        if (counter == 9) {
            if (answer.equals("5th June")) {
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
                FXMLLoader quiz = new FXMLLoader(getClass().getResource("result.fxml"));
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
                FXMLLoader quiz = new FXMLLoader(getClass().getResource("result.fxml"));
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
                FXMLLoader quiz = new FXMLLoader(getClass().getResource("result.fxml"));
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

