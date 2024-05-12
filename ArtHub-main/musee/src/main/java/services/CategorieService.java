package services;

import entities.categorie;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieService implements ICategorie<categorie> {
    private final Connection connection;

    public CategorieService() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public categorie consulterProduitParId(int id) throws SQLException {
      //  String query = "SELECT * FROM categorie WHERE idCategorie = ?";
     //   try (PreparedStatement stmt = connection.prepareStatement(query)) {
          //  stmt.setInt(1, id);
           // try (ResultSet resultSet = stmt.executeQuery()) {
             //   if (resultSet.next()) {
                  //  return mapToCategorie(resultSet);
              //  }
           // }
      //  }
        return null;
    }

    @Override
    public List<categorie> consulterTousLesProduits() throws SQLException {
       List<categorie> categories = new ArrayList<>();
       String query = "SELECT * FROM categorie";
       try (Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query)) {
              while (resultSet.next()) {
                categories.add(mapToCategorie(resultSet));
              }
       }
        return categories;
    }

    @Override
    public List<categorie> consulterProduitsParCritere(String critere) throws SQLException {
        List<categorie> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie WHERE nom LIKE ?";
      try (PreparedStatement stmt = connection.prepareStatement(query)) {
       stmt.setString(1, "%" + critere + "%");
        try (ResultSet resultSet = stmt.executeQuery()) {
       while (resultSet.next()) {
      categories.add(mapToCategorie(resultSet));
         }
       }
        }
        return null;
    }

    @Override
    public List<categorie> consulterProduitsParDisponibilite(boolean disponible) throws SQLException {
        // Cette méthode n'a pas de sens pour la classe de service des catégories, car elle concerne les produits
        // Par conséquent, nous laissons la méthode retourner null
        return null;
    }

    private categorie mapToCategorie(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("idCategorie");
        String nom = resultSet.getString("nom");
      String description = resultSet.getString("description");
        return new categorie(id, nom, description);
    }
}
