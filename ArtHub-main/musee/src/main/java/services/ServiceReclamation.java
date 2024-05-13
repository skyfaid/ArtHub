package services;

import entities.Reclamation;
import utils.MyDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceReclamation {
    private Connection connection;

    public ServiceReclamation() {
        this.connection = MyDataBase.getInstance().getConnection();
    }

    public void ajouter(Reclamation reclamation) throws SQLException {
        String sql = "INSERT INTO reclamation (utilisateur_id, oeuvre_id, Description, productPNG, Status, DateSubmitted ,phoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, reclamation.getUtilisateur_id());
            preparedStatement.setInt(2, reclamation.getId());
            preparedStatement.setString(3, reclamation.getDescription()); // Can be null
            if (reclamation.getProductPNG() != null && !reclamation.getProductPNG().isEmpty()) {
                preparedStatement.setString(4, reclamation.getProductPNG());
            } else {
                preparedStatement.setNull(4, Types.VARCHAR); // Handling null for productPNG
            }
            preparedStatement.setString(5, reclamation.getStatus() != null ? reclamation.getStatus() : "pending"); // Default to "pending" if null

            // Corrected to directly use LocalDateTime without converting to the start of the day
            if (reclamation.getDateSubmitted() != null) {
                preparedStatement.setTimestamp(6, Timestamp.valueOf(reclamation.getDateSubmitted()));
            } else {
                // If DateSubmitted is null, use the current LocalDateTime
                preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            }
            preparedStatement.setString(7, reclamation.getPhoneNumber());
            preparedStatement.executeUpdate();
        }
    }



    public List<Reclamation> recuperer() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String sql = "SELECT * FROM reclamation";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setReclamationID(resultSet.getInt("ReclamationID"));
                reclamation.setUtilisateur_id(resultSet.getInt("utilisateur_id"));
                reclamation.setId(resultSet.getInt("oeuvre_id"));
                reclamation.setDescription(resultSet.getString("Description"));
                reclamation.setPhoneNumber(resultSet.getString("phoneNumber"));
                reclamation.setProductPNG(resultSet.getString("productPNG"));
                reclamation.setStatus(resultSet.getString("Status"));
                reclamation.setDateSubmitted(resultSet.getTimestamp("DateSubmitted").toLocalDateTime());
                reclamations.add(reclamation);
            }
        }
        System.out.println("Fetched " + reclamations.size() + " reclamations."); // Debugging line
        return reclamations;
    }

    public void modifier(int ReclamationID, Reclamation reclamation) throws SQLException {
        String sql = "UPDATE reclamation SET utilisateur_id = ?, oeuvre_id = ?, Description = ?, productPNG = ?, Status = ?, DateSubmitted = ? WHERE ReclamationID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, reclamation.getUtilisateur_id());
            preparedStatement.setInt(2, reclamation.getId());
            preparedStatement.setString(3, reclamation.getDescription());
            if (reclamation.getProductPNG() != null && !reclamation.getProductPNG().isEmpty()) {
                preparedStatement.setString(4, reclamation.getProductPNG());
            } else {
                preparedStatement.setNull(4, Types.VARCHAR);
            }
            preparedStatement.setString(5, reclamation.getStatus());
            // Update DateSubmitted to the current timestamp
            preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setInt(7, ReclamationID);

            preparedStatement.executeUpdate();
        }
    }

    public void deleteDependentSolutions(int ReclamationID) throws SQLException {
        // Assuming 'solutions' table has a foreign key named 'ReclamationID' referencing 'reclamations' table
        String deleteSolutionsSQL = "DELETE FROM solutions WHERE ReclamationID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSolutionsSQL)) {
            preparedStatement.setInt(1, ReclamationID);
            preparedStatement.executeUpdate();
        }
        // Note: SQLException must be caught or declared to be thrown
    }
    private void deleteAssociatedSolutions(int ReclamationID) throws SQLException {
        String sql = "DELETE FROM solution WHERE ReclamationID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, ReclamationID);
            preparedStatement.executeUpdate();
        }
    }

    public void supprimer(int ReclamationID) throws SQLException {
        // First, delete any associated solutions
       // deleteAssociatedSolutions(ReclamationID);

        // Then, delete the reclamation itself
        String sql = "DELETE FROM reclamation WHERE ReclamationID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, ReclamationID);
            preparedStatement.executeUpdate();
        }
    }
    public void updateStatus(int ReclamationID, String newStatus) throws SQLException {
        String sql = "UPDATE reclamation SET Status = ? WHERE ReclamationID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, ReclamationID);
            preparedStatement.executeUpdate();
        }
    }
}



