package mono.connect.widget;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;


public class ConnectWidget {
  private String key;
  private Context context;
  private EventListener listener;

  private Map<String, String> params = new HashMap<>();

  public ConnectWidget(Context context, String key) {
    this.context = context;
    this.key = key;
  }

  void startWidgetActivity() {
    if (this.listener == null) {
      Log.e(Constants.TAG, "Event listener can't be null");
      return;
    }

    Intent intent = new Intent(context, ConnectWidgetActivity.class);
    intent.putExtra(Constants.KEY_URL, this.getUrl());
    context.startActivity(intent);
  }

  public void show() {
    this.startWidgetActivity();
  }

  public void reauthorise(String token) {
    this.params.put(Constants.KEY_REAUTH_TOKEN, token);
    this.startWidgetActivity();
  }

  public void setListener(EventListener listener) {
    this.listener = listener;
    MonoWebInterface.getInstance().setEventListener(listener);
  }

  public String getUrl() {
    Uri.Builder builder = new Uri.Builder();
    builder.scheme(Constants.URL_SCHEME)
      .authority(Constants.CONNECT_URL)
      .appendQueryParameter("key", this.key);

    for (Map.Entry<String, String> entry : this.params.entrySet()) {
      builder.appendQueryParameter(entry.getKey(), entry.getValue());
    }

    return builder.build().toString();
  }
}
