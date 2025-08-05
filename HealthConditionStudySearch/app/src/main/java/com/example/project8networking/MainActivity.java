package com.example.project8networking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //Class Variables
    EditText conditionET;
    TextView outputTV;
    TextView countTV;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing view components neeeded for the program
        conditionET = findViewById(R.id.conditionET);
        outputTV = findViewById(R.id.outputTV);
        countTV = findViewById(R.id.countResultTV);

        //Creating a button with a listener that retrieves case trial information on click
        Button searchBT = findViewById(R.id.searchBT);
        searchBT.setOnClickListener(v -> {
            getInfo();
        });

        //Handler to ensure only one thread is accessing the Main activity
        handler = new Handler(Looper.getMainLooper());
    }

    //Method to retrieve info from the clinicaltrials.gov webpage based on condition
    void getInfo(){

        //Saving the condition inputted by the user
        String strCond = "";
        if(conditionET.getText() != null)
            strCond = conditionET.getText().toString();

        //Creating a background thread to retrieve handle the api requests
        String condition = strCond;
        Thread thread = new Thread(() -> {
            String condURLQuery = "https://clinicaltrials.gov/api/v2/studies?query.cond=" + condition + "&countTotal=true";

            try{

                //Converting the String to a URL and connecting it to our program
                URL url1 = new URL(condURLQuery);
                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                InputStream inputStream1 = connection.getInputStream();

                //Creating a String buffer and a Reader to read from the webpage
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream1));
                String line;

                //Adding the JSON contents of the webpage to the string buffer
                while((line = reader.readLine()) != null){
                    buffer.append(line).append("\n");
                }

                //Setting the countTV and outputTV with the output from the webpage
                int count;
                String results = buffer.toString();
                count = parseJSONCount(results);
                parseJSON(results, count);

            } catch(Exception ex){
                 ex.printStackTrace();
            }
        });
        thread.start();
    }

    //Method to parse the JSON file for the nctId in the studies
    void parseJSON(String results, int count) throws JSONException {

        JSONObject root = new JSONObject(results);//Grabs the entire JSON object
        StringBuilder resultantBuilder = new StringBuilder();

        //If the amount of trials has more than 9 limit the output to 10
        if(count > 9)
            for(int i = 0; i < 10; i++){

                JSONObject study = root.getJSONArray("studies").getJSONObject(i);//Grabs the ith element in the JSON array
                resultantBuilder.append(study.getJSONObject("protocolSection").getJSONObject("identificationModule").getString("nctId")).append(" - ")
                        .append(study.getJSONObject("protocolSection").getJSONObject("identificationModule").getString("briefTitle")).append("\n");
            }

        else//Limit the output to the amount of trials found
            for(int i = 0; i < count; i++){
                JSONObject study = root.getJSONArray("studies").getJSONObject(i);
                resultantBuilder.append(study.getJSONObject("protocolSection").getJSONObject("identificationModule").getString("nctId")).append(" - ")
                        .append(study.getJSONObject("protocolSection").getJSONObject("identificationModule").getString("briefTitle")).append("\n");
            }


        handler.post(() -> {//Synchronize access to the outputTV
            outputTV.setText(resultantBuilder.toString());//Set text to the clinical trials found
        });
    }

    //Parse JSON file for the count of clinical trials
    int parseJSONCount(String results) throws JSONException {
        JSONObject root = new JSONObject(results);

        StringBuilder resultantBuilder = new StringBuilder();
        int count;

        count  = root.getInt("totalCount");
        String result = count + "";

        //Synchronizes access to the countTV and sets the text to the count of clinical trials
        handler.post(() -> {
            countTV.setText(result);
        });

        return count;
    }
}