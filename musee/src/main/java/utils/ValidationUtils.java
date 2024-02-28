package utils;

import entities.Article;

import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean isValidName(String name) {
        String regex = "^[A-Za-z]{1,30}$";
        return name != null && Pattern.matches(regex, name);
    }

    public static boolean isValidPseudo(String pseudo) {
        String regex = "^[A-Za-z]{3,}[A-Za-z0-9]*$";
        return pseudo != null && Pattern.matches(regex, pseudo);
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email != null && Pattern.matches(regex, email);
    }
    public static boolean isValidPassword(String password, String confirmPassword) {
        return password != null && !password.trim().isEmpty() && confirmPassword != null && password.equals(confirmPassword);
    }
    public static boolean isValidTitle(String title) {
        return title != null && !title.trim().isEmpty() && title.length() <= 50;
    }


    public static boolean isValidContent(String content) {
        return content != null && content.length() >= 100;
    }

    public static String validateArticle(Article article) {
        if (!isValidTitle(article.getTitre())) {
            return "Title should not be null or exceed 50 characters.";
        }
        if (!isValidContent(article.getContenu())) {
            return "Content should be at least 100 characters.";
        }
        return null;
    }

}