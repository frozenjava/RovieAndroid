package space.codeit.rovie.io.net;

/**
 * Josh Artuso
 * 11/1/2015
 *
 * Get settings from the robot
 *
 */

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class GetSettingInfo extends AsyncTask <String, Void, String> {

    private String setting;
    private TextView textView;

    public GetSettingInfo(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected String doInBackground(String... params) {
        String host = params[0];
        this.setting = params[1];
        String responseMessage = null;

        try {
            Log.d("LOGCAT", host);
            Log.d("LOGCAT", setting);
            int responseCode;
            URL url = new URL("http://" + host + ":8080/info/" + setting);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "RovieApp");
            responseCode = con.getResponseCode();
            Log.d("LOGCAT", String.valueOf(responseCode));

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer r = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                r.append(inputLine);
            }
            in.close();
            responseMessage = r.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseMessage;
    }

    @Override
    protected void onPostExecute(String response) {
        try {
            JSONObject responseObject = new JSONObject(response);
            Boolean success = responseObject.getBoolean("success");
            Log.d("LOGCAT", "Response Status: " + success);

            if (!success) {
                return;
            }

            JSONObject dataObject = responseObject.getJSONObject("data");

            switch (this.setting) {
                case "temp":
                    String fahrenheit = dataObject.getString("fahrenheit");
                    String celsius = dataObject.getString("celsius");
                    textView.setText(fahrenheit + "'F / " + celsius + "'C");
                    break;

                case "uptime":
                    String uptime = dataObject.getString("uptime");
                    String hours = uptime.split(":")[0];
                    String minutes = uptime.split(":")[1];
                    String seconds = uptime.split(":")[2].split("\\.")[0];
                    textView.setText(hours + " Hours " + minutes + " Minutes " + seconds + " Seconds");
                    break;

                case "name":
                    String name = dataObject.getString("name");
                    textView.setText(name);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
