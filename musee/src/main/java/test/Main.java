package test;

import entities.Utilisateur;
import services.ServiceUtilisateur;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        final ServiceUtilisateur ser=new ServiceUtilisateur();
        try {
            Utilisateur util=new Utilisateur("admin2","ala","boucha","admin2@gmail.com","azeqsd123","male");
            util.setRole("admin");
             ser.ajouter(util);
            System.out.println("hi from here");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}


