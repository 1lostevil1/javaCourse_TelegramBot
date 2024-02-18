package edu.java.bot.UrlChecker;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlChecker {

public static boolean check(String url) {

    try {
        URL is_link = new URL(url);
        return true;
    } catch (MalformedURLException e) {
        return false;
    }
}

}
