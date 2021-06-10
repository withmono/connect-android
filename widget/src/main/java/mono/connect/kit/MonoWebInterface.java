package mono.connect.kit;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.json.JSONException;

public class MonoWebInterface {
  private static MonoWebInterface mInstance;
  private ConnectSuccessCallback onSuccess = null;
  private ConnectCloseCallback onClose = null;
  private ConnectEventCallback onEvent = null;

  private Activity mActivity;

  public void setActivity(Activity activity) {
    this.mActivity = activity;
  }

  @JavascriptInterface
  public void postMessage(String message) throws JSONException {
    Event event = Event.fromString(message);
    ConnectEvent connectEvent = ConnectEvent.fromString(message);

    Log.d("Type: ", event.getType());

    if(!event.getType().equals("mono.connect.widget.closed") && !event.getType().equals("mono.connect.widget.account_linked") && !event.getType().equals("mono.modal.closed") && !event.getType().equals("mono.modal.linked")){
      if(onEvent != null){
        onEvent.run(connectEvent);
      }
    }

    switch (event.getType()) {
      case "mono.connect.widget.closed":
        if(onClose != null){
          onClose.run();
        }
        this.mActivity.finish();
        break;

      case "mono.connect.widget.account_linked":
        ConnectedAccount account = new ConnectedAccount(event.getData().getString("code"));
        if(onSuccess != null){
          onSuccess.run(account);
        }
        this.mActivity.finish();
        break;
    }
  }

  public void setOnSuccess(ConnectSuccessCallback onSuccess) {
    this.onSuccess = onSuccess;
  }

  public ConnectSuccessCallback getOnSuccess() {
    return this.onSuccess;
  }

  public void setOnClose(ConnectCloseCallback onClose) {
    this.onClose = onClose;
  }

  public ConnectCloseCallback getOnClose() {
    return this.onClose;
  }

  public void setOnEvent(ConnectEventCallback onEvent) {
    this.onEvent = onEvent;
  }

  public ConnectEventCallback getOnEvent() {
    return this.onEvent;
  }


  public void triggerEvent(ConnectEvent connectEvent){
    if(onEvent != null){
      onEvent.run(connectEvent);
    }
  }

  public static MonoWebInterface getInstance() {
    if (mInstance == null) {
      mInstance = new MonoWebInterface();
    }

    return mInstance;
  }
}
