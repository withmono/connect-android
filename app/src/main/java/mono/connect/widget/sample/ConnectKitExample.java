package mono.connect.widget.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import mono.connect.kit.*;

public class ConnectKitExample extends AppCompatActivity {
    ConnectKit mConnectKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();
    }

    void setup() {
        // replace your public key in strings.xml
        String key = this.getString(R.string.connect_public_key);

        MonoConfiguration config = new MonoConfiguration.Builder(this,
                key,
                (code) -> {
                    System.out.println("Successfully linked account. Code: "+code.getCode());
                })
                .addReference("test")
                .addReauthCode("code_xyz")
                .addOnEvent((event) -> {
                    System.out.println("Triggered: "+event.getEventName());
                })
                .addOnClose(() -> {
                    System.out.println("Widiget closed.");
                })
                .build();

        mConnectKit = Mono.create(config);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnectKit.show();
            }
        };

        findViewById(R.id.launch_widget).setOnClickListener(onClickListener);

    }

}
