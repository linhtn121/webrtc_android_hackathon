package hackathon.rad.ftel.voiceoverip;

import android.Manifest;
import android.content.pm.PackageManager;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import hackathon.rad.ftel.voiceoverip.client.WebRTCClient;
import hackathon.rad.ftel.voiceoverip.client.WebRTCClient.UAListenner;

public class MainActivity extends AppCompatActivity implements UAListenner, WebRTCClient.SessionListenner{


    WebView webView;
    TextView uaStatus;
    TextView session_status;
    Button callOut;

    View incoming, outcoming;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        uaStatus = (TextView) findViewById(R.id.ua_status);
        session_status = (TextView) findViewById(R.id.session_status);

        callOut = (Button) findViewById(R.id.button_call_out);
        callOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        incoming = findViewById(R.id.incomming);

        outcoming = findViewById(R.id.outcoming);

       findViewById(R.id.button_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebRTCClient.accept();
            }
        });

        findViewById(R.id.button_reject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebRTCClient.endCall();
            }
        });


        webView = (WebView) findViewById(R.id.webview);

        WebRTCClient.init(webView, getApplicationContext(), this, this);

        permission();

    }

    private  void permission(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        11111);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        2222);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.MODIFY_AUDIO_SETTINGS)) {


            } else {



                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS},
                        2222);


            }
        }
    }

    private void call() {


        EditText editText = (EditText) findViewById(R.id.editText);

        WebRTCClient.call(editText.getText().toString());
    }

    @Override
    public void loginSuccess() {

    }

    @Override
    public void loginFalse() {

    }

    @Override
    public void status(final String status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                uaStatus.setText("UA status: " + status);
            }
        });
    }


    @Override
    public void onAccept() {

    }

    @Override
    public void invite(String number) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                incoming.setVisibility(View.VISIBLE);
                outcoming.setVisibility(View.INVISIBLE);
            }
        });



    }

    @Override
    public void onTerminated(final String ms) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                incoming.setVisibility(View.INVISIBLE);
                outcoming.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), ms, Toast.LENGTH_LONG);
            }
        });



    }


}
