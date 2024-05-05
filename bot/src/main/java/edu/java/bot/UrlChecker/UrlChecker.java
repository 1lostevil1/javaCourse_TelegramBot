package edu.java.bot.UrlChecker;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class UrlChecker {

    private static Pattern githubRegex = Pattern.compile("^https://github\\.com/[\\w-]+/[\\w-\\.@\\:~]+$");
    private static Pattern gofRegex =
        Pattern.compile("^https://stackoverflow\\.com/questions/\\d+/[\\w-\\.@\\:~]+$");

    public static final int STATUS_OK = 200;

    private UrlChecker() {
    }

    public static boolean check(String uri) {
        URL link;
        boolean result = false;
        try {
            link = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) link.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == STATUS_OK) {
                return (githubRegex.matcher(uri).find()
                    || (gofRegex.matcher(uri).find()));
            }
        } catch (Exception ignored) {

        }
        return result;
    }

}
