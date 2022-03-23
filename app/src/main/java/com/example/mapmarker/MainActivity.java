package com.example.mapmarker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mapmarker.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SQLiteDatabase db;

    private Spinner routeSpinner;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    private GoogleMap googleMap;

    private long routeId = -1;

    private void refreshSpinner(){
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

    private void refreshMap()
    {
        googleMap.clear();

        if( routeId != -1 ) {
            Cursor points = db.query("point", new String[]{"_id", "ref_route", "lon", "lat"}, "ref_route=?", new String[]{ String.valueOf( routeId ) }, null, null, "_id");

            while (points.moveToNext()) {
                double lon = Double.parseDouble(points.getString(2));
                double lat = Double.parseDouble(points.getString(3));

                googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(lat, lon), 10 ));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.routeMap);
        mapFragment.getMapAsync(this);

        db = new DatabaseManager( this, "MapMarker", null, 1 ).getReadableDatabase();

        routeSpinner = findViewById( R.id.routeSpinner );
        addButton = findViewById( R.id.addButton );
        editButton = findViewById( R.id.editButton );
        deleteButton = findViewById( R.id.deleteButton );

        refreshSpinner();

        routeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                routeId = l;

                refreshMap();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

                i.putExtras( b );

                startActivity( i );
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.delete( "route", "_id=?", new String[]{ String.valueOf( routeSpinner.getSelectedItemId() ) } );
                db.delete( "point", "ref_route=?", new String[]{ String.valueOf( routeSpinner.getSelectedItemId() ) } );

                refreshSpinner();
                refreshMap();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        refreshMap();
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshSpinner();
    }
}