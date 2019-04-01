package com.example.ovidiu.kitesurfingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FilterActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Toast.makeText(getApplicationContext(), "HAHAH", Toast.LENGTH_LONG).show();

        Intent intent = getIntent();

        final String country = intent.getStringExtra("country");
        final String windProbability = intent.getStringExtra("windProbability");

        TextView countryInput = (TextView) findViewById(R.id.countryInput);
        countryInput.setText(country);

        TextView windProbabilityInput = (TextView) findViewById(R.id.windProbabilityInput);
        windProbabilityInput.setText(windProbability);

        Button applyButton = (Button) findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                TextView countryInput = (TextView) findViewById(R.id.countryInput);
                intent.putExtra("country", countryInput.getText().toString());
                TextView windProbabilityInput = (TextView) findViewById(R.id.windProbabilityInput);
                intent.putExtra("windProbability", windProbabilityInput.getText().toString());
                startActivity(intent);

            }
        });
    }
}
