package com.example.mapmarker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

public class RouteActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    private Button okButton;
    private Button addButton;
    private Button pic_Button;
    private EditText nameField;

    private Cursor route;

    private boolean delete = false;

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);


        startActivity( mediaScanIntent );
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

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
        pic_Button = findViewById( R.id.picButtorn );
        addButton = findViewById( R.id.addPointButton );


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
            ContentValues v = new ContentValues();
            v.put( "name", "" );

            route = getRoute( db.insert( "route", null, v ) );

            route.moveToFirst();

            delete = true;
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues v = new ContentValues();

                v.put( "name", nameField.getText().toString() );

                db.update( "route", v, "_id=?", new String[]{route.getString(0) } );

                delete = false;

                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( RouteActivity.this, MapsActivity.class );

                Bundle b = new Bundle();

                b.putInt( "id", route.getInt(0) );

                i.putExtras( b );

                startActivity(i);
            }
        });

        pic_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //galleryAddPic();
                dispatchTakePictureIntent();
            }
        });
    }
}