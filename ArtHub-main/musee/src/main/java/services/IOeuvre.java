package services;

import entities.Oeuvre;

import java.sql.SQLException;
import java.util.List;

public interface IOeuvre<T> {
    void ajouterOeuvre(Oeuvre o) throws SQLException;
    void modifierOeuvre(Oeuvre o) throws SQLException;
    void supprimerOeuvre(int id) throws SQLException;
    List<Oeuvre> recupererOeuvres() throws SQLException;
    void consulterOeuvre(Oeuvre o) throws SQLException;
}
