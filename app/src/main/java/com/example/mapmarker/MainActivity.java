package com.example.mapmarker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    private Spinner routeSpinner;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    private void refresh(){
        Cursor c = db.query( "route ", new String[] { "_id", "name" }, null, null, null, null, "name" );

        if( c.moveToFirst() ) {
            routeSpinner.setAdapter(new CursorAdapter(this, c, true) {
                @Override
                public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                    return new TextView(context);
                }

                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    ((TextView) view).setText(cursor.getString(1));
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseManager( this, "MapMarker", null, 1 ).getReadableDatabase();

        routeSpinner = findViewById( R.id.routeSpinner );
        addButton = findViewById( R.id.addButton );
        editButton = findViewById( R.id.editButton );
        deleteButton = findViewById( R.id.deleteButton );

        refresh();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RouteActivity.class);

                startActivity( i );
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RouteActivity.class);

                Bundle b = new Bundle();

                b.putLong( "id", routeSpinner.getSelectedItemId() );

                startActivity( i, b );
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeSpinner.getSelectedItemId();
            }
        });
    }
}