package hackathon.rad.ftel.voiceoverip.client;

import android.app.Activity;
import android.content.Context;

import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by linh on 14/01/2016.
 */
public class WebRTCClient {

    public static WebAppInterface webAppInterface;
   


    static WebView webView;


    public static void init(WebView webView, Context mContext, UAListenner uaListenner, SessionListenner sessionListenner) {

        WebRTCClient.webView = webView;

        webAppInterface = new WebAppInterface(mContext, webView, uaListenner, sessionListenner);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.addJavascriptInterface(webAppInterface, "Android");

        webView.setWebContentsDebuggingEnabled(true);


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
       webView.loadUrl("file:///android_asset/www/index.html", null);

    }


    public static void setEndCallListening(EndCall ec) {
        webAppInterface.mEndCallListening = ec;
    }

    public static void setAcceptetListener(AcceptedListenner ac) {
        webAppInterface.mAcceptedListenner = ac;
    }

    public static void call(String number) {

        String cmd = "javascript:call('" + number + "');";

        webView.loadUrl(cmd, null);

    }


    public static void login(String user, String pass) {

        String u = "'" + user + "'";
        String p = "'" + pass + "'";

        String cmd = "javascript:login(" + u + "," + p + " );";

        webView.loadUrl(cmd, null);

    }

    public static void endCall() {


                String cmd = "javascript:onDestroy();";
                webView.loadUrl(cmd, null);
    }

    public static void logout() {
        String cmd = "javascript:logout();";
        webView.loadUrl(cmd, null);

    }
    public static void hidden() {
        String cmd = "javascript:hidden();";
        webView.loadUrl(cmd, null);
    }
    public static void accept() {

        String cmd = "javascript:accept();";
        webView.loadUrl(cmd, null);
    }



    public interface UAListenner {
        public void loginSuccess();

        public void loginFalse();
        public void status(String status);
    }

    public interface ReadyFunction {

        public void ok();
    }

    public interface EndCall {

        public void toEndCall(String cause);
    }

    public interface AcceptedListenner {

        public void onAccept();
    }

    public interface SessionListenner {

        public void onAccept();
        public void invite(String number);
        public void onTerminated(String ms);
    }


}
