package services;

import entities.Evenement;
import entities.Participant;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceParticipant implements ServiceCrud<Participant> {

    private Connection cnx = MyDataBase.getInstance().getConnection();

    @Override
    public Participant recupererById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Participant> recuperer() throws SQLException {
        return null;
    }

    @Override
    public void ajouter(Participant p) {
        String req = "INSERT INTO participants(event_id, participant_id, utilisateur_id) VALUES(?,?,?)";
        try {
            PreparedStatement st = cnx.prepareStatement(req);
            st.setInt(1, p.getEvent_id());
            st.setInt(2, p.getParticipant_id());
            st.setInt(3, p.getUtilisateur_id());
            st.executeUpdate();
            System.out.println("Participant added successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Participant p) {
        String req = "UPDATE participants SET event_id = ?, utilisateur_id = ? WHERE participant_id = ?";
        try {
            PreparedStatement st = cnx.prepareStatement(req);
            st.setInt(1, p.getEvent_id());
            st.setInt(2, p.getUtilisateur_id());
            st.setInt(3, p.getParticipant_id());
            st.executeUpdate();
            System.out.println("Participant modified successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
    }

    public void supprimer(Participant p) {
        String req = "DELETE FROM participants WHERE participant_id=?";
        try {
            PreparedStatement st = cnx.prepareStatement(req);
            st.setInt(1, p.getParticipant_id());
            st.executeUpdate();
            System.out.println("Participant deleted successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Evenement> afficherfront() {
        return null;
    }

    public List<Participant> afficher() {
        List<Participant> listParticipant = new ArrayList<>();
        String req = "SELECT p.*, u.nom AS user_nom, u.prenom AS user_prenom, u.gender AS user_gender, e.nom AS event_nom " +
                "FROM participants p " +
                "JOIN utilisateurs u ON p.utilisateur_id = u.utilisateur_id " +
                "JOIN evenements e ON p.event_id = e.id";
        try {
            PreparedStatement st = cnx.prepareStatement(req);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                listParticipant.add(new Participant(
                        rs.getInt("event_id"),
                        rs.getInt("participant_id"),
                        rs.getInt("utilisateur_id"),
                        rs.getString("event_nom"),  // Event name
                        rs.getString("user_nom"),   // User last name
                        rs.getString("user_prenom"),// User first name
                        rs.getString("user_gender"))); // User gender - Make sure this matches your database column name for gender.
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listParticipant;
    }


    public boolean hasUserParticipated(int eventId, int userId) {
        String query = "SELECT count(*) FROM participants WHERE event_id = ? AND utilisateur_id = ?";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(query);
            preparedStatement.setInt(1, eventId);
            preparedStatement.setInt(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}

