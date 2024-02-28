package utils;

import entities.Evenement;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class FieldPopulator {

    public static void populateFields(Evenement event, TextField nomEditTF, TextField typeEditTF,
                                      DatePicker datedebutEditPicker, DatePicker datefinEditPicker,
                                      TextField lieuEditTF, TextField nbrePlacesEditTF,
                                      TextField descriptionEditTF) {
        nomEditTF.setText(event.getNom());
        typeEditTF.setText(event.getType());
        datedebutEditPicker.setValue(event.getDatedebut());
        datefinEditPicker.setValue(event.getDatefin());
        lieuEditTF.setText(event.getLieu());
        nbrePlacesEditTF.setText(String.valueOf(event.getNombrePlaces()));
        descriptionEditTF.setText(event.getDescription());
    }
}
