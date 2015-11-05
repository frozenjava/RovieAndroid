package space.codeit.rovie.io.net;

/**
 * Josh Artuso
 * 11/2/2015
 *
 * Do the stuff
 *
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import space.codeit.rovie.ControllerActivity;


public class SendInitialize extends AsyncTask<String, Void, Integer> {

    private String hostAddress = null;
    private ProgressDialog checkingDialog;
    private Activity activity;
    private TextView statusTextView;

    public SendInitialize(Activity activity, TextView textView) {
        this.activity = activity;
        this.statusTextView = textView;
    }

    @Override
    protected void onPreExecute() {
        checkingDialog = ProgressDialog.show(activity,
                "Attempting connection to robot...",
                "This may take a moment.",
                true);
    }

    @Override
    protected Integer doInBackground(String... host) {
        Integer responseCode = -1;
        hostAddress = host[0];

        //checkingDialog = ProgressDialog.show(SetupActivity.this, "Please wait...", "Attempting connection...", true);
        //checkingDialog.setCancelable(true);

        try {
            Log.d("LOGCAT", host[0]);
            URL url = new URL("http://" + host[0] + ":8080/connect");
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

        Log.d("LOGCAT", "RETURNING");
        return responseCode;
    }

    @Override
    protected void onPostExecute(Integer code) {
        if (checkingDialog != null)
            checkingDialog.dismiss();

        if (code != 200 && code != -1) {
            statusTextView.setVisibility(View.VISIBLE);
            statusTextView.setText("Error connecting to host. Status " + code);
        } else if (code == -1) {
            statusTextView.setVisibility(View.VISIBLE);
            statusTextView.setText("Error connecting to host. Make sure the robot is on.");
        } else {
            statusTextView.setVisibility(View.INVISIBLE);

            Toast.makeText(activity,
                    "Successfully connected to " + hostAddress,
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(activity, ControllerActivity.class);
            intent.putExtra("host", hostAddress);
            activity.startActivity(intent);
        }

    }
}
