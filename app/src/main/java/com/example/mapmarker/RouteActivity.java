package com.example.mapmarker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RouteActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    private Button okButton;
    private Button addButton;
    private Button deleteButton;
    private EditText nameField;

    private Cursor route;

    private boolean delete = false;

    private Cursor getRoute( long id )
    {
        return db.query( "route", new String[]{ "_id", "name" }, "_id=?", new String[]{ String.valueOf( id ) }, null, null, null );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        Intent i = getIntent();

        db = new DatabaseManager( this, "MapMarker", null, 1 ).getReadableDatabase();

        okButton = findViewById( R.id.okButton );
        nameField = findViewById( R.id.nameField );
        addButton = findViewById( R.id.addPointButton );
        deleteButton = findViewById( R.id.deletePointButton );

        if( i.getExtras() != null && i.getExtras().containsKey( "id" ) )
        {
            route = getRoute( i.getExtras().getLong( "id" ) );

            if( route.moveToFirst() ) {
                nameField.setText(route.getString(1));
            }
            else
            {
                route = null;
            }
        }

        if( route == null )
        {
            route = getRoute( db.insert( "route", null, new ContentValues() ) );

            delete = true;
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues v = new ContentValues();

                v.put( "name", nameField.getText().toString() );

                db.insert( "route", null, v );

                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( RouteActivity.this, MapsActivity.class );

                startActivity(i);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        if( delete )
        {
//            db.delete( "route" )
        }
    }
}