package services;

import entities.Evenement;
import entities.Utilisateur;
import utils.MotDePasseUtilitaire;
import utils.MyDataBase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "INSERT INTO Utilisateurs (pseudo, prenom, nom, email, mot_de_passe_hash, gender, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, utilisateur.getPseudo());
        preparedStatement.setString(2, utilisateur.getPrenom());
        preparedStatement.setString(3, utilisateur.getNom());
        preparedStatement.setString(4, utilisateur.getEmail());
        preparedStatement.setString(5, motDePasseUtilitaire.hacherMotDePasse(utilisateur.getMotDePasseHash()));
        preparedStatement.setString(6, utilisateur.getGender());
        preparedStatement.setString(7, utilisateur.getRole());
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

}
