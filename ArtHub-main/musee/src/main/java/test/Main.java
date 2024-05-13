package test;

import entities.Utilisateur;
import services.ServiceUtilisateur;
import utils.MotDePasseUtilitaire;

import java.sql.SQLException;

import static utils.MotDePasseUtilitaire.verifierMotDePasse;

public class Main {
    public static void main(String[] args) {

        boolean a=verifierMotDePasse("aa","$2y$12$i1262fRcfGnuRdOR51XLauPfjRRe3Ky9tK.2dJ1GKmH6vDQQQErBu");

        System.out.println(a);

    }

}


