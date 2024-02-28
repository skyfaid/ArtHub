package utils;

import java.time.LocalDate;

public class Validation {


    // Date Debut  And Date FIn
    public static  boolean validateDates(LocalDate startDate, LocalDate endDate) {

        // Check if either date is null
        if (startDate == null || endDate == null) {

            return false;
        }
        // Check if the end date is not after the start date
        if (!endDate.isAfter(startDate)&& !endDate.isEqual(startDate)) {
            return false;
        }
        return true;
    }


// Nom D'Activite
    public static boolean validateNomAct(String nomAct ) {
        // Check if the string is not null, not empty, consists only of letters, and has 15 or fewer characters
        return nomAct  != null && !nomAct .trim().isEmpty() && nomAct .matches("[a-zA-Z]+") && nomAct .length() <= 15;
    }


// controle Lieu
    public static boolean validateLieu(String lieu) {
        // Check if the string is not null, not empty, and matches the desired pattern
        // This regex allows letters, numbers, spaces, and common punctuation. Adjust as needed.
        return lieu != null && !lieu.trim().isEmpty() && lieu.matches("[a-zA-Z0-9 ,.-]+");
    }

    //controle nombre de places :
    public static boolean validateNbrePlaces(String nbrePlacesStr) {
        try {
            int nbrePlaces = Integer.parseInt(nbrePlacesStr);
            return nbrePlaces <= 100; // Assuming the number of places must be positive
        } catch (NumberFormatException e) {
            return false; // Not a valid integer
        }
    }

    //controle type
    public static boolean validateType(String typeStr)
    {
        return typeStr  != null && !typeStr .trim().isEmpty() && typeStr .matches("[a-zA-Z]+") && typeStr .length() <= 15;
    }
}
