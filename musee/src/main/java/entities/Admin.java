package entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Admin extends Utilisateur {
    private List<Article> pages = new ArrayList<Article>();

    public Admin(String pseudo, String prenom, String nom, String email, String motDePasseHash, Date dateInscription,String gender) {
        super(pseudo, prenom, nom, email, motDePasseHash, gender);
    }
}
