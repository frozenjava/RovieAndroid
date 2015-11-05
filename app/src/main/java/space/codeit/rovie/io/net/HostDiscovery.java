package space.codeit.rovie.io.net;

/**
 * Josh Artuso
 * 11/2/2015
 * <p/>
 * Discover the host on the network with SSDP
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import space.codeit.rovie.discovery.SSDPConstants;


public class HostDiscovery extends AsyncTask<String, Void, String> {

    private Activity activity;
    private ProgressDialog checkingDialog;
    private SocketAddress multicastGroupAddress = new InetSocketAddress(SSDPConstants.ADDRESS, SSDPConstants.PORT);
    private MulticastSocket multicastSocket;
    private DatagramSocket unicastSocket;

    private NetworkInterface networkInterface;

    private Context context;

    private TextView textView;

    private boolean running = false;

    public HostDiscovery(Activity activity, Context context, TextView textView) throws SocketException, UnknownHostException {
        this.context = context;
        this.activity = activity;
        this.textView = textView;
        this.networkInterface = this.getNetworkInterface();
    }

    @Override
    protected void onPreExecute() {
        checkingDialog = ProgressDialog.show(this.activity,
                "Searching for robot...",
                "This may take a moment.",
                true);
    }

    @Override
    protected String doInBackground(String... params) {
        String location = null;
        try {
            multicastSocket = new MulticastSocket(1900);
            multicastSocket.setLoopbackMode(true);
            multicastSocket.joinGroup(multicastGroupAddress, networkInterface);

            unicastSocket = new DatagramSocket(null);
            unicastSocket.setReuseAddress(true);
            unicastSocket.bind(new InetSocketAddress("", 1900));
        } catch (IOException e) {
            Log.e("LOGCAT", "SSDP setup failed", e);
            e.printStackTrace();
        }

        running = true;
        stopChecking();
        while (running) {
            DatagramPacket dp = null;
            try {
                dp = receive();

                String startLine = parseStartLine(dp);
                if (startLine.equals(SSDPConstants.SL_MSEARCH)) {
                    String st = parseHeaderValue(dp, SSDPConstants.ST);
                    if (st.equals(SSDPConstants.ST_DEVICE)) {
                        location = parseHeaderValue(dp, SSDPConstants.LOCATION);
                        break;
                    }

                }
            } catch (IOException e) {
                Log.e("LOGCAT", "SSDP fail", e);
            }
        }

        return location;
    }

    @Override
    protected void onPostExecute(String response) {
        checkingDialog.dismiss();
        if (response == null) {
            // Put up an error message
            Log.d("LOGCAT", "Error finding robot");
            this.textView.setTextColor(Color.RED);
            this.textView.setVisibility(View.VISIBLE);
            this.textView.setText("Couldn't find robot on network.");
        } else {
            new SendInitialize(this.activity, textView).execute(response.split(":")[0]);
        }
    }

    private DatagramPacket receive() throws IOException {
        byte[] buf = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);
        multicastSocket.receive(dp);

        return dp;
    }

    private String parseHeaderValue(String content, String headerName) {
        Scanner s = new Scanner(content);
        s.nextLine(); // Skip the start line

        while (s.hasNextLine()) {
            String line = s.nextLine();
            int index = line.indexOf(':');
            String header = line.substring(0, index);
            if (headerName.equalsIgnoreCase(header.trim())) {
                return line.substring(index + 1).trim();
            }
        }

        return null;
    }

    private String parseHeaderValue(DatagramPacket dp, String headerName) {
        return parseHeaderValue(new String(dp.getData()), headerName);
    }

    private String parseStartLine(String content) {
        Scanner s = new Scanner(content);
        return s.nextLine();
    }

    private String parseStartLine(DatagramPacket dp) {
        return parseStartLine(new String(dp.getData()));
    }

    private NetworkInterface getNetworkInterface() throws UnknownHostException, SocketException {
        WifiManager wifiManager = (WifiManager) this.activity.getSystemService(this.context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        byte[] bytes = BigInteger.valueOf(ipAddress).toByteArray();
        InetAddress addr = InetAddress.getByAddress(bytes);
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(addr);
        return networkInterface;
    }

    private void stopChecking() {
        new Thread() {
            @Override
            public void run() {

                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                running = false;

            }
        }.start();
    }

}
