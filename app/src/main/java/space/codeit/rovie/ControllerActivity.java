package space.codeit.rovie;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import space.codeit.rovie.io.net.GetSettingInfo;
import space.codeit.rovie.io.net.SendConfig;
import space.codeit.rovie.io.net.SendMovements;


public class ControllerActivity extends ActionBarActivity {

    private Button forward;
    private Button backward;
    private Button left;
    private Button leftBack;
    private Button right;
    private Button rightBack;
    private Button stop;
    private SeekBar speedBar;
    private TextView hostTextView;
    private String host = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        forward = (Button)findViewById(R.id.forwardButton);
        backward = (Button)findViewById(R.id.backwardButton);
        left = (Button)findViewById(R.id.leftForward);
        leftBack = (Button)findViewById(R.id.leftBack);
        right = (Button)findViewById(R.id.rightForward);
        rightBack = (Button)findViewById(R.id.rightBack);
        stop = (Button)findViewById(R.id.stopButton);
        speedBar = (SeekBar)findViewById(R.id.seekBar);
        hostTextView = (TextView)findViewById(R.id.hostInfoText);

        if (getIntent().getSerializableExtra("host") != null) {
            host = getIntent().getSerializableExtra("host").toString();
        }

        // Put the proper information into the info table
        updateInfoTable();

        forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        new SendMovements().execute(host, "forward");
                        break;
                    case MotionEvent.ACTION_UP:
                        new SendMovements().execute(host, "stop");
                        break;
                }
                return false;
            }
        });

        backward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        new SendMovements().execute(host, "backward");
                        break;
                    case MotionEvent.ACTION_UP:
                        new SendMovements().execute(host, "stop");
                        break;
                }
                return false;
            }
        });

        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        new SendMovements().execute(host, "left");
                        break;
                    case MotionEvent.ACTION_UP:
                        new SendMovements().execute(host, "stop");
                        break;
                }
                return false;
            }
        });

        leftBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        new SendMovements().execute(host, "left_reverse");
                        break;
                    case MotionEvent.ACTION_UP:
                        new SendMovements().execute(host, "stop");
                        break;
                }
                return false;
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        new SendMovements().execute(host, "right");
                        break;
                    case MotionEvent.ACTION_UP:
                        new SendMovements().execute(host, "stop");
                        break;
                }
                return false;
            }
        });

        rightBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        new SendMovements().execute(host, "right_reverse");
                        break;
                    case MotionEvent.ACTION_UP:
                        new SendMovements().execute(host, "stop");
                        break;
                }
                return false;
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendMovements().execute(host, "stop");
            }
        });

        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Double speed = 0.0;
                if (progress == 2)
                    speed = 0.0;
                else if (progress == 1)
                    speed = 0.50;
                else if (progress == 0)
                    speed = 1.0;

                new SendConfig().execute(host, "speed", "speed=" + speed.toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void updateInfoTable() {
        new GetSettingInfo((TextView)findViewById(R.id.tempText)).execute(host, "temp");
        new GetSettingInfo((TextView)findViewById(R.id.uptimeText)).execute(host, "uptime");
        new GetSettingInfo((TextView)findViewById(R.id.robotNameText)).execute(host, "name");
        hostTextView.setText(host);

        new Thread() {

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(4000);
                        new GetSettingInfo((TextView)findViewById(R.id.tempText)).execute(host, "temp");
                        new GetSettingInfo((TextView)findViewById(R.id.uptimeText)).execute(host, "uptime");
                        new GetSettingInfo((TextView)findViewById(R.id.robotNameText)).execute(host, "name");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }.start();
    }

}
