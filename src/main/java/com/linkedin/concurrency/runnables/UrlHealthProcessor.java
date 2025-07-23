package com.linkedin.concurrency.runnables;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UrlHealthProcessor implements Runnable {

    private Logger logger = Logger.getLogger(UrlHealthProcessor.class.getName());
    private static final String urlName = "http://www.google.com";

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": checking the health of the application.");
        String statusOfApp = "";

        try {
            URL url = new URL(urlName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                statusOfApp = "Yes, it is working!";
            } else {
                statusOfApp = "Sorry! Something went wrong";
            }

            System.out.println("Status of the app: "  + statusOfApp);

        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, null, e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, null, e);
        }
    }
}
