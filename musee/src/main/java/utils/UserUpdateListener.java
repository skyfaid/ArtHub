package utils;

import entities.Utilisateur;

public interface UserUpdateListener {
    void onUserUpdated(Utilisateur updatedUser);
}
