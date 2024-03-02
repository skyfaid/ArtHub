package controllers.Articles;

import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import entities.Article;
import javafx.scene.shape.Circle;
import services.ServiceArticle;
import services.ServiceUtilisateur;
import utils.ValidationUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ArticlesController implements Initializable {

    public VBox onearticle;
    public TextArea contenu;
    public TextField titre;
    public Label mail;
    public ImageView picuprof;
    public Button buttonsave;
    public Button buttondelete;
    public Button buttonedit;
    public TextField seachfield;
    public Button latestButton;
    public Button oldestButton;
    public Button myArticlesButton;


    @FXML
    private VBox articlesContainer;
    private Article selectedArticle=new Article();

    private ServiceArticle articleService = new ServiceArticle();
    private Utilisateur utilisateurConnecte;

    public void setUser(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        updateProfileImage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double radius = picuprof.getFitHeight() / 2;
        Circle clip = new Circle(radius, radius, radius);
        picuprof.setClip(clip);
        onearticle.setVisible(false);
        loadArticles();
        contenu.setWrapText(true);
        seachfield.textProperty().addListener((observable, oldValue, newValue) -> {
            loadArticles(); // This will reload the articles based on the new search text
        });
    }

    private void updateProfileImage() {
        if (utilisateurConnecte != null && utilisateurConnecte.getUrlImageProfil() != null && !utilisateurConnecte.getUrlImageProfil().isEmpty()) {
            picuprof.setImage(new Image(utilisateurConnecte.getUrlImageProfil(), true)); // true for asynchronous loading
        } else {
            picuprof.setImage(new Image(getClass().getResourceAsStream("/images/nopic.png")));
        }
    }

    private void loadArticles() {
        articlesContainer.getChildren().clear(); // Clear previous articles
        try {
            String searchText = seachfield.getText().toLowerCase();
            List<Article> articles = articleService.recuperer();
            for (Article article : articles) {
                if (article.getTitre().toLowerCase().startsWith(searchText)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiArticle/ArticleItem.fxml"));
                    HBox articleBox = loader.load();
                    ArticleItemController controller = loader.getController();
                    controller.setData(article);
                    articleBox.setOnMouseEntered(event -> articleBox.setCursor(Cursor.HAND));
                    // Change cursor back to default when not hovering
                    articleBox.setOnMouseExited(event -> articleBox.setCursor(Cursor.DEFAULT));

                    articleBox.setOnMouseClicked(event -> {
                        // This line sets the selected article and displays its details
                        displayArticleDetails(article);
                    });
                    articlesContainer.getChildren().add(articleBox);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Or handle error appropriately
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL Exception
        }
    }

    private void displayArticleDetails(Article article) {
        selectedArticle.setArticleId(article.getArticleId());
        titre.setText(article.getTitre());
        titre.setEditable(false);
        contenu.setText(article.getContenu());
        contenu.setEditable(false);
        buttonsave.setVisible(false);

        try {
            ServiceUtilisateur ser = new ServiceUtilisateur();
            Utilisateur util = ser.recupererById(article.getUtilisateurId());
            mail.setText(util.getEmail());

            String imagePath = util.getUrlImageProfil();
            if (imagePath != null && !imagePath.isEmpty()) {
                URL imgUrl = getClass().getResource(imagePath);
                if (imgUrl != null) {
                    Image image = new Image(imgUrl.toString(), true); // Load the image asynchronously
                    picuprof.setImage(image);
                } else {
                    // If the image doesn't exist, set a default one
                    picuprof.setImage(new Image(getClass().getResourceAsStream("/images/nopic.png")));
                }
            } else {
                // Set default image if URL is null or empty
                picuprof.setImage(new Image(getClass().getResourceAsStream("/images/nopic.png")));
            }
            if (utilisateurConnecte != null && article.getUtilisateurId() == utilisateurConnecte.getUtilisateurId()) {
                buttonedit.setVisible(true);
                buttondelete.setVisible(true);
            } else {
                buttonedit.setVisible(false);
                buttondelete.setVisible(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onearticle.setVisible(true);
    }



    public void add(ActionEvent actionEvent) {
        titre.clear();
        contenu.clear();
        buttonsave.setVisible(true);
        titre.setEditable(true);
        contenu.setEditable(true);
        // Display the VBox for article input
        onearticle.setVisible(true);
        mail.setText(utilisateurConnecte.getEmail());
        //picuprof.setImage(new Image(getClass().getResourceAsStream(defaultUser.getUrlImageProfil())));
    }
    @FXML
    public void saveArticle() {
        Article newArticle = new Article();
        newArticle.setTitre(titre.getText());
        newArticle.setContenu(contenu.getText());
        newArticle.setUtilisateurId(utilisateurConnecte.getUtilisateurId());
        String validationError = ValidationUtils.validateArticle(newArticle);
        if (validationError != null) {
            showAlert("Validation Error", validationError);
            return;
        }
        try {
            articleService.ajouter(newArticle);
            loadArticles();
            onearticle.setVisible(false);
            showAlert("Success", "The article has been saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while saving the article: " + e.getMessage());
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void editArticle(ActionEvent actionEvent) {
            titre.setEditable(true);
            contenu.setEditable(true);
            buttonsave.setVisible(true);
            // Set the action for saving after editing
            buttonsave.setOnAction(e -> {
                // Update the article with new content from the form fields
                selectedArticle.setTitre(titre.getText());
                selectedArticle.setContenu(contenu.getText());
                selectedArticle.setUtilisateurId(utilisateurConnecte.getUtilisateurId());
                String validationError = ValidationUtils.validateArticle(selectedArticle);
                if (validationError != null) {
                    showAlert("Validation Error", validationError);
                    return;
                }
                try {
                    articleService.modifier(selectedArticle); // Assuming you have a method to update articles
                    loadArticles();
                    showAlert("Success", "Article updated successfully.");
                    onearticle.setVisible(false); // Hide the form after saving
                } catch (SQLException ex) {
                    showAlert("Error", "Failed to update article: " + ex.getMessage());
                }
            });

    }

    @FXML
    public void deleteArticle(ActionEvent actionEvent) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this article?", ButtonType.YES, ButtonType.NO);
        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    articleService.supprimer(selectedArticle.getArticleId());
                    loadArticles();
                    showAlert("Success", "Article deleted successfully.");
                    onearticle.setVisible(false); // Hide the form after deletion
                } catch (SQLException ex) {
                    showAlert("Error", "Failed to delete article: " + ex.getMessage());
                }
            }
        });
    }

    @FXML
    private void loadLatestArticles() {
        articlesContainer.getChildren().clear(); // Clear previous articles
        try {
            List<Article> articles = articleService.recuperer(); // Assuming this method exists and fetches all articles
            articles.sort((a1, a2) -> a2.getDateCreation().compareTo(a1.getDateCreation())); // Sort by creation date descending

            for (Article article : articles) {
                addArticleToContainer(article);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    @FXML
    private void loadOldestArticles() {
        articlesContainer.getChildren().clear(); // Clear previous articles
        try {
            List<Article> articles = articleService.recuperer(); // Assuming this method exists and fetches all articles
            articles.sort((a1, a2) -> a1.getDateCreation().compareTo(a2.getDateCreation())); // Sort by creation date ascending

            for (Article article : articles) {
                addArticleToContainer(article);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    private void addArticleToContainer(Article article) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guiArticle/ArticleItem.fxml"));
        HBox articleBox = loader.load();
        ArticleItemController controller = loader.getController();
        controller.setData(article);
        articleBox.setOnMouseEntered(event -> articleBox.setCursor(Cursor.HAND));
        articleBox.setOnMouseExited(event -> articleBox.setCursor(Cursor.DEFAULT));
        articleBox.setOnMouseClicked(event -> displayArticleDetails(article));
        articlesContainer.getChildren().add(articleBox);
    }

    @FXML
    private void loadMyArticles() {
        articlesContainer.getChildren().clear(); // Clear previous articles
        try {
            List<Article> articles = articleService.recuperer(); // Assuming this method exists and fetches all articles
            articles = articles.stream()
                    .filter(article -> article.getUtilisateurId() == utilisateurConnecte.getUtilisateurId())
                    .collect(Collectors.toList());

            for (Article article : articles) {
                addArticleToContainer(article);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions
        }
    }


}
