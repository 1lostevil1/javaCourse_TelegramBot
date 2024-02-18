package edu.java.bot.Users;

import java.util.ArrayList;
import java.util.List;

public class Users {
    public List<User> usersList = new ArrayList<>();

    public boolean find(User newUser) {
        for (var user : usersList) {
            if (user.id().equals(newUser.id())) {
                return true;
            }
        }
        return false;
    }
}
