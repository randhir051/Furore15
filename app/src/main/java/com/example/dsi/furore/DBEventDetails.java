package com.example.dsi.furore;

/**
 * Created by Randhir on 1/30/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DBEventDetails {

    public static final String KEY_ID = "id";
    public static final String KEY_EVENT_NAME = "event_name";
    public static final String KEY_EVENT_CATEGORY = "event_category";
    public static final String KEY_EVENT_TIMINGS = "event_timing";
    public static final String KEY_EVENT_DESCRIPTION = "event_description";


    private static final String DATABASE_NAME = "EventDetails";
    public static final String DATABASE_TABLE = "event_details";
    private static final int DATABASE_VERSION = 1;

    private DBHelper ourHelper;
    private final Context ourContext;
    public SQLiteDatabase ourDatabase;


    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DBEventDetails.DATABASE_NAME, null,
                    DBEventDetails.DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("CREATE TABLE " + DBEventDetails.DATABASE_TABLE + " ("
                    + DBEventDetails.KEY_ID + " TEXT NOT NULL, "
                    + DBEventDetails.KEY_EVENT_NAME + " TEXT NOT NULL, "
                    + DBEventDetails.KEY_EVENT_CATEGORY + " TEXT NOT NULL, "
                    + DBEventDetails.KEY_EVENT_TIMINGS + " TEXT NOT NULL, "
                    + DBEventDetails.KEY_EVENT_DESCRIPTION + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXIST " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public DBEventDetails(Context c) {
        ourContext = c;
    }

    public DBEventDetails open() throws SQLException {
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public long createEntry(String event_id, String event_name,
                            String event_category, String event_timings, String event_description) {
        // TODO Auto-generated method stub
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, event_id);
        cv.put(KEY_EVENT_NAME, event_name);
        cv.put(KEY_EVENT_CATEGORY, event_category);
        cv.put(KEY_EVENT_TIMINGS, event_timings);
        cv.put(KEY_EVENT_DESCRIPTION, event_description);
        return ourDatabase.insert(DATABASE_TABLE, null, cv);
    }

    public ArrayList<Event> getEvents() {

        ArrayList<Event> result = new ArrayList<>();
        String id, name, category, description, timing;
        Cursor mCursor = ourDatabase.rawQuery("SELECT * FROM "
                + DBEventDetails.DATABASE_TABLE, null);

        if (mCursor.moveToFirst()) {

            do {
                id = mCursor.getString(mCursor
                        .getColumnIndex(DBEventDetails.KEY_ID));
                name = mCursor.getString(mCursor
                        .getColumnIndex(DBEventDetails.KEY_EVENT_NAME));
                category = mCursor.getString(mCursor
                        .getColumnIndex(DBEventDetails.KEY_EVENT_CATEGORY));
                timing = mCursor.getString(mCursor
                        .getColumnIndex(DBEventDetails.KEY_EVENT_TIMINGS));
                description = mCursor.getString(mCursor
                        .getColumnIndex(DBEventDetails.KEY_EVENT_DESCRIPTION));

                result.add(0, new Event(id, name, timing, category, description));

            } while (mCursor.moveToNext());
        }

        mCursor.close();

        return result;
    }

}

