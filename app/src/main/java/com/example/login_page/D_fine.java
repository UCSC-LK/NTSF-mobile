package com.example.login_page;

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class D_fine extends Activity implements OnClickListener {

    EditText Licence,VehicleNo,Location,FineNo,Description;

    Button submit;

    String PoliceId;

    String fine_number;

    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Inside driver fine");

        PoliceIdSession storenew = new PoliceIdSession();

        PoliceId = storenew.getPoliceId(D_fine.this);

        System.out.println("Police id: "+PoliceId);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_fine);

        findViewsById();
        submit.setOnClickListener(this);

        Button home = findViewById(R.id.Homebtn);
        home.setOnClickListener(view -> {
            Intent intent=new Intent(D_fine.this, Home.class);
            startActivity(intent);
        });

        Button viewButton = findViewById(R.id.viewbtn);
        viewButton.setOnClickListener(view -> {
            Intent intent=new Intent(D_fine.this, D_list.class);
            startActivity(intent);
        });

        Button ImgButton = findViewById(R.id.footagebtn);
        ImgButton.setOnClickListener(view -> {
            Intent intent = new Intent(D_fine.this, Footage_D.class);
            startActivity(intent);
        });



    }

    private void findViewsById() {
        //Fine no will be autoincremented
        //Fine Type from session is not required as in web

        Licence = findViewById(R.id.licence);
        VehicleNo = findViewById(R.id.vehicleNo); //++++should be changed as driven_vehicle_no
        Location = findViewById(R.id.location); //-----Remove this
        FineNo = findViewById(R.id.FineNo); //+++Should be changed as offence_no
        Description = findViewById(R.id.description); // spot_Description
        ///Date and time set by server
        //police_id n police_station should be retrieved from session as in web. but police_station tken from server
        //Fine amount retrieved from offence_no
        //PAyment Status will be set as "unpaid" by defult in database
        //LAtitude and longitude SHOULD BE Sent by Android to javascript



        submit = findViewById(R.id.submitbtn);

        c = this;

    }

    public void onClick(View view){
        System.out.println("Inside click");
        String Licence_String = Licence.getText().toString();
        String VehicleNo_String = VehicleNo.getText().toString();
        String Location_String = Location.getText().toString();
        String FineNo_String = FineNo.getText().toString();
        String Description_String = Description.getText().toString();

        if(Licence_String.matches("") || VehicleNo_String.matches("")
        || Location_String.matches("") || FineNo_String.matches("") || Description_String.matches("")){
            System.out.println("Empty");
            TextView incompleteFieldsMessage = findViewById(R.id.incomplete_fields_message);
//            Toast.makeText(c, incompleteFieldsMessage.getText().toString(), Toast.LENGTH_LONG).show();
            incompleteFieldsMessage.setVisibility(View.VISIBLE);

            TextView credentialsFieldsMessage = findViewById(R.id.credentials_error);
//            Toast.makeText(c, incompleteFieldsMessage.getText().toString(), Toast.LENGTH_LONG).show();
            credentialsFieldsMessage.setVisibility(View.INVISIBLE);


//            Toast.makeText(c, "Fields are Incomplete", Toast.LENGTH_LONG).show();
        } else {
            new DFineAsynTask().execute();

        }
    }

    private void showNextpage() {
        Intent intent;
        intent = new Intent(D_fine.this, Message.class);
        intent.putExtra("fine_number", fine_number);
        startActivity(intent);
    }

    class DFineAsynTask extends AsyncTask<Void, Void, String> {

        Boolean status;

        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String bookJsonString = null;
            System.out.println("Do in Background");

            final String BOOK_BASE_URL = "http://10.0.2.2:8080/ntsf_backend_war/m_fine";

            String Licence_String = Licence.getText().toString();
            String VehicleNo_String = VehicleNo.getText().toString();
            String Location_String = Location.getText().toString();
            String FineNo_String = FineNo.getText().toString();
            String Description_String = Description.getText().toString();
            Double Latitude = 6.902042;
            Double Longitude = 79.86133;

            Uri uri = Uri.parse(BOOK_BASE_URL)
                    .buildUpon()
                    .appendQueryParameter("license_number",Licence_String)
                    .appendQueryParameter("location",Location_String)
                    .appendQueryParameter("fine_no",FineNo_String)
                    .appendQueryParameter("spot_description",Description_String)
                    .appendQueryParameter("police_id",PoliceId)
                    .appendQueryParameter("offence_type","DRIVER")
                    .appendQueryParameter("driven_vehicle_no",VehicleNo_String)
                    .build();

            try {
                URL url = new URL(uri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");

                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader((new InputStreamReader(inputStream)));

                StringBuilder builder = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line).append("\n");
                }
                JSONObject responseJson = new JSONObject(builder.toString());
                if (responseJson.getString("Status").equals("Success")) {
                    status = true;
                    fine_number = responseJson.getString("fine_number");
                } else {
                    status = false;
                }



            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (status) {
                System.out.println("Success");
                showNextpage();
            } else {
                System.out.println("Not Success");
//                Toast.makeText(c, "Licence Number and Name don't match", Toast.LENGTH_LONG).show();

                TextView incompleteFieldsMessage = findViewById(R.id.incomplete_fields_message);
//            Toast.makeText(c, incompleteFieldsMessage.getText().toString(), Toast.LENGTH_LONG).show();
                incompleteFieldsMessage.setVisibility(View.INVISIBLE);

                TextView credentialsFieldsMessage = findViewById(R.id.credentials_error);
//            Toast.makeText(c, incompleteFieldsMessage.getText().toString(), Toast.LENGTH_LONG).show();
                credentialsFieldsMessage.setVisibility(View.VISIBLE);
            }


        }


    }

}