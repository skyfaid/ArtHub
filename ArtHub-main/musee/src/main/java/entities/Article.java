package entities;

import java.util.Date;

public class Article {

    private int articleId;
    private int utilisateurId;
    private String titre;
    private String contenu;
    private Date dateCreation;
    private Date derniereModification;

    public Article() {
    }

    public Article(int utilisateurId, String titre, String contenu) {
        this.utilisateurId = utilisateurId;
        this.titre = titre;
        this.contenu = contenu;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDerniereModification() {
        return derniereModification;
    }

    public void setDerniereModification(Date derniereModification) {
        this.derniereModification = derniereModification;
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", utilisateurId=" + utilisateurId +
                ", titre='" + titre + '\'' +
                ", contenu='" + contenu + '\'' +
                ", dateCreation=" + dateCreation +
                ", derniereModification=" + derniereModification +
                '}';
    }
}
