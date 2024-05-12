package services;

import entities.vente;

import java.sql.SQLException;
import java.util.List;

public interface IVente<T> {
    void ajouterVente(vente vente) throws SQLException;
    void modifierVente(vente vente) throws SQLException;
    void supprimerVente(int id) throws SQLException;
    List<vente> recupererToutesVentes() throws SQLException;
  //  vente recupererVenteParId(int id) throws SQLException;

    List<vente> recupererVentesParOeuvreId(int oeuvreId) throws SQLException;

    List<vente> recupererVentesParClient(int idClient) throws SQLException;
    List<vente> recupererVentesParProduit(int idProduit) throws SQLException;
    double calculerTotalVentes() throws SQLException;
    double calculerTotalPrixVentes() throws SQLException;
}
