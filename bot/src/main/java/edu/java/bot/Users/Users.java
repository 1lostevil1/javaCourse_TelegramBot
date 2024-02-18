package edu.java.bot.Users;

import java.util.HashMap;
import java.util.Map;

public class Users {
    public Map<Long, User> usersMap = new HashMap<>();

    public boolean find(Long id) {
        if (usersMap.containsKey(id)) {
            return true;
        }
        return false;
    }
}
