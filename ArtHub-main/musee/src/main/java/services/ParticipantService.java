package services;

import entities.Evenement;
import entities.Participants;
import utils.MyDataBase;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipantService implements ServiceCrud<Participants> {

    private final Connection connection;
    public ParticipantService(){connection= MyDataBase.getInstance().getConnection();}


    @Override
    public void ajouter(Participants p) throws SQLException {
        String sql ="insert into participant(date_inscription,nbr_formations)"
                +"values('"+p.getDate_inscription()+"','" + p.getNbr_formations()+"')";

        Statement st = connection.createStatement();
        st.executeUpdate(sql);

    }

    @Override
    public void modifier(Participants p) throws SQLException {
        String sql = "update participant set date_inscription = ?, nbr_formations = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, p.getDate_inscription());
        ps.setInt(2, p.getNbr_formations());

        ps.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM participant WHERE id= ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public List<Evenement> afficherfront() {
        return null;
    }

    @Override
    public Participants recupererById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Participants> recuperer() throws SQLException {
        String sql = "select * from participant";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Participants> part = new ArrayList<>();
        while(rs.next()) {
            Participants p=new Participants();
            p.setId(rs.getInt("id"));
            p.setDate_inscription(rs.getString("date_inscription"));
            p.setNbr_formations(rs.getInt("nbr_formations"));
            part.add(p);
        }
        return part;
    }
    }

