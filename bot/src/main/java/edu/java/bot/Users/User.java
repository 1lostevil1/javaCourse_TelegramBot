package edu.java.bot.Users;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class User {
     private final String name;
     private final Long id;
     private List<String> urls;
     public State state;

     public User(String name, Long id){
         this.name = name;
         this.id = id;
         urls = new ArrayList<>();
         state = State.NONE;
     }

     public void addUrl(String url){
         urls.add(url);
     }

     public void removeUrl(String url){
         urls.remove(url);
     }

     public Long getId(){
         return id;
     }

     public String UrlstoString(){
         return urls.stream().collect(Collectors.joining("\n"));
     }

}
