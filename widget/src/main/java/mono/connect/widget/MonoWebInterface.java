package mono.connect.widget;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.json.JSONException;

public class MonoWebInterface {
  private static MonoWebInterface mInstance;
  private EventListener mEventListener;

  private Activity mActivity;

  public void setActivity(Activity activity) {
    this.mActivity = activity;
  }

  @JavascriptInterface
  public void postMessage(String message) throws JSONException {
    Event event = Event.fromString(message);

    switch (event.getType()) {
      case "mono.connect.widget.closed":
        mEventListener.onClose();
        this.mActivity.finish();
        break;

      case "mono.connect.widget.account_linked":
        ConnectedAccount account = new ConnectedAccount(event.getData().getString("code"));
        mEventListener.onSuccess(account);
        this.mActivity.finish();
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
