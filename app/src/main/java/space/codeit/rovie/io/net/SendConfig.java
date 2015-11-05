package space.codeit.rovie.io.net;

/**
 * Josh Artuso
 * 10/31/2015
 * Send config options
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SendConfig extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... params) {
        String host = params[0];
        String setting = params[1];
        String value = params[2];

        try {
            Log.d("LOGCAT", host);
            Log.d("LOGCAT", setting);
            int responseCode;
            URL url = new URL("http://" + host + ":8080/config/" + setting + "?" + value);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "RovieApp");
            responseCode = con.getResponseCode();
            Log.d("LOGCAT", String.valueOf(responseCode));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
