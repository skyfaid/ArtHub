package controllers.Oeuvres;

import entities.Oeuvre;

import java.util.ArrayList;
import java.util.List;

public class GestionPanier {
    private static GestionPanier instance;
    private static List<Oeuvre> panier;

    private GestionPanier() {
        panier = new ArrayList<>();
    }

    public static GestionPanier getInstance() {
        if (instance == null) {
            instance = new GestionPanier();
        }
        return instance;
    }

    public static void ajouterOeuvre(Oeuvre oeuvre) {
        panier.add(oeuvre);
    }

    public static List<Oeuvre> getPanier() {
        return panier;
    }
}

