package com.example.mapmarker;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseManager extends SQLiteOpenHelper {

    public DatabaseManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL( "create table route( " +
                                "_id integer primary key autoincrement, " +
                                "name text not null ); " );

        sqLiteDatabase.execSQL( "create table point(" +
                                "_id integer primary key autoincrement," +
                                "ref_route integer not null, " +
                                "number integer, " +
                                "lon text, " +
                                "lat text, " +
                                "foreign key( ref_route ) references route (id) )" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL( "drop table route" );
        sqLiteDatabase.execSQL( "drop table point" );

        onCreate( sqLiteDatabase );
    }
}
