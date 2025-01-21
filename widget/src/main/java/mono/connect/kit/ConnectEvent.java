package mono.connect.kit;

import org.json.JSONException;
import org.json.JSONObject;

public class ConnectEvent {

    private final String eventName;
    private final JSONObject data;

    public ConnectEvent(String eventName, JSONObject data) {

        this.eventName = eventName;
        this.data = data;

    }

    public static ConnectEvent fromString(String data) throws JSONException {
        JSONObject event = new JSONObject(data);
        JSONObject body = null;
        String type = event.getString("type");

        String name = "UNKNOWN";

        if (type.equals("mono.connect.widget_opened")) {
            name = "OPENED";
        } else if (type.equals("mono.connect.widget.account_linked")) {
            name = "SUCCESS";
        } else if (type.equals("mono.connect.error_occured")) {
            name = "ERROR";
        } else if (type.equals("mono.connect.institution_selected")) {
            name = "INSTITUTION_SELECTED";
        } else if (type.equals("mono.connect.auth_method_switched")) {
            name = "AUTH_METHOD_SWITCHED";
        } else if (type.equals("mono.connect.on_exit")) {
            name = "EXIT";
        } else if (type.equals("mono.connect.login_attempt")) {
            name = "SUBMIT_CREDENTIALS";
        } else if (type.equals("mono.connect.mfa_submitted")) {
            name = "SUBMIT_MFA";
        } else if (type.equals("mono.connect.account_linked")) {
            name = "ACCOUNT_LINKED";
        } else if (type.equals("mono.connect.account_selected")) {
            name = "ACCOUNT_SELECTED";
        }

        if (event.has("data")) {
            body = event.getJSONObject("data");
        }

        return new ConnectEvent(name, body);
    }

    public String getEventName() {
        return this.eventName;
    }

    public JSONObject getData() {
        return this.data;
    }

}
