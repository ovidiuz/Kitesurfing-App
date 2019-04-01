package com.example.ovidiu.kitesurfingapp;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.System.in;

public class CustomAdapter extends ArrayAdapter<Spot> {

    public CustomAdapter(Context context, int resource, List<Spot> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        if(listItem == null)
            listItem= LayoutInflater.from(this.getContext()).inflate(R.layout.list_item, parent, false);

        final Spot currentSpot = this.getItem(position);

        final ImageView imageView = (ImageView) listItem.findViewById(R.id.favoriteStar);

        if(currentSpot.getIsFavorite().equals("true")) {
            imageView.setImageResource(R.drawable.staron);
            imageView.setTag(R.drawable.staron);
        } else {
            imageView.setImageResource(R.drawable.staroff);
            imageView.setTag(R.drawable.staroff);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView imgView = (ImageView) v;
                if(R.drawable.staroff == (int)imgView.getTag()) {

                    imgView.setImageResource(R.drawable.staron);
                    imageView.setTag(R.drawable.staron);
                    DownloadTaskFavorites downloadTask = new DownloadTaskFavorites(currentSpot.getId());
                    downloadTask.execute("https://internship-2019.herokuapp.com/api-spot-favorites-add");
                    currentSpot.setIsFavorite("true");

                } else {
                    imgView.setImageResource(R.drawable.staroff);
                    imageView.setTag(R.drawable.staroff);
                    DownloadTaskFavorites downloadTask = new DownloadTaskFavorites(currentSpot.getId());
                    downloadTask.execute("https://internship-2019.herokuapp.com/api-spot-favorites-remove");
                    currentSpot.setIsFavorite("false");
                }

            }
        });

        TextView countryText = (TextView) listItem.findViewById(R.id.countryTextView);
        countryText.setText(currentSpot.getCountry());

        TextView spotText = (TextView) listItem.findViewById(R.id.spotTextView);
        spotText.setText(currentSpot.getName());

        return listItem;

    }
}
