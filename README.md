# Mono Connect Android Sdk

This is the official implementation of the mono connect widget for integration with android apps.

Request access [here](https://app.withmono.com/register) to get your API keys

# Installation

## Gradle

```sh
build.gradle

allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

```sh
dependencies {
  implementation 'com.github.withmono:mono-connect-android:v1.0.0'
}
```

## Implementation

```java
import mono.connect.widget.ConnectWidget;
import mono.connect.widget.ConnectedAccount;
import mono.connect.widget.EventListener;


public class MainActivity extends AppCompatActivity implements EventListener {

  void setup() {
    String key = this.getString(R.string.connect_public_key);
    
    mConnectWidget = new ConnectWidget(this, key);
    mConnectWidget.setListener(this);

    Button mLaunchWidgetButton = findViewById(R.id.launch_widget);
    mLaunchWidgetButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mConnectWidget.show();
      }
    });
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
```

## Implementation In kotlin
```kotlin
import mono.connect.widget.ConnectWidget
import mono.connect.widget.ConnectedAccount
import mono.connect.widget.EventListener


class KotlinMainActivity : AppCompatActivity() , EventListener {

    private lateinit var button: Button
    private lateinit var connectWidget: ConnectWidget

      private fun setup() {
            button = findViewById(R.id.launch_widget_k)
            val key = getString(R.string.connect_public_key)

            connectWidget = ConnectWidget(this, key)
            connectWidget.setListener(this)

            button.setOnClickListener {
                connectWidget.show()
            }
        }

     override fun onClose() {
         Toast.makeText(this, "widget closed", Toast.LENGTH_LONG).show() }

     override fun onSuccess(account: ConnectedAccount?) {
         Toast.makeText(this, "Account successfully connected", Toast.LENGTH_LONG).show()
         Toast.makeText(this, "Account auth code: ${account?.code}", Toast.LENGTH_LONG).show()
     }
}
```
