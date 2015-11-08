package space.codeit.rovie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import space.codeit.rovie.io.database.DatabaseHandler;
import space.codeit.rovie.io.net.HostDiscovery;
import space.codeit.rovie.io.net.SendInitialize;
import space.codeit.rovie.models.Connection;

public class SetupActivity extends ActionBarActivity {

    private EditText hostText;
    private Button hostButton;
    private Button discoveryButton;
    private TextView statusTextView;
    private ListView historyView;
    private ArrayList<Connection> historyItems;
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

        final DatabaseHandler dbHandler = new DatabaseHandler(this);
        historyItems = dbHandler.getAllHistory();

        adapter = new ArrayAdapter<Connection>(this, android.R.layout.simple_list_item_2, android.R.id.text1, historyItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                Connection connection = historyItems.get(position);

                if (connection != null) {
                    text1.setText(connection.getHostAddress());
                    String rawDate = connection.getConnectionDate().split("\\s+")[0];
                    String rawTime = connection.getConnectionDate().split("\\s+")[1];
                    String formattedTime = formatClockText(Integer.parseInt(rawTime.split(":")[0]), Integer.parseInt(rawTime.split(":")[1]));
                    text2.setText("Last connected on " + rawDate + " at " + formattedTime);
                }

                return view;

            }
        };

        hostText = (EditText) findViewById(R.id.hostText);
        hostButton = (Button) findViewById(R.id.hostButton);
        discoveryButton = (Button) findViewById(R.id.discoveryButton);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        statusTextView.setTextColor(Color.RED);
        historyView = (ListView) findViewById(R.id.historyListView);


        historyView.setAdapter(adapter);

        historyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new SendInitialize(SetupActivity.this, statusTextView).execute(historyItems.get(position).getHostAddress());
            }
        });

        historyView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(SetupActivity.this)
                        .setTitle("Deleting Recent Connection")
                        .setMessage("Are you sure you want to remove this connection for your history?")
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SetupActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                dbHandler.deleteItem(historyItems.get(position));
                                historyItems.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();

                return true;
            }
        });

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

    private String formatClockText(int hour, int minute) {
        String timeOfDay = "AM";
        String formattedMinute = String.valueOf(minute);

        if (hour >= 12)
            timeOfDay = "PM";

        if (hour > 12)
            hour -= 12;

        if (hour == 0)
            hour = 12;

        if (minute >= 0 && minute <= 9)
            formattedMinute = "0" + minute;

        return hour + ":" + formattedMinute + " " + timeOfDay;
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
