package com.example.ovidiu.kitesurfingapp;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.System.in;

public class DownloadTaskFavorites extends AsyncTask<String, Void, String> {

    String spotId;

    public DownloadTaskFavorites(String spotId) {
        this.spotId = spotId;
    }

    @Override
    protected String doInBackground(String... urls) {

        String result = "";
        URL url;
        HttpsURLConnection urlConnection = null;

        try {

            url = new URL(urls[0]);

            urlConnection = (HttpsURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("token", "u7BglTUIaQ");
            urlConnection.setRequestMethod("POST");

            if(spotId != null && !spotId.equals("")) {

                OutputStream out = urlConnection.getOutputStream();
                byte byteArray[] = ("{\n" +
                        "\t\"spotId\": \"" + spotId + "\"\n" +
                        "}").getBytes();
                out.write(byteArray);
                out.flush();
                out.close();

            }

            InputStream in = urlConnection.getInputStream();

            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while(data != -1) {

                char current = (char) data;

                result += current;

                data = reader.read();

            }

            return result;
        }
        catch (Exception e) {

            e.printStackTrace();

        } finally {
            urlConnection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}

