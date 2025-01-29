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
        MonoCustomerIdentity identity = new MonoCustomerIdentity("bvn", "2011119422");
        MonoCustomer customer = new MonoCustomer("Samuel Olumide", "example@gmail.com", identity);
        // or use an existing customer
        // MonoCustomer customer = new MonoCustomer("65a7aac0bbeafdb9352daab6");

        MonoConfiguration config = new MonoConfiguration.Builder(this,
                key,
                (code) -> {
                    System.out.println("Successfully linked account. Code: "+code.getCode());
                })
                .addReference("test")
                .addCustomer(customer)
//                .addAccountId("account_xyz")
                .addOnEvent((event) -> {
                    System.out.println("Triggered: "+event.getEventName());
                    if(event.getData().has("reference")){
                        System.out.println("ref: "+event.getData().getString("reference"));
                    }
                })
                .addOnClose(() -> {
                    System.out.println("Widget closed.");
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
