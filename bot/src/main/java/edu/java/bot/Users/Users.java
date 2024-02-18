package edu.java.bot.Users;

import java.util.ArrayList;
import java.util.List;

public class Users {
    public List<User> UsersList = new ArrayList<>();

    public boolean find(User NewUser){
    for(var user:UsersList){
        if (user.id().equals(NewUser.id())) return true;
    }
    return false;
    }
}
