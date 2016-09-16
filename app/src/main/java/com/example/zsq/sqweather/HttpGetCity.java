package com.example.zsq.sqweather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zsq on 16-9-1.
 */
public class HttpGetCity {
    public interface HttpCallbackListener{
        void onFinish(String response);
        void onError(Exception e);
    }

    public static void sendRequest(final String urlStr, final HttpCallbackListener callbackListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try {
                    URL url = new URL(urlStr);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (callbackListener != null) {
                        callbackListener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    callbackListener.onError(e);
                }
            }
        }).start();
    }
}
