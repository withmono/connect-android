package mono.connect.kit;

import android.util.Log;

public class Mono {

    public static ConnectKit create(MonoConfiguration config) {
        if (config.accountId != null) {
            Log.e(Constants.TAG, "You cannot pass an accountId: String to the default create function, use Mono.reauthorise() instead.");
        }

        MonoWebInterface.getInstance().reset();

        return new ConnectKit(config);

    }

    public static ConnectKit reauthorise(MonoConfiguration config) {
        if (config.accountId == null) {
            Log.e(Constants.TAG, "Reauthorisation requires you to pass an accountId: String to the configuration object.");
        }

        MonoWebInterface.getInstance().reset();

        return new ConnectKit(config);

    }

}
