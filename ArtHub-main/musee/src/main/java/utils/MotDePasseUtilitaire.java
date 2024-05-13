package utils;
import org.mindrot.jbcrypt.BCrypt;

public class MotDePasseUtilitaire {

    public static String hacherMotDePasse(String motDePasse) {
        // Générez un sel aléatoire
        String salt = BCrypt.gensalt(12);
        // Hachez le mot de passe avec le sel généré
        return BCrypt.hashpw(motDePasse, salt);
       // return BCrypt.hashpw(motDePasse, BCrypt.VERSION_2Y, salt);
    }

    public static boolean verifierMotDePasse(String motDePasseNonHaché, String motDePasseHaché) {
        //return BCrypt.checkpw(motDePasseNonHaché, motDePasseHaché);
        if (motDePasseHaché.startsWith("$2y$")) {
            motDePasseHaché = "$2a$" + motDePasseHaché.substring(4);
        }
        return BCrypt.checkpw(motDePasseNonHaché, motDePasseHaché);
    }
}
