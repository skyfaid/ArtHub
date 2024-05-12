package entities;

public class Participants {
    private int id,nbr_formations;
    private String date_inscription;

    public Participants() {
    }
    public Participants( int nbr_formations, String date_inscription) {
        this.nbr_formations = nbr_formations;
        this.date_inscription = date_inscription;
    }
    public Participants(int id, int nbr_formations, String date_inscription) {
        this.id = id;
        this.nbr_formations = nbr_formations;
        this.date_inscription = date_inscription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbr_formations() {
        return nbr_formations;
    }

    public void setNbr_formations(int nbr_formations) {
        this.nbr_formations = nbr_formations;
    }

    public String getDate_inscription() {
        return date_inscription;
    }

    public void setDate_inscription(String date_inscription) {
        this.date_inscription = date_inscription;
    }

    @Override
    public String toString() {
        return "Participants{" +
                "id=" + id +
                ", nbr_formations=" + nbr_formations +
                ", date_inscription='" + date_inscription + '\'' +
                '}';
    }
}
