package mono.connect.kit;

import org.json.JSONException;

public interface ConnectEventCallback {
    void run(ConnectEvent event) throws JSONException;
}
