package mono.connect.kit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.PermissionRequest;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import org.json.JSONException;
import org.json.JSONObject;

import mono.connect.R;

public class ConnectKitActivity extends AppCompatActivity {
  private ProgressBar mConnectLoader;
  private View mProgressContainer;

  private final String[] permissions = {
          Manifest.permission.CAMERA
  };

  private final int REQUEST_CODE = 0;
  WebView mWebView;
  String URL;

  private void askPermissions() {
    ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
  }

  private boolean isPermissionGranted() {
    for (String permission : permissions) {
      if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  private WebViewClient mWebViewClient = new WebViewClient() {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      return true;
    }

    @Override
    public void onPageFinished(WebView view, final String url) {
      mProgressContainer.setVisibility(View.GONE);
      mConnectLoader.setVisibility(View.GONE);

      // trigger LOADED event
      JSONObject data = new JSONObject();
      long unixTime = System.currentTimeMillis() / 1000L;
      try {
        data.put("timestamp", unixTime);
        ConnectEvent connectEvent = new ConnectEvent("OPENED", data);
        MonoWebInterface.getInstance().triggerEvent(connectEvent);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  };

  @Override
  protected void onCreate(@Nullable Bundle bundle) {
    supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

    super.onCreate(bundle);
    setContentView(R.layout.main);

    this.setup();
  }

  @SuppressLint("SetJavaScriptEnabled")
  private void setup() {
    mWebView = this.findViewById(R.id.connect_web_view);
    mConnectLoader = this.findViewById(R.id.connect_loader);
    mProgressContainer = this.findViewById(R.id.progress_container);

    URL = getIntent().getStringExtra(Constants.KEY_URL);
    mWebView.getSettings().setJavaScriptEnabled(true);
    mWebView.getSettings().setLoadWithOverviewMode(true);
    mWebView.getSettings().setUseWideViewPort(true);
    mWebView.getSettings().setDomStorageEnabled(true);
    mWebView.getSettings().setAllowContentAccess(true);
    mWebView.getSettings().setAllowFileAccess(true);
    mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
    mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
    mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    mWebView.getSettings().setBuiltInZoomControls(true);
    mWebView.getSettings().setMediaPlaybackRequiresUserGesture(true);
    mWebView.getSettings().setSafeBrowsingEnabled(true);
    mWebView.getSettings().setSupportZoom(true);
    mWebView.setWebViewClient(mWebViewClient);

    mWebView.setWebChromeClient(new WebChromeClient() {
      @Override
      public void onPermissionRequest(PermissionRequest request) {
        request.grant(request.getResources());
      }
    });

    if (!isPermissionGranted()) {
      askPermissions();
    }

    MonoWebInterface instance = MonoWebInterface.getInstance();
    instance.setActivity(this);

    mWebView.addJavascriptInterface(instance, "MonoClientInterface");
    mWebView.loadUrl(URL);

    // trigger OPENED event
    JSONObject data = new JSONObject();
    long unixTime = System.currentTimeMillis() / 1000L;
    try {
      data.put("timestamp", unixTime);
      String reference = instance.getReference();
      if(reference != null){
        data.put("reference", reference);
        data.put("type", "mono.connect.widget_opened");
      }
      ConnectEvent connectEvent = new ConnectEvent("OPENED", data);
      instance.triggerEvent(connectEvent);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_CODE) {
      int sum = 0;
      for (int result: grantResults) {
        // result will be  0 if PackageManager.PERMISSION_GRANTED
        // result will be -1 if PackageManager.PERMISSION_DENIED
        sum += result;
      }

      if (grantResults.length > 0 && sum == 0) {
        mWebView.loadUrl(URL);
      } else {
        Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
        finish();
      }
    }
  }
}