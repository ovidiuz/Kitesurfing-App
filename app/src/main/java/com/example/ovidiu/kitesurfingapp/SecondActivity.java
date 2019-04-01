package com.example.ovidiu.kitesurfingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SecondActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                startActivity(intent);
            }
        });

        final Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        final String name = intent.getStringExtra("name");
        final String isFavorite = intent.getStringExtra("isFavorite");
        final int position = intent.getIntExtra("position", 0);

        ImageButton imageButton = (ImageButton) findViewById(R.id.detailsStar);

        if(isFavorite.equals("true")) {
            imageButton.setImageResource(R.drawable.staron);
        } else {
            imageButton.setImageResource(R.drawable.staroff);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageButton imageButton = (ImageButton) v;

                if(isFavorite.equals("false")) {

                    imageButton.setImageResource(R.drawable.staron);
                    DownloadTaskFavorites downloadTask = new DownloadTaskFavorites(id);
                    downloadTask.execute("https://internship-2019.herokuapp.com/api-spot-favorites-add");

                } else {

                    imageButton.setImageResource(R.drawable.staroff);
                    DownloadTaskFavorites downloadTask = new DownloadTaskFavorites(id);
                    downloadTask.execute("https://internship-2019.herokuapp.com/api-spot-favorites-remove");
                }

            }
        });

        TextView spotNameView = (TextView) findViewById(R.id.spotNameView);
        spotNameView.setText(name);

        DownloadTask downloadTask = new DownloadTask(id);
        downloadTask.execute("https://internship-2019.herokuapp.com/api-spot-get-details");

    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        private String id;

        public DownloadTask(String id) {
            this.id = id;
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

                OutputStream out = urlConnection.getOutputStream();
                byte byteArray[] = ("{\n" +
                        "\t\"spotId\": \"" + id + "\"\n" +
                        "}").getBytes();
                out.write(byteArray);
                out.flush();
                out.close();

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
        public void onPostExecute(String result) {

            super.onPostExecute(result);

            List<String> displayInfo = new ArrayList<>();

            ListView detailsListView = (ListView) findViewById(R.id.detailsListView);

            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonDetails = (JSONObject) jsonObject.get("result");

                String country = "";
                String latitude = "";
                String longitude = "";
                String windProbability = "";
                String whenToGo = "";

                country = jsonDetails.getString("country");
                latitude = jsonDetails.getString("latitude");
                longitude = jsonDetails.getString("longitude");
                windProbability = jsonDetails.getString("windProbability");
                whenToGo = jsonDetails.getString("whenToGo");

                if(country != ""
                        && latitude != ""
                        && longitude != ""
                        && windProbability != ""
                        && whenToGo != "") {

                    displayInfo.add("Country" + "\n" + country + "\r\n");
                    displayInfo.add("Latitude" + "\n" + latitude + "\r\n");
                    displayInfo.add("Longitude" + "\n" + longitude + "\r\n");
                    displayInfo.add("Wind Probability" + "\n" + windProbability + "\r\n");
                    displayInfo.add("When To Go" + "\n" + whenToGo + "\r\n");

                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, displayInfo);
                detailsListView.setAdapter(arrayAdapter);

                final double lat = Double.parseDouble(latitude);
                final double longt = Double.parseDouble(longitude);

                Button mapsButton = (Button) findViewById(R.id.mapsButton);
                mapsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        intent.putExtra("longitude", longt);
                        intent.putExtra("latitude", lat);
                        startActivity(intent);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
