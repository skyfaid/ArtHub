package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.opencv.core.Core;
import utils.EmailService;


public class MainFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
       // System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
      //  EmailService.sendEmail("smtp.office365.com","wajdi.bouallegui@esprit.tn","W@JDATAskills123","wajdi.bouallegui@esprit.tn","wajdi.bouallegui@gmail.com","hello","hi");
       stage.initStyle(StageStyle.UNDECORATED);
        Image icon = new Image("/images/logo.png");
        stage.getIcons().add(icon);
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/guiUtilisateur/login.fxml"));
      // FXMLLoader loader=new FXMLLoader(getClass().getResource("/guiEvent/AjouterEvenement.fxml"));
        Parent root=loader.load();
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
