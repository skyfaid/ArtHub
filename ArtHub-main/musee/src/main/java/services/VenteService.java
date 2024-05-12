package services;

import entities.vente;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenteService implements IVente<vente>{
    private final Connection connection;

    public VenteService() {
        connection = MyDataBase.getInstance().getConnection();
    }
    @Override
    public void ajouterVente(vente vente) throws SQLException {
        String query = "INSERT INTO vente (ID_OeuvreVendue, DateVente, PrixVente, ModePaiement, quantite) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vente.getID_OeuvreVendue());
            stmt.setDate(2, vente.getDateVente());
            stmt.setDouble(3, vente.getPrixVente());
            stmt.setString(4, vente.getModePaiement());
            stmt.setInt(5, vente.getQuantite());
            stmt.executeUpdate();
        }
    }

    @Override
    public void modifierVente(vente vente) throws SQLException {
        String query = "UPDATE vente SET ID_OeuvreVendue = ?, DateVente = ?, PrixVente = ?, ModePaiement = ? , quantite = ? WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vente.getID_OeuvreVendue());
            stmt.setInt(2, vente.getId_client()); // Ajouté: Prise en compte de id_client
            stmt.setDate(3, vente.getDateVente());
            stmt.setDouble(4, vente.getPrixVente());
            stmt.setString(5, vente.getModePaiement());
            stmt.setInt(6, vente.getQuantite()); // Ajouté: Prise en compte de quantite
            stmt.setInt(7, vente.getID());
            stmt.executeUpdate();
        }
    }
    public void modifierVenteQte(vente vente) throws SQLException {
        String query = "UPDATE vente SET quantite = ? WHERE ID_OeuvreVendue = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(2, vente.getID_OeuvreVendue());
            stmt.setInt(1, vente.getQuantite()); // Ajouté: Prise en compte de quantite
            stmt.executeUpdate();
        }
    }

    @Override
    public void supprimerVente(int id) throws SQLException {
        String query = "DELETE FROM vente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
  public  List<vente> recupererToutesVentes() throws SQLException {
       List<vente> ventes = new ArrayList<>();
        String query = "SELECT * FROM vente";
        try (Statement stmt = connection.createStatement();
           ResultSet resultSet = stmt.executeQuery(query)) {
        while (resultSet.next()) {
          ventes.add(mapToVente(resultSet));
         }
        }
        return ventes;
    }



    @Override
    public List<vente> recupererVentesParOeuvreId(int oeuvreId) throws SQLException {
        List<vente> ventes = new ArrayList<>();
        String query = "SELECT * FROM vente WHERE ID_OeuvreVendue = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, oeuvreId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    ventes.add(mapToVente(resultSet));
                }
            }
        }
        return ventes;
    }

    @Override
   public List<vente> recupererVentesParClient(int idClient) throws SQLException {
       List<vente> ventes = new ArrayList<>();
       String query = "SELECT * FROM vente WHERE id_client = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idClient);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    ventes.add(mapToVente(resultSet));
                }
            }
        }
        return null;
   }

    @Override
    public List<vente> recupererVentesParProduit(int idProduit) throws SQLException {
        List<vente> ventes = new ArrayList<>();
        String query = "SELECT * FROM vente WHERE ID_OeuvreVendue = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {  stmt.setInt(1, idProduit);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    ventes.add(mapToVente(resultSet));
                }
            }
        }
      return null;
  }

    @Override
    public double calculerTotalVentes() throws SQLException {
        String query = "SELECT COUNT(*) AS totalVentes FROM vente";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("totalVentes");
            }
        }
        return 0;
    }

    public double calculerTotalPrixVentes() throws SQLException {
        String query = "SELECT SUM(PrixVente) AS totalPrix FROM vente";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getDouble("totalPrix");
            }
        }
        return 0.0;
    }
    private vente mapToVente(ResultSet resultSet) throws SQLException {
        int ID = resultSet.getInt("ID");
        int ID_OeuvreVendue = resultSet.getInt("ID_OeuvreVendue");
        //int id_client = resultSet.getInt("id_client");
        Date DateVente = resultSet.getDate("DateVente");

        double PrixVente = resultSet.getDouble("PrixVente");
        String ModePaiement = resultSet.getString("ModePaiement");
        int quantite = resultSet.getInt("quantite");
        return new vente(ID, ID_OeuvreVendue, DateVente, PrixVente,ModePaiement , quantite);

    }
    }
