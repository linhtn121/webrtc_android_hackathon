package hackathon.rad.ftel.voiceoverip.client;


import android.content.Context;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;


/**
 * Created by linh on 14/01/2016.
 */
public class WebAppInterface {

    Context mContext;
    private WebView webView;

    public MediaPlayer mediaPlayer = null;
    public WebRTCClient.UAListenner mLoginListenner;
    public WebRTCClient.ReadyFunction mReadyFunction;

    public WebRTCClient.EndCall mEndCallListening;
    public WebRTCClient.AcceptedListenner mAcceptedListenner;


    WebRTCClient.SessionListenner sessionListenner;


    /** Instantiate the interface and set the context */
    WebAppInterface(Context c, WebView webView, WebRTCClient.UAListenner uaListenner, WebRTCClient.SessionListenner sessionListenner) {
        mContext = c;
        this.webView = webView;
        this.mLoginListenner = uaListenner;
        this.sessionListenner = sessionListenner;
    }


    @JavascriptInterface
    public void onRegistered(String department) {

        if(mLoginListenner != null)
        mLoginListenner.status("Registered");
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                if (mLoginListenner != null)
                    mLoginListenner.loginSuccess();

            }
        });
    }
    @JavascriptInterface
    public void onUnregistered() {

        if(mLoginListenner != null)
        mLoginListenner.status("Unregistered");

    }

    @JavascriptInterface
    public void onRegistrationFailed() {

        mLoginListenner.status("RegistrationFailed");
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(mLoginListenner != null)
                    mLoginListenner.loginFalse();
            }
        });
    }


    @JavascriptInterface
    public void onConnected() {

        if(mLoginListenner != null)
            mLoginListenner.status("Connected");

    }

    @JavascriptInterface
    public void onDisconnected() {

        if(mLoginListenner != null)
        mLoginListenner.status("Disconnected");
    }


    public void onCancel() {
    }


    @JavascriptInterface
    public void onBye() {
        Log.i("WebAppInterface", "onBye");

    }

    @JavascriptInterface
    public void onTerminated(String cause) {


        if(sessionListenner != null)
            sessionListenner.onTerminated(cause);
    }

    @JavascriptInterface
    public void onProgress(String code) {
        try {
            if (Integer.parseInt(code) >= 180 && Integer.parseInt(code) < 199) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {



                    }
                });
            }
        }catch (Exception e){}
    }

    @JavascriptInterface
    public void onConnecting() {

        mLoginListenner.status("Connecting");
    }

    @JavascriptInterface
    public void failedSession(String cause) {
        if(cause.equals("Not Found"))
        {

        }
    }




    @JavascriptInterface
    public void onAccepted() {
        if(mAcceptedListenner != null)
        {

           new Handler().post(new Runnable() {
                @Override
                public void run() {

                    mAcceptedListenner.onAccept();
             }
            });
        }

    }


    @JavascriptInterface
    public void onInvite(final String number) {

            if(sessionListenner != null)

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        sessionListenner.invite(number);
                    }
                });


        }



    //a remote invitation is calling

    @JavascriptInterface
    public void onMessage() {

    }


    @JavascriptInterface
    public void readyFunction() {
        if(mReadyFunction != null)
            mReadyFunction.ok();
    }

    // login

    @JavascriptInterface
    public void loginSuccess() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(mLoginListenner != null)
                    mLoginListenner.loginSuccess();}
        });

    }

    @JavascriptInterface
    public void loginFalse() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (mLoginListenner != null)
                    mLoginListenner.loginFalse();
            }
        });
    }

    @JavascriptInterface
    public void showToast(String toast) {
        //Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void pauseAudio() {
        Log.i("Audio", "stop audio");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = null;

    }

    @JavascriptInterface
    public void playAudio(boolean isspeaker) {


    }


    @JavascriptInterface
    public String  localStorageFromAndroid() {
        return "localStorageFromAndroid 6446464646";
    }

    @JavascriptInterface
    public void  incomingCall() {

    }

}
