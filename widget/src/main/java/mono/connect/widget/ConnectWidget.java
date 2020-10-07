package mono.connect.widget;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Locale;

public class ConnectWidget {
  private String url;
  private Context context;
  private EventListener listener;

  public ConnectWidget(Context context, String key) {
    this.context = context;
    this.url = String.format("https://connect.withmono.com/?key=%s", key);
  }

  public void show() {
    if (this.listener == null) {
      Log.e(Constants.TAG, "Event listener can't be null");
      return;
    }

    Intent intent = new Intent(context, ConnectWidgetActivity.class);
    intent.putExtra(Constants.KEY_URL, this.getUrl());
    context.startActivity(intent);
  }

  public void setListener(EventListener listener) {
    this.listener = listener;
    MonoWebInterface.getInstance().setEventListener(listener);
  }

  public String getUrl() {
    return url;
  }
}
