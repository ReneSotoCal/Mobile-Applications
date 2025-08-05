package com.example.project7logininfostore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//Main Class
public class MainActivity extends AppCompatActivity {

    //Class Variables
    EditText site;
    EditText user;
    EditText pass;
    TextView output;
    DBHelper helper = new DBHelper(this);//Instantiating the DB helper class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiating the view components
        output = findViewById(R.id.outputTv);
        site = findViewById(R.id.editSite);
        user = findViewById(R.id.editUser);
        pass = findViewById(R.id.editPass);
        Button retrieve = findViewById(R.id.retrieveBt);
        Button insert = findViewById(R.id.insertBt);

        //Event Listener for the insert button
        insert.setOnClickListener(v -> {
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues row = new ContentValues();

            //Fill the row's content values
            row.put("Site", site.getText().toString());
            row.put("User", user.getText().toString());
            row.put("Password", pass.getText().toString());

            //Variables for querying the LoginInfo Table
            String[] projection = {"Site"};
            String selection = "Site" + "=  ?";
            String[] selectionArgs = {site.getText().toString()};

            //Save the cursor object returned by the database query
            Cursor cursor = db.query("LoginInfo", projection, selection, selectionArgs, null, null, null);

            //Check whether the record already exits
            if(cursor.moveToFirst()){
                db.update("LoginInfo", row, selection, selectionArgs);//Update if it does
                Toast.makeText(this,"Entry Updated", Toast.LENGTH_SHORT).show();
            } else{
                db.insert("LoginInfo", null, row);//Insert a new row if it doesn't
                Toast.makeText(this,"New Entry Inserted", Toast.LENGTH_SHORT).show();
            }

            //Close the cursor and database objects
            cursor.close();
            db.close();
        });

        //Event Listener for the retrieve button
        retrieve.setOnClickListener(v -> {
            SQLiteDatabase db = helper.getReadableDatabase();
            String username = "";
            String password = "";

            // Variables to query the database
            String[] projection = {"Site", "User", "Password"};
            String selection = "Site" + "=  ?";
            String[] selectionArgs = {site.getText().toString()};

            //Save the cursor object returned by the database query
            Cursor cursor = db.query("LoginInfo", projection, selection, selectionArgs, null, null, null);

            //Check whether a record exists. If so retrieve user and password, else alert user.
            if(cursor.moveToFirst()){
                username = cursor.getString(cursor.getColumnIndexOrThrow("User"));
                password = cursor.getString(cursor.getColumnIndexOrThrow("Password"));
            } else{
                Toast.makeText(this,"No Entry Found", Toast.LENGTH_SHORT).show();
            }

            // Close the cursor and database objects
            cursor.close();
            db.close();

            //Format the output and display it in the textview
            String result = String.format("Username:    %s%nPassword:  %s%nFor: %s", username, password, site.getText().toString());
            output.setText(result);
        });
    }
}