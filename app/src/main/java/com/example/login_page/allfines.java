package com.example.login_page;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



public class allfines extends AppCompatActivity {

    private Button Back;
    JSONArray list;

    String name;

    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.all_fines);



        Back = findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(allfines.this, Home.class);
                startActivity(intent);
            }
        });

        c = this;

        // Call AsyncTask to get offence list
        GetOffenceListTask task = new GetOffenceListTask();
        task.execute();
    }

    protected JSONArray getOffenceList() throws JSONException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJsonString = null;
        System.out.println("Do in Background");

        final String BASE_URL = "http://10.0.2.2:8080/ntsf_backend_war/offenceList";

        StringBuilder builder;
        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader((new InputStreamReader(inputStream)));

            builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            bookJsonString = builder.toString();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(bookJsonString);
        JSONObject jsonObject = new JSONObject(bookJsonString);
        JSONArray offencesArray = jsonObject.getJSONArray("offences");



        return offencesArray;

    }

    private class GetOffenceListTask extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String bookJsonString = null;
            System.out.println("Do in Background");

            final String BASE_URL = "http://10.0.2.2:8080/ntsf_backend_war/offenceList";

            StringBuilder builder;
            try {
                URL url = new URL(BASE_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader((new InputStreamReader(inputStream)));

                builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                bookJsonString = builder.toString();

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(bookJsonString);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            JSONArray offencesArray = null;
            try {
                offencesArray = jsonObject.getJSONArray("offences");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            return offencesArray;
        }

        @Override
        protected void onPostExecute(JSONArray offencesArray) {
            super.onPostExecute(offencesArray);
            list = offencesArray;


            LinearLayout layoutDriver = findViewById(R.id.driver_fines);
            LinearLayout layoutVehicle = findViewById(R.id.vehicle_fines);
            LinearLayout layoutPedestrian = findViewById(R.id.pedestrian_fines);

            JSONArray jsonArrayD = new JSONArray();
            JSONArray jsonArrayV = new JSONArray();
            JSONArray jsonArrayP = new JSONArray();

            for (int i = 0; i < list.length(); i++) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = (JSONObject) list.get(i);
                    System.out.println(i+1+" Json: "+jsonObject);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                String offenceType = null;
                try {
                    offenceType = jsonObject.getString("offenceType");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                if (offenceType.equals("driver")) {
                    jsonArrayD.put(jsonObject);
                } else if (offenceType.equals("vehicle")) {
                    jsonArrayV.put(jsonObject);
                } else if (offenceType.equals("pedestrian")) {
                    jsonArrayP.put(jsonObject);
                }
            }

            int amount;
            String offenceType;
            String description;
            int pointWeight;
            int offenceNo;
            JSONObject jsonObject;


//            TextView nameMessage = findViewById(R.id.name1);
//            nameMessage.setText(name);


            JSONArray jsonArray = null;
            jsonArray = list;

            for (int i = 0; i < jsonArrayD.length(); i++) {

                try {
                    jsonObject = jsonArrayD.getJSONObject(i);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


                try {
                    amount = jsonObject.getInt("amount");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    offenceType = jsonObject.getString("offenceType");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    description = jsonObject.getString("description");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    pointWeight = jsonObject.getInt("pointWeight");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    offenceNo = jsonObject.getInt("offenceNo");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


//                TextView textView = new TextView(c);
//                textView.setText(i+1+". "+"\n"+
//                                "Offence No     :  " + offenceNo + "\n"+
//                                "Offence Type  :  " + offenceType + "\n" +
//                                "Description     :  " + description + "\n" +
//                                "Point Weight   : " + pointWeight + "\n"+
//                                "Amount           :  LKR  " + amount  + " /=" + "\n" );
//
//
//                textView.setTextColor(Color.BLACK);
//                layoutDriver.addView(textView);

                // Create a new CardView
                CardView cardView = new CardView(c);
                CardView.LayoutParams layoutParams = new CardView.LayoutParams(
                        CardView.LayoutParams.MATCH_PARENT,
                        CardView.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(12, 12, 12, 12); // Set margin bottom to 16dp
                cardView.setLayoutParams(layoutParams);
                cardView.setCardBackgroundColor(Color.parseColor("#C2E9BF"));
                cardView.setRadius(16);
                cardView.setContentPadding(32, 32, 32, 32);
                cardView.setCardElevation(8);
                cardView.setMaxCardElevation(16);

                // Create a new LinearLayout to hold the TextViews for each offence
                LinearLayout linearLayout = new LinearLayout(c);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                linearLayout.setOrientation(LinearLayout.VERTICAL);

//                // Create TextViews for each offence
//                TextView textViewTitle = new TextView(c);
//                textViewTitle.setText("Offence No     :  " + offenceNo);
//                textViewTitle.setTextColor(Color.BLACK);
//                linearLayout.addView(textViewTitle);

                TextView textViewTitle = new TextView(c);
                textViewTitle.setText("Offence No: " + offenceNo);
                textViewTitle.setTextColor(Color.BLACK);
                textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // set font size to 18sp
                //textViewTitle.setTypeface(null, Typeface.BOLD); // set text to bold
                linearLayout.addView(textViewTitle);

                TextView offenceTypeTextView = new TextView(c);
                offenceTypeTextView.setText("Offence Type  :  " + offenceType);
                offenceTypeTextView.setTextColor(Color.BLACK);
                linearLayout.addView(offenceTypeTextView);

                TextView descriptionTextView = new TextView(c);
                descriptionTextView.setText("Description     :  " + description);
                descriptionTextView.setTextColor(Color.BLACK);
                linearLayout.addView(descriptionTextView);

                TextView pointWeightTextView = new TextView(c);
                pointWeightTextView.setText("Point Weight   : " + pointWeight);
                pointWeightTextView.setTextColor(Color.BLACK);
                linearLayout.addView(pointWeightTextView);

                TextView amountTextView = new TextView(c);
                amountTextView.setText("Amount           :  LKR  " + amount + " /=");
                amountTextView.setTextColor(Color.BLACK);
                linearLayout.addView(amountTextView);

                // Add the LinearLayout to the CardView
                cardView.addView(linearLayout);

                // Add the CardView to the layout for driver offences
                layoutDriver.addView(cardView);







            }

            for (int i = 0; i < jsonArrayV.length(); i++) {

                try {
                    jsonObject = jsonArrayV.getJSONObject(i);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


                try {
                    amount = jsonObject.getInt("amount");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    offenceType = jsonObject.getString("offenceType");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    description = jsonObject.getString("description");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    pointWeight = jsonObject.getInt("pointWeight");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    offenceNo = jsonObject.getInt("offenceNo");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


//                TextView textView = new TextView(c);
//                textView.setText(i+1+". "+"\n"+
//                        "Offence No     :  " + offenceNo + "\n"+
//                        "Offence Type  :  " + offenceType + "\n" +
//                        "Description     :  " + description + "\n" +
//                        "Point Weight   : " + pointWeight + "\n"+
//                        "Amount           :  LKR  " + amount  + " /=" + "\n" );
//                textView.setTextColor(Color.BLACK);
//                layoutVehicle.addView(textView);

                CardView cardView_v = new CardView(c);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(16, 16, 16, 16);
                cardView_v.setLayoutParams(params);
                cardView_v.setCardBackgroundColor(Color.parseColor("#D1E5F4"));
                cardView_v.setRadius(16);

                LinearLayout linearLayout = new LinearLayout(c);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                linearLayout.setPadding(16, 16, 16, 16);

                TextView textViewTitle = new TextView(c);
                textViewTitle.setText("Offence No  :   " + offenceNo);
                textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                textViewTitle.setTextColor(Color.BLACK);

                TextView textViewOffenceType = new TextView(c);
                textViewOffenceType.setText("Offence Type    :   " + offenceType);
                textViewOffenceType.setTextColor(Color.BLACK);

                TextView textViewDescription = new TextView(c);
                textViewDescription.setText("Description        :   " + description);
                textViewDescription.setTextColor(Color.BLACK);

                TextView textViewPointWeight = new TextView(c);
                textViewPointWeight.setText("Point Weight     :   " + pointWeight);
                textViewPointWeight.setTextColor(Color.BLACK);

                TextView textViewAmount = new TextView(c);
                textViewAmount.setText("Amount             :   LKR " + amount + " /=");
                textViewAmount.setTextColor(Color.BLACK);

                linearLayout.addView(textViewTitle);
                linearLayout.addView(textViewOffenceType);
                linearLayout.addView(textViewDescription);
                linearLayout.addView(textViewPointWeight);
                linearLayout.addView(textViewAmount);

                cardView_v.addView(linearLayout);
                layoutVehicle.addView(cardView_v);

            }

            for (int i = 0; i < jsonArrayP.length(); i++) {

                try {
                    jsonObject = jsonArrayP.getJSONObject(i);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


                try {
                    amount = jsonObject.getInt("amount");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    offenceType = jsonObject.getString("offenceType");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    description = jsonObject.getString("description");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    pointWeight = jsonObject.getInt("pointWeight");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    offenceNo = jsonObject.getInt("offenceNo");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


//                TextView textView = new TextView(c);
//                textView.setText(i+1+". "+"\n"+
//                        "Offence No     :  " + offenceNo + "\n"+
//                        "Offence Type  :  " + offenceType + "\n" +
//                        "Description     :  " + description + "\n" +
//                        "Point Weight   : " + pointWeight + "\n"+
//                        "Amount           :  LKR  " + amount  + " /=" + "\n" );
//                textView.setTextColor(Color.BLACK);
//                layoutPedestrian.addView(textView);
                CardView cardView = new CardView(c);
                CardView.LayoutParams layoutParams = new CardView.LayoutParams(
                        CardView.LayoutParams.MATCH_PARENT,
                        CardView.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(12, 12, 12, 12); // Set margin bottom to 12dp
                cardView.setLayoutParams(layoutParams);
                cardView.setCardBackgroundColor(Color.parseColor("#d0c6c6"));
                cardView.setRadius(16);
                cardView.setContentPadding(32, 32, 32, 32);
                cardView.setCardElevation(8);
                cardView.setMaxCardElevation(16);

                LinearLayout linearLayout = new LinearLayout(c);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                linearLayout.setOrientation(LinearLayout.VERTICAL);

//                TextView textViewTitle = new TextView(c);
//                textViewTitle.setText(i+1+". "+"\n"+
//                        "Offence No     :  " + offenceNo);
//                textViewTitle.setTextColor(Color.BLACK);
//                textViewTitle.setTextSize(18); // set title font size
//                linearLayout.addView(textViewTitle);

                TextView textViewTitle = new TextView(c);
                textViewTitle.setText("Offence No     :  " + offenceNo);
                textViewTitle.setTextColor(Color.BLACK);
                textViewTitle.setTextSize(18); // set title font size
                linearLayout.addView(textViewTitle);

                TextView offenceTypeTextView = new TextView(c);
                offenceTypeTextView.setText("Offence Type  :  " + offenceType);
                offenceTypeTextView.setTextColor(Color.BLACK);
                linearLayout.addView(offenceTypeTextView);

                TextView descriptionTextView = new TextView(c);
                descriptionTextView.setText("Description     :  " + description);
                descriptionTextView.setTextColor(Color.BLACK);
                linearLayout.addView(descriptionTextView);

                TextView pointWeightTextView = new TextView(c);
                pointWeightTextView.setText("Point Weight   : " + pointWeight);
                pointWeightTextView.setTextColor(Color.BLACK);
                linearLayout.addView(pointWeightTextView);

                TextView amountTextView = new TextView(c);
                amountTextView.setText("Amount           :  LKR  " + amount + " /=");
                amountTextView.setTextColor(Color.BLACK);
                linearLayout.addView(amountTextView);

                cardView.addView(linearLayout);
                layoutPedestrian.addView(cardView);
            }
            // Update UI here
        }
    }

}