package mono.connect.widget.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import mono.connect.widget.ConnectWidget;
import mono.connect.widget.ConnectedAccount;
import mono.connect.widget.EventListener;

public class MainActivity extends AppCompatActivity implements EventListener {
  ConnectWidget mConnectWidget;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setup();
  }

  void setup() {
    String key = this.getString(R.string.connect_public_key);
    mConnectWidget = new ConnectWidget(this, key);
    mConnectWidget.setListener(this);

    View.OnClickListener onClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mConnectWidget.show();
      }
    };

    findViewById(R.id.launch_widget).setOnClickListener(onClickListener);
  }

  @Override
  public void onClose() {
    Toast.makeText(this, "widget closed", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onSuccess(ConnectedAccount account) {
    Toast.makeText(this, "Account successfully connected", Toast.LENGTH_LONG).show();
    Toast.makeText(this, String.format("Account auth code: %s", account.getCode()), Toast.LENGTH_LONG).show();
  }
}