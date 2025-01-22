package mono.connect.kit;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

public class ConnectKit {
    private final String key;
    private final Context context;
    private ConnectData data;

    //optionals
    private String accountId = null;

    private MonoInstitution selectedInstitution = null;

    private final Map<String, String> params = new HashMap<>();

    public ConnectKit(Context context, String key) {
        this.context = context;
        this.key = key;
    }

    public ConnectKit(MonoConfiguration config) {
        this.context = config.context;
        this.key = config.publicKey;

        if (config.scope != null) {
            this.params.put(Constants.KEY_SCOPE, config.scope);
        }

        if (config.reference != null) {
            this.params.put(Constants.KEY_REFERENCE, config.reference);
            MonoWebInterface.getInstance().setReference(config.reference);
        }

        if (config.accountId != null) {
            this.accountId = config.accountId;
            if (config.scope == null || config.scope.equals(Constants.SCOPE)) {
                this.params.put(Constants.KEY_SCOPE, Constants.REAUTH_SCOPE);
            }
        }

        if (config.onSuccess != null) {
            MonoWebInterface.getInstance().setOnSuccess(config.onSuccess);
        }
        if (config.onClose != null) {
            MonoWebInterface.getInstance().setOnClose(config.onClose);
        }
        if (config.onEvent != null) {
            MonoWebInterface.getInstance().setOnEvent(config.onEvent);
        }

        if (config.selectedInstitution != null) {
            this.selectedInstitution = config.selectedInstitution;
        }

        if (config.customer != null) {
            this.data = new ConnectData(config.customer);
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
        String data = new Gson().toJson(this.data);

        if (this.accountId != null) {
            try {
                JSONObject fullData = new JSONObject(data);
                fullData.put("account", this.accountId);

                data = fullData.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Constants.URL_SCHEME)
                .authority(Constants.CONNECT_URL)
                .appendQueryParameter(Constants.KEY_VERSION, Constants.VERSION)
                .appendQueryParameter("key", this.key)
                .appendQueryParameter("data", data);

        if (selectedInstitution != null) {
            builder.appendQueryParameter("selectedInstitution", "{\"id\":\"" + selectedInstitution.getId()
                    + "\", \"auth_method\": \"" + selectedInstitution.getAuthMethod() + "\"}");
        }

        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        return builder.build().toString();
    }
}

class ConnectData {
    MonoCustomer customer;

    public ConnectData(MonoCustomer customer) {
        this.customer = customer;
    }
}
