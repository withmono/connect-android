package mono.connect.kit;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;

import java.util.HashMap;
import java.util.Map;


public class ConnectKit {
  private String key;
  private Context context;

  private Map<String, String> params = new HashMap<>();

  public ConnectKit(Context context, String key) {
    this.context = context;
    this.key = key;
  }

  public ConnectKit(MonoConfiguration config){

    this.context = config.context;
    this.key = config.publicKey;

    if(config.reference != null){
      this.params.put(Constants.KEY_REFERENCE, config.reference);
    }

    if(config.reauthCode != null){
      this.params.put(Constants.KEY_REAUTH_TOKEN, config.reauthCode);
    }

    if(config.onSuccess != null){
      MonoWebInterface.getInstance().setOnSuccess(config.onSuccess);
    }
    if(config.onClose != null){
      MonoWebInterface.getInstance().setOnClose(config.onClose);
    }
    if(config.onEvent != null){
      MonoWebInterface.getInstance().setOnEvent(config.onEvent);
    }

  }

  void startWidgetActivity() {
    if (MonoWebInterface.getInstance().getOnSuccess() == null) {
      Log.e(Constants.TAG, "onSuccess can't be null");
      return;
    }

    Intent intent = new Intent(context, ConnectKitActivity.class);
    intent.putExtra(Constants.KEY_URL, this.getUrl());
    context.startActivity(intent);
  }

  public void show() {
    this.startWidgetActivity();
  }

  public String getUrl() {
    Uri.Builder builder = new Uri.Builder();
    builder.scheme(Constants.URL_SCHEME)
      .authority(Constants.CONNECT_URL)
      .appendQueryParameter(Constants.KEY_VERSION, Constants.VERSION)
      .appendQueryParameter("key", this.key);

    for (Map.Entry<String, String> entry : this.params.entrySet()) {
      builder.appendQueryParameter(entry.getKey(), entry.getValue());
    }

    return builder.build().toString();
  }
}
