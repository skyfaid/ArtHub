package services;

import entities.Oeuvre;
import utils.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OeuvreService implements IOeuvre<Oeuvre> {
    private  final Connection connection;

    public OeuvreService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouterOeuvre(Oeuvre o) throws SQLException {
        String query = "INSERT INTO oeuvre (titre, description, disponibilite, type, posterUrl, dateCreation, prix) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, o.getTitre());
            stmt.setString(2, o.getDescription());
            stmt.setString(3, o.getDisponibilite());
            stmt.setString(4, o.getType());
            stmt.setString(5, o.getPosterUrl());
            stmt.setObject(6, o.getDateCreation()); // Using setObject for LocalDate
            stmt.setDouble(7, o.getPrix());
            stmt.executeUpdate();
        }
    }

    @Override
    public void modifierOeuvre(Oeuvre o) throws SQLException {
        String query = "UPDATE oeuvre SET titre=?, description=?, disponibilite=?, type=?, posterUrl=?, dateCreation=?, prix=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, o.getTitre());
            stmt.setString(2, o.getDescription());
            stmt.setString(3, o.getDisponibilite());
            stmt.setString(4, o.getType());
            stmt.setString(5, o.getPosterUrl());
            stmt.setDate(6, Date.valueOf(o.getDateCreation())); // Convert LocalDate to java.sql.Date
            stmt.setDouble(7, o.getPrix());
            stmt.setInt(8, o.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void supprimerOeuvre(int id) throws SQLException {
        String query = "DELETE FROM oeuvre WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public  List<Oeuvre> recupererOeuvres() {
        List<Oeuvre> listOeuvre = new ArrayList<>();
        String req = "SELECT * FROM oeuvre";
        try {
            PreparedStatement st = connection.prepareStatement(req);
            ResultSet result = st.executeQuery();
            while (result.next()) {
                LocalDate dateCreation = result.getDate("dateCreation") != null ? result.getDate("dateCreation").toLocalDate() : null;
                Oeuvre o = new Oeuvre(
                        result.getInt("id"),
                        result.getString("titre"),
                        result.getString("description"),
                        result.getString("disponibilite"),
                        result.getString("type"),
                        dateCreation,
                        result.getDouble("prix"),
                        result.getString("posterUrl")
                );
                listOeuvre.add(o);
            }
            System.out.println("Oeuvres retrieved successfully!");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return listOeuvre;
    }

    @Override
    public void consulterOeuvre(Oeuvre o) throws SQLException {
        // Implementation for consulting an artwork
    }

    public List<Oeuvre> rechercherOeuvres(String recherche) {
        List<Oeuvre> listOeuvre = new ArrayList<>();
        String req = "SELECT * FROM oeuvre WHERE titre LIKE ?";
        try {
            PreparedStatement st = connection.prepareStatement(req);
            st.setString(1, "%" + recherche + "%"); // Recherche partielle
            ResultSet result = st.executeQuery();
            while (result.next()) {
                LocalDate dateCreation = result.getDate("dateCreation") != null ? result.getDate("dateCreation").toLocalDate() : null;
                Oeuvre o = new Oeuvre(
                        result.getInt("id"),
                        result.getString("titre"),
                        result.getString("description"),
                        result.getString("disponibilite"),
                        result.getString("type"),
                        dateCreation,
                        result.getDouble("prix"),
                        result.getString("posterUrl")
                );
                listOeuvre.add(o);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return listOeuvre;
    }

}
