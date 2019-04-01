package com.example.ovidiu.kitesurfingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    ListView spotsListView;
    String country = "";
    String windProbability = "";
    public static CustomAdapter adapter;

    public CustomAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imgButton = (ImageButton) findViewById(R.id.imageButton);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                intent.putExtra("country", country);
                intent.putExtra("windProbability", windProbability);
                startActivity(intent);
            }
        });

        try {
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute("https://internship-2019.herokuapp.com/api-spot-get-all");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

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

                Intent intent = getIntent();
                country = intent.getStringExtra("country");
                windProbability = intent.getStringExtra("windProbability");

                if(country == null)
                    country = "";
                if(windProbability == null)
                    windProbability = "";

                if(country != null && windProbability != null) {

                    OutputStream out = urlConnection.getOutputStream();
                    String x = (windProbability.equals("") ? "\"\"" : windProbability);

                    byte byteArray[] = ("{\n" +
                            "\t\"country\": \"" + country + "\",\n" +
                            "\t\"windProbability\": " + x
                            + "\n" + "}").getBytes();
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
        public void onPostExecute(String result) {

            super.onPostExecute(result);

            spotsListView = (ListView) findViewById(R.id.spotsListView);

            final List<Spot> spots = new ArrayList<>();

            try {

                JSONObject jsonObject = new JSONObject(result);

                String resultInfo = jsonObject.getString("result");

                JSONArray jsonArray = new JSONArray(resultInfo);

                for (int i = 0; i < jsonArray.length(); i ++) {

                    JSONObject jsonPart = jsonArray.getJSONObject(i);

                    Spot newSpot = new Spot(jsonPart);

                    if (newSpot.getId() != ""
                            && newSpot.getCountry() != ""
                            && newSpot.getIsFavorite() != ""
                            && newSpot.getName() != ""
                            && newSpot.getWhenToGo() != "") {

                        spots.add(newSpot);

                    }

                }

                adapter = new CustomAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, spots);
                spotsListView.setAdapter(adapter);

                spotsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Spot spot = spots.get(position);

                        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                        intent.putExtra("id", spot.getId());
                        intent.putExtra("name", spot.getName());
                        intent.putExtra("country", spot.getCountry());
                        intent.putExtra("whenToGo", spot.getWhenToGo());
                        intent.putExtra("isFavorite", spot.getIsFavorite());
                        intent.putExtra("position", position);

                        startActivity(intent);
                    }
                });

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
