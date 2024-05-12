package services;

import entities.Evenement;
import entities.Formations;
import utils.MyDataBase;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class FormationService implements ServiceCrud<Formations> {
    private final Connection connection;
    public FormationService() {
        connection = MyDataBase.getInstance().getConnection();
    }
    @Override
    public void ajouter(Formations f) throws SQLException {
        String sql ="insert into formations(nbr_participants,lien,date_fin,date_debut)"
                +"values('"+f.getNbr_participants()+"','" + f.getLien()+"','"+f.getDate_debut()+"','"+f.getDate_fin()+"')";


        Statement st = connection.createStatement();
        st.executeUpdate(sql);

    }

    @Override
    public void modifier(Formations f) throws SQLException {
        String sql = "update formations set nbr_participants = ?, lien = ?, date_debut= ?, date_fin= ? ";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,f.getNbr_participants());
        ps.setString(2,f.getLien());
        ps.setString(3, f.getDate_debut());
        ps.setString(4, f.getDate_fin());
        ps.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM formations WHERE id= ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<Evenement> afficherfront() {
        return null;
    }

    @Override
    public Formations recupererById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Formations> recuperer() throws SQLException {
        String sql = "select * from formations";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Formations> form = new ArrayList<>();
        while(rs.next()) {
            Formations f=new Formations();
            f.setId(rs.getInt("id"));
            f.setNbr_participants(rs.getInt("nbr_participants"));
            f.setLien(rs.getString("lien"));
            f.setDate_debut(rs.getString("date_debut"));
            f.setDate_fin(rs.getString("date_fin"));
            form.add(f);
        }
        return form;
    }
    }

