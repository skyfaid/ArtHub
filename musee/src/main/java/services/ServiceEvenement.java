package services;

import entities.Evenement;
import entities.Participant;
import utils.DuplicateEventException;
import utils.MyDataBase;
import services.ServiceParticipant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement implements ServiceCrud<Evenement> {

    private final Connection cnx = MyDataBase.getInstance().getConnection();

    private ServiceParticipant serviceParticipant;
    @Override
    public Evenement recupererById(int id) throws SQLException {
        return null;
    }
    // Constructor
    public ServiceEvenement() {
        this.serviceParticipant = new ServiceParticipant(); // Initialize ServiceParticipant
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

    public boolean participer(int eventId, int userId) {
        // Check if user has already participated
        if (serviceParticipant.hasUserParticipated(eventId, userId)) {
            return false; // User already participated
        } else {
            String updateEvent = "UPDATE evenements SET nombrePlaces = nombrePlaces - 1, nombreParticipants = nombreParticipants + 1 WHERE id = ? AND nombrePlaces > 0";
            try {
                cnx.setAutoCommit(false); // Disable auto-commit
                PreparedStatement st = cnx.prepareStatement(updateEvent);
                st.setInt(1, eventId);
                int rowsUpdated = st.executeUpdate();
                if (rowsUpdated > 0) {
                    // Add the new participant to the database using ServiceParticipant
                    serviceParticipant.ajouter(new Participant(eventId, userId)); // Adjust according to your actual constructor
                    cnx.commit(); // Commit the transaction
                    return true;
                } else {
                    cnx.rollback(); // Rollback the transaction
                    return false;
                }
            } catch (SQLException ex) {
                try {
                    cnx.rollback(); // Rollback the transaction
                } catch (SQLException exRollback) {
                    exRollback.printStackTrace();
                }
                ex.printStackTrace();
                return false;
            } finally {
                try {
                    cnx.setAutoCommit(true); // Re-enable auto-commit
                } catch (SQLException exAutoCommit) {
                    exAutoCommit.printStackTrace();
                }
            }
        }
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
