package mono.connect.widget;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import org.json.JSONException;

public class MonoWebInterface {
  private static MonoWebInterface mInstance;
  private EventListener mEventListener;

  private Activity mActivity;

  public void setActivity(Activity activity) {
    this.mActivity = activity;
  }

  @JavascriptInterface
  public void onMessage(String message) throws JSONException {
    Event event = Event.fromString(message);
    this.mActivity.finish();

    switch (event.getType()) {
      case "mono.widget.closed":
        mEventListener.onClose();
        break;

      case "mono.widget.account_connected":
        ConnectedAccount account = new ConnectedAccount(event.getData().getString("code"));
        mEventListener.onSuccess(account);
        break;
    }
  }

  public void setEventListener(EventListener listener) {
    this.mEventListener = listener;
  }

  public static MonoWebInterface getInstance() {
    if (mInstance == null) {
      mInstance = new MonoWebInterface();
    }

    return mInstance;
  }
}
