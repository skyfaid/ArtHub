package services;

import entities.Evenement;
import utils.DuplicateEventException;
import utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement implements ServiceCrud<Evenement> {

    private final Connection cnx = MyDataBase.getInstance().getConnection();


    @Override
    public Evenement recupererById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Evenement> recuperer() throws SQLException {
        return null;
    }

    @Override
    public void ajouter(Evenement e) {
        String req = "INSERT INTO evenements(nom, datedebut, datefin, lieu, type, description, nombrePlaces, posterUrl) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement st = cnx.prepareStatement(req);
            st.setString(1, e.getNom());
            st.setDate(2, java.sql.Date.valueOf(e.getDatedebut())); // Convert LocalDate to java.sql.Date for datedebut
            st.setDate(3, java.sql.Date.valueOf(e.getDatefin())); // Convert LocalDate to java.sql.Date for datefin
            st.setString(4, e.getLieu());
            st.setString(5, e.getType());
            st.setString(6, e.getDescription());
            st.setInt(7, e.getNombrePlaces());
            st.setString(8, e.getPosterUrl()); // Set the poster URL
            st.executeUpdate();
            System.out.println("Event added successfully!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("unique_event_constraint")) {
                throw new DuplicateEventException("Kindly change the event's name", ex);
            } else {
                System.out.println(ex.getMessage());
            }
        }
    }
  /*@Override
  public void ajouter(Evenement e) {
      String req = "INSERT INTO evenements(nom, datedebut, datefin, lieu, type, description, nombrePlaces, posterUrl) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
      try {
          PreparedStatement st = cnx.prepareStatement(req);
          st.setString(1, e.getNom());
          st.setDate(2, java.sql.Date.valueOf(e.getDatedebut())); // Convert LocalDate to java.sql.Date for datedebut
          st.setDate(3, java.sql.Date.valueOf(e.getDatefin())); // Convert LocalDate to java.sql.Date for datefin
          st.setString(4, e.getLieu());
          st.setString(5, e.getType());
          st.setString(6, e.getDescription());
          st.setInt(7, e.getNombrePlaces());
          st.setString(8, e.getPosterUrl()); // Set the poster URL
          st.executeUpdate();
          System.out.println("Event added successfully!");
      } catch (SQLException ex) {
          System.out.println(ex.getMessage());
      }
  }*/

    @Override
    public void modifier(Evenement e) {
        String req = "UPDATE evenements SET nom = ?, lieu = ?, type = ?, description = ?, nombrePlaces = ?, posterUrl = ? WHERE id = ?";
        try {
            PreparedStatement st = cnx.prepareStatement(req);
            st.setString(1, e.getNom());
            st.setString(2, e.getLieu());
            st.setString(3, e.getType());
            st.setString(4, e.getDescription());
            st.setInt(5, e.getNombrePlaces());
            st.setString(6, e.getPosterUrl()); // Set the poster URL
            st.setInt(7, e.getId());
            st.executeUpdate();
            System.out.println("Event modified successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {

    }

    public void supprimer(Evenement e) {
        String req = "DELETE FROM evenements WHERE id = ?";
        try {
            PreparedStatement st = cnx.prepareStatement(req);
            st.setInt(1, e.getId());
            st.executeUpdate();
            System.out.println("Event deleted successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public List<Evenement> afficher() {
        return null;
    }

    public List<Evenement> afficherback() {
        List<Evenement> listEvenement = new ArrayList<>();
        String req = "SELECT *, (SELECT COUNT(*) FROM participants WHERE event_id = evenements.id) AS nombreParticipants FROM evenements";
        try {
            PreparedStatement st = cnx.prepareStatement(req);
            ResultSet result = st.executeQuery();
            while (result.next()) {
                LocalDate datedebut = result.getDate("datedebut") != null ? result.getDate("datedebut").toLocalDate() : null;
                LocalDate datefin = result.getDate("datefin") != null ? result.getDate("datefin").toLocalDate() : null;
                Evenement e = new Evenement(
                        result.getInt("id"),
                        result.getString("nom"),
                        datedebut,
                        datefin,
                        result.getString("lieu"),
                        result.getString("type"),
                        result.getString("description"),
                        result.getInt("nombrePlaces"),
                        result.getString("posterUrl"),
                        result.getInt("nombreParticipants") // Get the participant count directly from the query
                );
                listEvenement.add(e);
            }
            System.out.println("Events retrieved successfully!");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return listEvenement;
    }

    public List<Evenement> afficherfront() {
        List<Evenement> listEvenement = new ArrayList<>();
        String req = "SELECT * FROM evenements";
        try {
            PreparedStatement st = cnx.prepareStatement(req);
            ResultSet result = st.executeQuery();
            while (result.next()) {
                LocalDate datedebut = result.getDate("datedebut") != null ? result.getDate("datedebut").toLocalDate() : null;
                LocalDate datefin = result.getDate("datefin") != null ? result.getDate("datefin").toLocalDate() : null;
                Evenement e = new Evenement(
                        result.getInt("id"),
                        result.getString("nom"),
                        datedebut,
                        datefin,
                        result.getString("lieu"),
                        result.getString("type"),
                        result.getString("description"),
                        result.getInt("nombrePlaces"),
                        result.getString("posterUrl")); // Get the poster URL
                listEvenement.add(e);
            }
            System.out.println("Events retrieved successfully!");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return listEvenement;
    }

    public boolean participer(int eventId, int currentUserId) {
        String checkParticipation = "SELECT count(*) FROM participants WHERE event_id = ? AND user_id = ?";
        String insertParticipation = "INSERT INTO participants(event_id, user_id) VALUES(?, ?)";
        String deleteParticipation = "DELETE FROM participants WHERE event_id = ? AND user_id = ?";
        try {
            // Check if the user is already a participant
            PreparedStatement checkStmt = cnx.prepareStatement(checkParticipation);
            checkStmt.setInt(1, eventId);
            checkStmt.setInt(2, currentUserId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                // User is not a participant, add them
                PreparedStatement insertStmt = cnx.prepareStatement(insertParticipation);
                insertStmt.setInt(1, eventId);
                insertStmt.setInt(2, currentUserId);
                int inserted = insertStmt.executeUpdate();
                return inserted > 0; // Return true if the user was successfully added
            } else if (rs.getInt(1) > 0) {
                // User is already a participant, remove them
                PreparedStatement deleteStmt = cnx.prepareStatement(deleteParticipation);
                deleteStmt.setInt(1, eventId);
                deleteStmt.setInt(2, currentUserId);
                int deleted = deleteStmt.executeUpdate();
                return deleted > 0; // Return true if the user was successfully removed
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false; // Return false if neither adding nor removing was successful
    }


    public int getParticipantCount(int eventId) {
        String query = "SELECT nombreParticipants FROM evenements WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, eventId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("nombreParticipants");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0; // Default value if participant count retrieval fails
    }


}
