package mono.connect.widget.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import mono.connect.kit.*;

public class MainActivity extends AppCompatActivity {
  ConnectKit mConnectKit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setup();
  }

  void setup() {
    String key = this.getString(R.string.connect_public_key);

     MonoIdentity identity = new MonoIdentity("bvn", "2011119422");
     MonoCustomer customer = new MonoCustomer("Samuel Olumide", "example@gmail.com", identity);
    // MonoCustomer customer = new MonoCustomer("65a7aac0bbeafdb9352daab6");

    MonoConfiguration config = new MonoConfiguration.Builder(this,
            key,
            (code) -> {
              Log.d("result","Successfully linked account. Code: "+code.getCode());
            })
            .addReference("f8k1jg4a82ndb")
            .addCustomer(customer)
            .addOnEvent((event) -> {
              System.out.println("Triggered: "+event.getEventName());
              if(event.getData().has("reference")){
                System.out.println("ref: "+event.getData().getString("reference"));
              }
            })
//            .addSelectedInstitution(new MonoInstitution("5f2d08c060b92e2888287706", "internet_banking"))
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
