package services;

import entities.Solution;
import utils.MyDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceSolution {
    private Connection connection;

    public ServiceSolution() {
        this.connection = MyDataBase.getInstance().getConnection();
    }

    private Solution mapResultSetToSolution(ResultSet resultSet) throws SQLException {
        Solution solution = new Solution();
        solution.setSolutionID(resultSet.getInt("SolutionID"));
        solution.setReclamationID(resultSet.getInt("ReclamationID"));
        solution.setUtilisateur_id(resultSet.getInt("utilisateur_id"));
        solution.setStatus(resultSet.getString("Status"));
        solution.setRefundAmount(resultSet.getFloat("RefundAmount"));
        solution.setAdminFeedback(resultSet.getString("AdminFeedback"));
        solution.setDateResolved(resultSet.getTimestamp("DateResolved"));
        return solution;
    }

    public Solution recupererById(int solutionId) throws SQLException {
        String sql = "SELECT * FROM Solution WHERE SolutionID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, solutionId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return mapResultSetToSolution(resultSet);
        }
        return null;
    }

    public List<Solution> recuperer() throws SQLException {
        List<Solution> solutions = new ArrayList<>();
        String sql = "SELECT * FROM Solution";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            Solution solution = mapResultSetToSolution(resultSet);
            solutions.add(solution);
        }
        return solutions;
    }

    public void ajouter(Solution solution) {
        String sql = "INSERT INTO Solution (ReclamationID, utilisateur_id, Status, AdminFeedback, RefundAmount, DateResolved) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, solution.getReclamationID());
            preparedStatement.setInt(2, solution.getUtilisateur_id());
            preparedStatement.setString(3, solution.getStatus());
            preparedStatement.setString(4, solution.getAdminFeedback());
            preparedStatement.setFloat(5, solution.getRefundAmount());
            preparedStatement.setTimestamp(6, solution.getDateResolved());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating solution failed, no rows affected.");
            }

            // If you want to handle the generated keys (e.g., auto-increment ID)
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    solution.setSolutionID(generatedKeys.getInt(1)); // Assuming 'SolutionID' is an auto-increment field
                } else {
                    throw new SQLException("Creating solution failed, no ID obtained.");
                }
            }
            System.out.println("Solution added successfully: " + solution);
        } catch (SQLException e) {
            System.err.println("Failed to add the solution: " + e.getMessage());
            // Optionally, re-throw the exception if you want to handle it further up the call stack
            // throw e;
        }
    }


    public void modifier(Solution solution) throws SQLException {
        // Set DateResolved to the current timestamp using Java
        solution.setDateResolved(new Timestamp(System.currentTimeMillis()));

        String sql = "UPDATE Solution SET utilisateur_id = ?, Status = ?, AdminFeedback = ?, RefundAmount = ?, DateResolved = ? WHERE SolutionID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, solution.getUtilisateur_id());
            preparedStatement.setString(2, solution.getStatus());
            preparedStatement.setString(3, solution.getAdminFeedback());
            preparedStatement.setFloat(4, solution.getRefundAmount());
            // Use the updated DateResolved from the Solution object
            preparedStatement.setTimestamp(5, solution.getDateResolved());
            preparedStatement.setInt(6, solution.getSolutionID());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating solution failed, no rows affected.");
            }
        }
    }

    public void supprimer(int solutionId) throws SQLException {
        String sql = "DELETE FROM Solution WHERE SolutionID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, solutionId);
        preparedStatement.executeUpdate();
    }






    public List<Solution> getAllSolutions() throws SQLException {
        List<Solution> solutions = new ArrayList<>();
        // Update this SQL query to join with the `reclamation` and `oeuvre` tables and select the `oeuvreId`
        String sql = "SELECT sol.*, rec.id as reclamationId, oeuvre.id as oeuvreId " +
                "FROM solution sol " +
                "JOIN reclamation rec ON sol.ReclamationID = rec.ReclamationID " +
                "JOIN oeuvre ON rec.id = oeuvre.id"; // Adjust this line based on your actual table and column names

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Solution solution = mapResultSetToSolution(resultSet);
                // Assuming the oeuvre ID is fetched with the alias oeuvreId in the SQL query
                solution.setOeuvreId(resultSet.getInt("oeuvreId")); // Set the oeuvreId here
                solutions.add(solution);
            }
        }
        return solutions;
    }


}
