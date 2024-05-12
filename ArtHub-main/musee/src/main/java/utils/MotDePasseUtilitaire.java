package utils;
import org.mindrot.jbcrypt.BCrypt;

public class MotDePasseUtilitaire {

    public static String hacherMotDePasse(String motDePasse) {
        // Générez un sel aléatoire
        String salt = BCrypt.gensalt();
        // Hachez le mot de passe avec le sel généré
        return BCrypt.hashpw(motDePasse, salt);
    }

    public static boolean verifierMotDePasse(String motDePasseNonHaché, String motDePasseHaché) {
        return BCrypt.checkpw(motDePasseNonHaché, motDePasseHaché);
    }
}
