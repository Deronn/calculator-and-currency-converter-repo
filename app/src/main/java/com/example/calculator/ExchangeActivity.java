package com.example.calculator;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class ExchangeActivity extends AppCompatActivity {

    String baseCurrency = "EUR";
    String convertedCurrency = "USD";
    double conversionRate = 0;
    Context context;
    EditText firstConv;
    EditText secondConv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);



        spinnerSetup();

        firstConv = findViewById(R.id.firstConversion);
        secondConv = findViewById(R.id.secondConversion);




    }



    public void getApiResult() {
        Editable firstConvText = firstConv.getText();

        if (firstConvText != null && firstConvText.length() > 0) {
            String API = "http://api.exchangeratesapi.io/v1/latest?access_key=46df7c72276c70aa14ea36356797f369&base=" + baseCurrency + "&symbols=" + convertedCurrency;

            if (baseCurrency.equals(convertedCurrency)) {
                Toast.makeText(getApplicationContext(), "Please pick different currencies to convert", Toast.LENGTH_SHORT).show();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(API);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                            try {
                                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                                Scanner scanner = new Scanner(in).useDelimiter("\\A");
                                String apiResult = scanner.hasNext() ? scanner.next() : "";

                                JSONObject jsonObject = new JSONObject(apiResult);

                                if (jsonObject.has("rates") && jsonObject.getJSONObject("rates").has(convertedCurrency)) {
                                    conversionRate = Double.parseDouble(jsonObject.getJSONObject("rates").getString(convertedCurrency));

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            double convertedValue = Double.parseDouble(firstConvText.toString()) * conversionRate;
                                            secondConv.setText(String.valueOf(convertedValue));
                                        }
                                    });
                                } else {
                                    Log.e("Main", "Invalid JSON structure: " + apiResult);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Failed to get conversion rate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                Log.e("Main", "Error during API request: " + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Error during API request", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } finally {
                                urlConnection.disconnect();
                            }
                        } catch (Exception e) {
                            Log.e("Main", String.valueOf(e));
                        }
                    }
                }).start();
            }
        }
    }

    public void spinnerSetup () {
        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.currencies));


        spinner1.setAdapter(spinnerArrayAdapter);

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.currencies2));

        spinner2.setAdapter(spinnerArrayAdapter2);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                baseCurrency = parent.getItemAtPosition(position).toString();
                getApiResult();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convertedCurrency = parent.getItemAtPosition(position).toString();
                getApiResult();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
