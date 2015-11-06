package space.codeit.rovie;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import space.codeit.rovie.io.database.DatabaseHandler;
import space.codeit.rovie.io.net.HostDiscovery;
import space.codeit.rovie.io.net.SendInitialize;

public class SetupActivity extends ActionBarActivity {

    private EditText hostText;
    private Button hostButton;
    private Button discoveryButton;
    private TextView statusTextView;
    private ListView historyView;
    private ArrayList<String> historyItems;
    private ArrayAdapter adapter;

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        //String[] historyListItems = new String[] {"Connection 1", "Connection 2", "Connection 3"};

        DatabaseHandler dbHandler = new DatabaseHandler(this);

        ArrayAdapter<String> historyArray = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dbHandler.getAllHistory());

        hostText = (EditText) findViewById(R.id.hostText);
        hostButton = (Button) findViewById(R.id.hostButton);
        discoveryButton = (Button) findViewById(R.id.discoveryButton);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        statusTextView.setTextColor(Color.RED);
        historyView = (ListView) findViewById(R.id.historyListView);

        //historyView.setAdapter(historyArray);

        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hostAddress = hostText.getText().toString().replace(" ", "");

                Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
                Matcher matcher = pattern.matcher(hostAddress);

                if (!matcher.matches()) {
                    statusTextView.setVisibility(View.VISIBLE);
                    statusTextView.setText("Enter a valid IPv4 Address.");
                    return;
                }

                //new HostCheck().execute(hostAddress);
                new SendInitialize(SetupActivity.this, statusTextView).execute(hostAddress);

            }
        });

        discoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    new HostDiscovery(SetupActivity.this, SetupActivity.this, statusTextView).execute();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
