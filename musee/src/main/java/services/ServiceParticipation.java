package services;

import entities.Activite;
import entities.Evenement;
import entities.Participation;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public  class ServiceParticipation implements ServiceCrud<Participation> {
    private Connection connection;

    public ServiceParticipation() {
        this.connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Participation participation) throws SQLException {
        String sql = "INSERT INTO Participation (id_activite, utilisateur_id, score, participation_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, participation.getId_activite());
            preparedStatement.setInt(2, participation.getUtilisateur_id());
            preparedStatement.setInt(3, participation.getScore());
            preparedStatement.setDate(4, new Date(participation.getParticipation_date().getTime()));
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void modifier(Participation entity) throws SQLException {

    }

    @Override
    public void supprimer(int id) throws SQLException {

    }

    @Override
    public List<Evenement> afficherfront() {
        return null;
    }

    @Override
    public Participation recupererById(int id) throws SQLException {
        return null;
    }

    public List<Participation> recuperer() throws SQLException {
        List<Participation> participations = new ArrayList<>();
        String sql = "SELECT * FROM participation";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Participation participation = new Participation(
                        resultSet.getInt("id_participation"),
                        resultSet.getInt("id_activite"),
                        resultSet.getInt("utilisateur_id"),
                        resultSet.getInt("score"),
                        resultSet.getDate("participation_date"));
                participations.add(participation);
            }
        }
        return participations;
    }

    public void modifier(String ancienNom, Activite activite) throws SQLException {
    }


    public void supprimer(String nomActivite) throws SQLException {
    }

    /*
    public void supprimer(int idParticipation) throws SQLException {
        String sql = "DELETE FROM Participation WHERE id_participation = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idParticipation);
            preparedStatement.executeUpdate();
        }
    }
*/

    public List<Participation> recuperera(int i) throws SQLException{
        List<Participation> top3participations = new ArrayList<>();
        String sql = "SELECT * FROM participation ORDER BY Score DESC LIMIT 3";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Participation participation = new Participation(
                        resultSet.getInt("id_participation"),
                        resultSet.getInt("id_activite"),
                        resultSet.getInt("utilisateur_id"),
                        resultSet.getInt("score"),
                        resultSet.getDate("participation_date"));
                top3participations.add(participation);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return top3participations;
    }











    public  List<Participation> getTopParticipantsForActivity(int activityId, int limit) throws SQLException {
        List<Participation> topParticipants = new ArrayList<>();
        String sql = "SELECT u.name, p.score FROM participation p JOIN utilisateurs u ON p.utilisateur_id = u.id WHERE p.id_activite = ? ORDER BY p.score DESC LIMIT 3";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, activityId);
            preparedStatement.setInt(2, limit);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Participation participation = new Participation(
                        resultSet.getInt("id_participation"),
                        resultSet.getInt("id_activite"),
                        resultSet.getInt("utilisateur_id"),
                        resultSet.getInt("score"),
                        resultSet.getDate("participation_date")
                );
                topParticipants.add(participation);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            throw e;
        }

        return topParticipants;
    }
}




