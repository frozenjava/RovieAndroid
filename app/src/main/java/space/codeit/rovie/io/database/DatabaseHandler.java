package space.codeit.rovie.io.database;

/**
 * Josh Artuso
 * 11/4/2015
 *
 * Handle database things
 *
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import space.codeit.rovie.models.Connection;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ConnectionHistory";
    private static final String HISTORY_TABLE_NAME = "history";
    private static final String HISTORY_TABLE_CREATE =
            "CREATE TABLE " + HISTORY_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "host TEXT, connectDate TEXT);";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HISTORY_TABLE_CREATE);
        Log.d("LOGCAT", "Created table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void deleteItem(Connection connection) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + HISTORY_TABLE_NAME + " WHERE id=\"" + connection.getConnectionId() + "\"");
        db.close();
    }

    public void insertNew(Connection connection) {
        SQLiteDatabase db = this.getWritableDatabase();
        int uid = findExistingId(connection.getHostAddress(), db);
        if (uid == -1) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("host", connection.getHostAddress());
            contentValues.put("connectDate", connection.getConnectionDate());
            db.insert(HISTORY_TABLE_NAME, null, contentValues);
            Log.d("LOGCAT", "INSERTED TO NEW RECORD: " + contentValues.getAsString("host"));
        } else {
            connection.setConnectionId(uid);
            updateExisting(connection, db);
        }
        db.close();
    }

    private int findExistingId(String host, SQLiteDatabase db) {
        int id = -1;

        if (host != null) {
            Cursor cursor = db.rawQuery("SELECT * FROM " + HISTORY_TABLE_NAME + " WHERE host = \"" + host + "\"", null);
            cursor.moveToFirst();
            if (cursor.moveToNext()) {
                if (cursor.getString(1) != null) {
                    id = cursor.getInt(0);
                }
            }
            //}
            cursor.close();
        }
        return id;
    }

    private void updateExisting(Connection connection, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("host", connection.getHostAddress());
        contentValues.put("connectDate", connection.getConnectionDate());

        db.update(HISTORY_TABLE_NAME, contentValues, "id" + " = ?", new String[]{String.valueOf(connection.getConnectionId())});
        Log.d("LOGCAT", "UPDATED: " + connection.getConnectionId());

    }

    public ArrayList<Connection> getAllHistory() {
        ArrayList<Connection> arrayList = new ArrayList<Connection>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + HISTORY_TABLE_NAME, null);

            Log.d("LOGCAT", "TOTAL RECORDS: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    Connection connection = new Connection();
                    connection.setConnectionId(cursor.getInt(0));
                    connection.setHostAddress(cursor.getString(1));
                    connection.setConnectionDate(cursor.getString(2));
                    arrayList.add(connection);
                }
            }

            cursor.close();
        } catch (Exception e) {
            Log.e("LOGCAT", e.getMessage());
        }

        return arrayList;
    }

}
