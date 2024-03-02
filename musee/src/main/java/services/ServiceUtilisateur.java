package services;

import entities.Evenement;
import entities.Utilisateur;
import utils.MotDePasseUtilitaire;
import utils.MyDataBase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServiceUtilisateur implements ServiceCrud<Utilisateur> {
    private final Connection connection;
    private MotDePasseUtilitaire motDePasseUtilitaire;
    public ServiceUtilisateur(){
        this.connection = MyDataBase.getInstance().getConnection();
        this.motDePasseUtilitaire = new MotDePasseUtilitaire();
    }
    private Utilisateur mapResultSetToUtilisateur(ResultSet resultSet) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUtilisateurId(resultSet.getInt("utilisateur_id"));
        utilisateur.setPseudo(resultSet.getString("pseudo"));
        utilisateur.setPrenom(resultSet.getString("prenom"));
        utilisateur.setNom(resultSet.getString("nom"));
        utilisateur.setEmail(resultSet.getString("email"));
        utilisateur.setMotDePasseHash(resultSet.getString("mot_de_passe_hash"));
        utilisateur.setRole(resultSet.getString("role"));
        utilisateur.setGender(resultSet.getString("gender"));
        utilisateur.setDateInscription(resultSet.getTimestamp("date_inscription"));
        utilisateur.setDerniereConnexion(resultSet.getTimestamp("derniere_connexion"));
        utilisateur.setUrlImageProfil(resultSet.getString("url_image_profil"));
        utilisateur.setEstActif(resultSet.getBoolean("estActif"));
        return utilisateur;
    }
    @Override
    public Utilisateur recupererById(int id) throws SQLException {
        String sql = "SELECT * FROM Utilisateurs WHERE utilisateur_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
            return mapResultSetToUtilisateur(resultSet);
        return null;
    }
    @Override
    public List<Utilisateur> recuperer() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList();
        String sql = "SELECT * FROM Utilisateurs";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            Utilisateur utilisateur = mapResultSetToUtilisateur(resultSet);
            utilisateurs.add(utilisateur);
        }
        return utilisateurs;
    }
    public boolean estPseudoUnique(String pseudo) throws SQLException {
        String requete = "SELECT COUNT(*) FROM Utilisateurs WHERE pseudo = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(requete);
        preparedStatement.setString(1, pseudo.trim());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int compte = resultSet.getInt(1); // Obtenir le compte
            return compte == 0; // vrai si unique, faux sinon
        }
        return false; // Retour par défaut à faux si la requête échoue à s'exécuter
    }

    public boolean estEmailUnique(String email) throws SQLException {
        String requete = "SELECT COUNT(*) FROM Utilisateurs WHERE email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(requete);
        preparedStatement.setString(1, email.trim());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int compte = resultSet.getInt(1); // Obtenir le compte
            return compte == 0; // vrai si unique, faux sinon
        }
        return false; // Retour par défaut à faux si la requête échoue à s'exécuter
    }

    @Override
    public void ajouter(Utilisateur utilisateur) throws SQLException {
        String sql = "INSERT INTO Utilisateurs (pseudo, prenom, nom, email, mot_de_passe_hash, gender, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, utilisateur.getPseudo());
        preparedStatement.setString(2, utilisateur.getPrenom());
        preparedStatement.setString(3, utilisateur.getNom());
        preparedStatement.setString(4, utilisateur.getEmail());
        preparedStatement.setString(5, motDePasseUtilitaire.hacherMotDePasse(utilisateur.getMotDePasseHash()));
        preparedStatement.setString(6, utilisateur.getGender());
        preparedStatement.setString(7, utilisateur.getPhoneNumber());
        preparedStatement.executeUpdate();
    }
    @Override
    public void modifier(Utilisateur utilisateur) throws SQLException {
        String sql = "UPDATE Utilisateurs SET pseudo = ?, prenom = ?, nom = ?, email = ?, mot_de_passe_hash = ?,  url_image_profil = ? WHERE utilisateur_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, utilisateur.getPseudo());
        preparedStatement.setString(2, utilisateur.getPrenom());
        preparedStatement.setString(3, utilisateur.getNom());
        preparedStatement.setString(4, utilisateur.getEmail());
        preparedStatement.setString(5, motDePasseUtilitaire.hacherMotDePasse(utilisateur.getMotDePasseHash()));

        preparedStatement.setString(6, utilisateur.getUrlImageProfil());
        preparedStatement.setInt(7, utilisateur.getUtilisateurId());
        preparedStatement.executeUpdate();
    }
    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM Utilisateurs WHERE utilisateur_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Evenement> afficherfront() {
        return null;
    }

    public Utilisateur authentifierUtilisateur(String identifiant, String motDePasse) throws SQLException {
        String sql = "SELECT * FROM Utilisateurs WHERE pseudo = ? OR email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, identifiant);
        preparedStatement.setString(2, identifiant);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Utilisateur utilisateur = mapResultSetToUtilisateur(resultSet);
            if (motDePasseUtilitaire.verifierMotDePasse(motDePasse, utilisateur.getMotDePasseHash())) {
                String sqlUpdate = "UPDATE Utilisateurs SET derniere_connexion = CURRENT_TIMESTAMP WHERE utilisateur_id = ?";
                PreparedStatement preparedStatementUpdate = connection.prepareStatement(sqlUpdate);
                preparedStatementUpdate.setInt(1, utilisateur.getUtilisateurId());
                preparedStatementUpdate.executeUpdate();
                return utilisateur; // Authentification réussie
            }
        }
        return null; // Authentification échouée
    }
    public void bloquerDebloquerUtilisateur(int idUtilisateur, boolean estActif) throws SQLException {
        String sql = "UPDATE Utilisateurs SET estActif = ? WHERE utilisateur_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBoolean(1, estActif);
            preparedStatement.setInt(2, idUtilisateur);
            preparedStatement.executeUpdate();
        }
    }
    public void updateProfileImage(int utilisateurId, String imagePath) throws SQLException {
        String sql = "UPDATE Utilisateurs SET url_image_profil = ? WHERE utilisateur_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, imagePath);
            preparedStatement.setInt(2, utilisateurId);
            preparedStatement.executeUpdate();
        }
    }
    public boolean verifyPassword(int userId, String password) throws SQLException {
        String sql = "SELECT mot_de_passe_hash FROM Utilisateurs WHERE utilisateur_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString("mot_de_passe_hash");
                    return motDePasseUtilitaire.verifierMotDePasse(password, hashedPassword);
                }
            }
        }
        return false;
    }
    public void updateResetCode(String email, String resetCode) throws SQLException {
        String sql = "UPDATE Utilisateurs SET reset_code = ? WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, resetCode);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        }
    }
    public int getCurrentUserId(String identifiant) throws SQLException {
        String sql = "SELECT utilisateur_id FROM Utilisateurs WHERE pseudo = ? OR email = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, identifiant);
        preparedStatement.setString(2, identifiant);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("utilisateur_id");
        }
        return -1; // Retourne -1 si l'utilisateur n'est pas trouvé
    }




    public String generateFacialIdentifier(byte[] facialData) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(facialData);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString(); // This is a simplified way to generate a unique string based on facial data
    }

    public void updateFacialData(int userId, String facialIdentifier) throws SQLException {
        String sql = "UPDATE Utilisateurs SET facial_data_hash = ? WHERE utilisateur_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, facialIdentifier);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        }
    }


    public Map<String, Integer> getUserRegistrationsPerMonth() throws SQLException {
        Map<String, Integer> registrations = new LinkedHashMap<>();
        String sql = "SELECT YEAR(date_inscription) as year, MONTH(date_inscription) as month, COUNT(*) as count FROM Utilisateurs GROUP BY YEAR(date_inscription), MONTH(date_inscription) ORDER BY year, month";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String monthYear = resultSet.getInt("month") + "/" + resultSet.getInt("year");
                registrations.put(monthYear, resultSet.getInt("count"));
            }
        }
        return registrations;
    }

    public Map<String, Long> getLastConnectionDays() throws SQLException {
        Map<String, Long> lastConnections = new LinkedHashMap<>();
        String sql = "SELECT pseudo, derniere_connexion FROM Utilisateurs";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String user = resultSet.getString("pseudo");
                Timestamp lastLogin = resultSet.getTimestamp("derniere_connexion");
                long daysSinceLastLogin = lastLogin != null ? ChronoUnit.DAYS.between(lastLogin.toLocalDateTime(), LocalDateTime.now()) : -1; // Use -1 or other logic to handle nulls appropriately
                lastConnections.put(user, daysSinceLastLogin);
            }
        }
        return lastConnections;
    }
    public boolean verifyResetCode(String email, String resetCode) throws SQLException {
        System.out.println("hi");
        String sql = "SELECT reset_code FROM Utilisateurs WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedCode = resultSet.getString("reset_code");
                    System.out.println(storedCode+" "+resetCode);
                    return resetCode.equals(storedCode);
                }
            }
        }
        return false;
    }
    public void updatePasswordByEmail(String email, String newPassword) throws SQLException {
        String hashedPassword = motDePasseUtilitaire.hacherMotDePasse(newPassword);
        String sql = "UPDATE Utilisateurs SET mot_de_passe_hash = ?, reset_code = null WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        }
    }

    public String getPhoneNumberByEmail(String email) throws SQLException {
        String sql = "SELECT phone_number FROM Utilisateurs WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email.trim());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("phone_number");
                }
            }
        }
        return null;
    }


}
