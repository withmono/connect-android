package mono.connect.kit;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MonoWebInterface {
  private static MonoWebInterface mInstance;
  private ConnectSuccessCallback onSuccess = null;
  private ConnectCloseCallback onClose = null;
  private ConnectEventCallback onEvent = null;
  private String reference = null;
  public static final List<String> DEPRECATED_EVENTS = Arrays.asList("mono.connect.widget.closed", "mono.connect.widget.account_linked", "mono.modal.closed", "mono.modal.linked");

  private Activity mActivity;

  public void reset (){
    mInstance = new MonoWebInterface();
  }

  public void setActivity(Activity activity) {
    this.mActivity = activity;
  }

  @JavascriptInterface
  public void postMessage(String message) throws JSONException {
    Event event = Event.fromString(message);
    ConnectEvent connectEvent = ConnectEvent.fromString(message);

    Log.d("Type: ", event.getType());

    if(!DEPRECATED_EVENTS.contains(event.getType())){
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

          // trigger LOADED event
          JSONObject data = new JSONObject();
          long unixTime = System.currentTimeMillis() / 1000L;
          try {
            data.put("timestamp", unixTime);
            data.put("code", account.getCode());
            ConnectEvent successEvent = new ConnectEvent("SUCCESS", data);
            MonoWebInterface.getInstance().triggerEvent(successEvent);
          } catch (JSONException e) {
            e.printStackTrace();
          }

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

  public void setReference(String ref){
    reference = ref;
  }

  public String getReference(){
    if(reference != null){
      return reference;
    }
    return null;
  }

  public void triggerEvent(ConnectEvent connectEvent) throws JSONException{
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
