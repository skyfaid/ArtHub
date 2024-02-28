package services;

import entities.Article;
import entities.Evenement;
import utils.MyDataBase;
import utils.ValidationUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceArticle implements ServiceCrud<Article> {
    private Connection connection;
    public ServiceArticle(){
        this.connection = MyDataBase.getInstance().getConnection();
    }
    private Article mapResultSetToArticle(ResultSet resultSet) throws SQLException {
        Article article = new Article();
        article.setArticleId(resultSet.getInt("article_id"));
        article.setUtilisateurId(resultSet.getInt("utilisateur_id"));
        article.setTitre(resultSet.getString("titre"));
        article.setContenu(resultSet.getString("contenu"));
        article.setDateCreation(resultSet.getTimestamp("date_creation"));
        article.setDerniereModification(resultSet.getTimestamp("derniere_modification"));
        return article;
    }
    @Override
    public Article recupererById(int id) throws SQLException {
        String sql = "SELECT * FROM Articles WHERE article_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return mapResultSetToArticle(resultSet);
        }
        return null;
    }

    @Override
    public List<Article> recuperer() throws SQLException {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM Articles";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            Article article = mapResultSetToArticle(resultSet);
            articles.add(article);
        }
        return articles;
    }

    @Override
    public void ajouter(Article article) throws SQLException {
        String sql = "INSERT INTO Articles (utilisateur_id, titre, contenu) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, article.getUtilisateurId());
        preparedStatement.setString(2, article.getTitre());
        preparedStatement.setString(3, article.getContenu());
        preparedStatement.executeUpdate();
    }

    @Override
    public void modifier(Article article) throws SQLException {
        String sql = "UPDATE Articles SET  titre = ?, contenu = ?,derniere_modification = CURRENT_TIMESTAMP WHERE article_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, article.getTitre());
        preparedStatement.setString(2, article.getContenu());
        preparedStatement.setInt(3, article.getArticleId());
        preparedStatement.executeUpdate();

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM Articles WHERE article_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }

    }

    @Override
    public List<Evenement> afficherfront() {
        return null;
    }
}
