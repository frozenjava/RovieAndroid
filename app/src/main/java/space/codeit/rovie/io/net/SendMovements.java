package space.codeit.rovie.io.net;

/**
 * Josh Artuso
 * 10/31/2015
 *
 * This will send API requests to the robot
 *
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SendMovements extends AsyncTask<String, Void, Boolean> {


    @Override
    protected Boolean doInBackground(String... params) {

        String host = params[0];
        String direction = params[1];

        try {
            Log.d("LOGCAT", host);
            Log.d("LOGCAT", direction);
            int responseCode;
            URL url = new URL("http://" + host + ":8080/move/" + direction);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "RovieApp");
            responseCode = con.getResponseCode();
            Log.d("LOGCAT", String.valueOf(responseCode));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;

    }
}
