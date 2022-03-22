package com.example.mapmarker;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

public class RouteActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        db = new DatabaseManager( this, "MapMarker", null, 1 ).getReadableDatabase();

        if( savedInstanceState.containsKey( "id" ) )
        {

        }
    }
}