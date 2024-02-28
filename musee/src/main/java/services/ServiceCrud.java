package services;

import entities.Evenement;
import entities.Participant;

import java.sql.SQLException;
import java.util.List;

public interface ServiceCrud <T>{
    T recupererById(int id)throws SQLException;

    List<T> recuperer()throws SQLException;

    void ajouter(T entity)throws SQLException;

    void modifier(T entity)throws SQLException;

    void supprimer(int id)throws SQLException;


    List<Evenement> afficherfront();
}
