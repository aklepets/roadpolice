package com.hfad.roadpolice;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText firstNameInput;
    EditText lastNameInput;
    EditText paternalNameInput;
    EditText driverLicenseSeriesInput;
    EditText driverLicenseNumberInput;
    Button btn;
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstNameInput = (EditText) findViewById(R.id.first_name);
        lastNameInput = (EditText) findViewById(R.id.last_name);
        paternalNameInput = (EditText) findViewById(R.id.paternal_name);
        driverLicenseSeriesInput = (EditText) findViewById(R.id.driver_license_series);
        driverLicenseNumberInput = (EditText) findViewById(R.id.driver_license_number);
        btn = (Button) findViewById(R.id.button);
        resultTextView = (TextView) findViewById(R.id.text_view);

    }

    public void onClickButton(View view) {
        String [] s = {"hello"};
        HttpAsyncTask myTask = new HttpAsyncTask();
        myTask.execute();
    }

    public class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            BufferedReader reader = null;
            String responseStr = null;

            try {
                HttpURLConnection httpconnection = (HttpURLConnection) ((new URL("http://mvd.gov.by/Ajax.asmx/GetExt").openConnection()));
                httpconnection.setRequestProperty("Content-Type", "application/json");
                httpconnection.setRequestProperty("Referer", "http://mvd.gov.by/main.aspx?guid=15791");
                httpconnection.setRequestProperty("Connection", "keep-alive");
                httpconnection.setRequestMethod("POST");
                httpconnection.connect();

                String lastName = lastNameInput.getText().toString();
                String firstName = firstNameInput.getText().toString();
                String paternalName = paternalNameInput.getText().toString();
                String driverLicenseSeries = driverLicenseSeriesInput.getText().toString();
                String driverLicenseNumber = driverLicenseNumberInput.getText().toString();


                String guidControl = "'GuidControl'";
                String numb2091 = "2091";
                String param1 = "'Param1'";
                String param2 = "'Param2'";
                String param3 = "'Param3'";
                String openCurlyBrace = "{";
                String closeCurlyBrace = "}";
                String colon = ":";
                String comma = ",";
                String singleQuote = "'";
                String space = " ";



                byte[] outputBytes = (openCurlyBrace + guidControl + colon + numb2091 + comma +
                        param1 + colon + singleQuote + lastName + space +
                        firstName + space +
                        paternalName + singleQuote + comma +
                        param2 + colon + singleQuote + driverLicenseSeries + singleQuote + comma +
                        param3 + colon + singleQuote + driverLicenseNumber + singleQuote + closeCurlyBrace).getBytes();


                OutputStream os = httpconnection.getOutputStream();
                os.write(outputBytes);

                os.close();

                // Read the input stream into a String
                InputStream inputStream = httpconnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                // Nothing to do.
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                responseStr = buffer.toString();

                return responseStr;
            }
            catch (Exception e){
                Log.e("roadpolice", "Excpection occured" + e.getClass().toString());
                return e.getClass().toString();
            }
        }

        @Override
        protected void onPreExecute() {
            resultTextView.setText("preExecute");
        }

        @Override
        protected void onPostExecute(String result) {
            resultTextView.setText(result);
        }


    }
}
