package edu.java.bot.UrlChecker;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class UrlChecker {

    private final Pattern githubRegex = Pattern.compile("^https://github\\.com/[\\w-]+/[\\w-\\.@\\:~]+$");
    private final Pattern sofRegex = Pattern.compile("^https://stackoverflow\\.com/questions/\\d+/[\\w-\\.@\\:~]+$");

    private UrlChecker() {
    }

    public static boolean check(String url) {

        try {
            URL link = new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

}
