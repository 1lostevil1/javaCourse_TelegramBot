package edu.java.bot.Users;

import java.util.List;

public record User (String name, Long id, List<String> urls){};
