package utils;

import entities.Utilisateur;

public class UserConnected {
    private static Utilisateur user;
    public static void setUser(Utilisateur u){
        user=u;
    }

    public static Utilisateur getUser() {
        return user;
    }
}
