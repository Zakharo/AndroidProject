package com.example.vladzakharo.androidproject;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Vlad Zakharo on 05.01.2017.
 */

public class JsonFetcher {
    private static final String TAG = "JsonFetcher";

    public static String getJsonObject(String urlSpec) {
        String result = "result";
        try {
            URL url = new URL(urlSpec);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = connection.getInputStream();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException(connection.getResponseMessage() +
                            ": with " +
                            urlSpec);
                }
                int bytesRead;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                result = out.toString();
            } finally {
                connection.disconnect();
            }
        } catch (IOException ioe) {
            Log.e(TAG, "JsonFetcher", ioe);
        }
        return result;
    }
}
