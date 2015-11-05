package space.codeit.rovie.io.database;

/**
 * Josh Artuso
 * 11/4/2015
 *
 * Handle database things
 *
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ConnectionHistory";
    private static final String HISTORY_TABLE_NAME = "history";
    private static final String HISTORY_TABLE_CREATE =
            "CREATE TABLE" + HISTORY_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
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



}
