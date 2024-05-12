package services;

import entities.categorie;

import java.sql.SQLException;
import java.util.List;

public interface ICategorie<T> {
    categorie consulterProduitParId(int id) throws SQLException;
    List<categorie> consulterTousLesProduits() throws SQLException;
    List<categorie> consulterProduitsParCritere(String critere) throws SQLException;
    List<categorie> consulterProduitsParDisponibilite(boolean disponible) throws SQLException;
}
