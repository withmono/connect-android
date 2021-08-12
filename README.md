# Mono Connect Android SDK

The Mono Connect SDK is a quick and secure way to link bank accounts to Mono from within your Android app. Mono Connect is a drop-in framework that handles connecting a financial institution to your app (credential validation, multi-factor authentication, error handling, etc).

For accessing customer accounts and interacting with Mono's API (Identity, Transactions, Income, TransferPay) use the server-side [Mono API](https://docs.mono.co/docs/intro-to-mono-api).


## Documentation

For complete information about Mono Connect, head to the [docs](https://docs.mono.co/docs/intro-to-mono-connect-widget).


## Getting Started

1. Register on the [Mono](https://app.withmono.com/dashboard) website and get your public and secret keys.
2. Setup a server to [exchange tokens](https://docs.mono.co/reference/authentication-endpoint) to access user financial data with your Mono secret key.

## Installation

### Gradle

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
  implementation 'com.github.withmono:mono-connect-android:v1.0.1'
}
```

## Requirements

- Java 8 or higher
- The latest version of the Mono Connect Android SDK


## Usage

Before you can open Mono Connect, you need to first create a `publicKey`. Your `publicKey` can be found in the [Mono Dashboard](https://app.withmono.com/apps).


#### Import ConnectKit
```java
import mono.connect.kit.*;
```
#### Create a MonoConfiguration
```java
MonoConfiguration config = new MonoConfiguration.Builder(this,
  "test_pk_...", // your publicKey
  (account) -> {
    System.out.println("Successfully linked account. Code: " + account.getCode());
  }) // onSuccess function
  .addReference("test")
  .addReauthCode("code_xyz")
  .addOnEvent((event) -> {
    System.out.println("Triggered: "+event.getEventName());
  }) // onEvent function
  .addOnClose(() -> {
    System.out.println("Widget closed.");
  }) // onClose function
  .build();
```

#### Initialize a Mono Connect Widget
```java
ConnectKit widget = Mono.create(config);
```

#### Show the Widget
```java
View.OnClickListener onClickListener = new View.OnClickListener() {
  @Override
  public void onClick(View v) {
    widget.show();
  }
};

findViewById(R.id.launch_widget).setOnClickListener(onClickListener);
```
## Configuration Options

- [`publicKey`](#publicKey)
- [`onSuccess`](#onSuccess)
- [`onClose`](#onClose)
- [`onEvent`](#onEvent)
- [`reference`](#reference)
- [`reauthCode`](#reauthCode)
- [`selectedInstitution`](#selectedInstitution)



### <a name="publicKey"></a> `publicKey`
**String: Required**

This is your Mono public API key from the [Mono dashboard](https://app.withmono.com/apps).

```java
MonoConfiguration config = new MonoConfiguration.Builder(this,
  "test_pk_...", // your publicKey
  (account) -> {
    System.out.println("Successfully linked account. Code: " + account.getCode());
  }) // onSuccess function
  .build();
```

### <a name="onSuccess"></a> `onSuccess`
**(ConnectedAccount account) -> { Void }: Required**

The closure is called when a user has successfully onboarded their account. It should take a single String argument containing the code that can be [exchanged for an account id](https://docs.mono.co/reference/authentication-endpoint).

```java
MonoConfiguration config = new MonoConfiguration.Builder(this,
  "test_pk_...", // your publicKey
  (account) -> {
    System.out.println("Successfully linked account. Code: " + account.getCode());
  }) // onSuccess function
  .build();
```

### <a name="onClose"></a> `onClose`
**() -> { Void }: Optional**

The optional closure is called when a user has specifically exited the Mono Connect flow. It does not take any arguments.

```java
MonoConfiguration config = new MonoConfiguration.Builder(this,
  "test_pk_...", // your publicKey
  (account) -> {
    System.out.println("Successfully linked account. Code: " + account.getCode());
  }) // onSuccess function
  .addOnClose(() -> {
    System.out.println("Widget closed.");
  }) // onClose function
  .build();
```

### <a name="onEvent"></a> `onEvent`
**(ConnectEvent event) -> { Void }: Optional**

This optional closure is called when certain events in the Mono Connect flow have occurred, for example, when the user selected an institution. This enables your application to gain further insight into what is going on as the user goes through the Mono Connect flow.

See the [ConnectEvent](#connectEvent) object below for details.

```java
MonoConfiguration config = new MonoConfiguration.Builder(this,
  "test_pk_...", // your publicKey
  (account) -> {
    System.out.println("Successfully linked account. Code: " + account.getCode());
  }) // onSuccess function
  .addOnEvent((event) -> {
    System.out.println("Triggered: "+event.getEventName());
  }) // onEvent function
  .build();
```

### <a name="reference"></a> `reference`
**String: Optional**

When passing a reference to the configuration it will be passed back on all onEvent calls.

```java
MonoConfiguration config = new MonoConfiguration.Builder(this,
  "test_pk_...", // your publicKey
  (account) -> {
    System.out.println("Successfully linked account. Code: " + account.getCode());
  }) // onSuccess function
  .addReference("random_string")
  .build();
```

### <a name="reauthCode"></a> `reauthCode`
**String: Optional**

Reauthorisation of already authenticated accounts is done when MFA (Multi Factor Authentication) or 2FA is required by the institution for security purposes before more data can be fetched from the account.

Check Mono [docs](https://docs.mono.co/reference/data-sync-overview) on how to obtain `reauthCode` of an account.

```java
MonoConfiguration config = new MonoConfiguration.Builder(this,
  "test_pk_...", // your publicKey
  (account) -> {
    System.out.println("Successfully linked account. Code: " + account.getCode());
  }) // onSuccess function
  .addReauthCode("code_xyz")
  .build();
```
### <a name="selectedInstitution"></a> `selectedInstitution`
**String: Optional**

Passing the `selectedInstitution` option allows you to open  the widget directly to a financial institution's login page. It takes two options `String id` which is the id of the institution, and `String auth_method` which can be `"internet_banking"` or `"mobile_banking"` which will open to the respective bank login.

```java
MonoConfiguration config = new MonoConfiguration.Builder(this,
  "test_pk_...", // your publicKey
  (account) -> {
    System.out.println("Successfully linked account. Code: " + account.getCode());
  }) // onSuccess function
  .addSelectedInstitution(new MonoInstitution("5f2d08c060b92e2888287706", "internet_banking"))
  .build();
```




## API Reference

### Mono Object

The Mono Object provides two functions for easy interaction with the Mono Connect Widget. It provides two main methods `Mono.create(config: MonoConfiguration)` and `Mono.reauthorise(config: MonoConfiguration)` that both take a [MonoConfiguration](#MonoConfiguration).

### <a name="MonoConfiguration"></a> MonoConfiguration

The configuration option is passed to Mono.create(config: MonoConfiguration) or Mono.reauthorise(config: MonoConfiguration).

```java
publicKey: String // required
onSuccesss: (ConnectedAccount account) -> Void // required
onClose: () -> Void // optional
onEvent: (ConnectEvent event) -> Void // optional
reference: String // optional
reauthCode: String // optional
```
#### Usage

```java
MonoConfiguration config = new MonoConfiguration.Builder(this,
  "test_pk_...", // your publicKey
  (account) -> {
    System.out.println("Successfully linked account. Code: " + account.getCode());
  }) // onSuccess function
  .addReference("test")
  .addReauthCode("code_xyz")
  .addOnEvent((event) -> {
    System.out.println("Triggered: "+event.getEventName());
  }) // onEvent function
  .addOnClose(() -> {
    System.out.println("Widget closed.");
  }) // onClose function
  .build();
````

### <a name="connectEvent"></a> ConnectEvent

#### <a name="eventName"></a> `eventName: String`

Event names corespond to the `type` key returned by the event data. Possible options are in the table below.

| Event Name | Description |
| ----------- | ----------- |
| OPENED | Triggered when the user opens the Connect Widget. |
| SUCCESS | Triggered when the user successfully links their account and provides the code for autentication. |
| EXIT | Triggered when the user closes the Connect Widget. |
| INSTITUTION_SELECTED | Triggered when the user selects an institution. |
| AUTH_METHOD_SWITCHED | Triggered when the user changes authentication method from internet to mobile banking, or vice versa. |
| SUBMIT_CREDENTIALS | Triggered when the user presses Log in. |
| ACCOUNT_LINKED | Triggered when the user successfully links their account. |
| ACCOUNT_SELECTED | Triggered when the user selects a new account. |
| ERROR | Triggered when the widget reports an error.|


#### <a name="dataObject"></a> `data: JSONObject`
The data object of type JSONObect returned from the onEvent callback. You can access any property by `event.getData().get("PROPERT_NAME");` then casting it to the coresponding type.

```java
type: String // type of event mono.connect.xxxx
code: String // code returned from SUCCESS that is used to get account_id
reference: String // reference passed through the connect config
pageName: String // name of page the widget exited on
prevAuthMethod: String // auth method before it was last changed
authMethod: String // current auth method
mfaType: String // type of MFA the current user/bank requires
selectedAccountsCount: int // number of accounts selected by the user
errorType: String // error thrown by widget
errorMessage: String // error message describing the error
institutionId: String // id of institution
institutionName: String // name of institution
timestamp: int // unix timestamp of the event as an Integer
```

## Examples

#### Connecting a Financial Account

On a button click, get an auth `code` for a first time user from [Mono Connect Widget](https://docs.mono.co/docs/widgets).

**Note:** Exchange tokens or a `code` must be passed to your backend for final verification with your `secretKey` for you can retrieve financial information. See [Exchange Token](https://docs.mono.co/reference/authentication-endpoint).

```java
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

    MonoConfiguration config = new MonoConfiguration.Builder(this,
            key,
            (code) -> {
              System.out.println("Successfully linked account. Code: "+code.getCode());
            })
            .addReference("f8k1jg4a82ndb")
            .addOnEvent((event) -> {
              System.out.println("Triggered: "+event.getEventName());
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
```
##### Reauthorising an account with MFA

1. First you will need to get a Reauth token on your backend with the [Reauthorise API](https://docs.mono.co/reference/reauth-code).

2. Then you have to pass this token to the frontend for user authentication.

3. Complete the reauthorisation flow by passing the token to the widget configuration and open the widget.

**Note:** The reauth token expires in 10 minutes. You need to request a token on your backend and pass it to the frontend for use immediately.

```java
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

    MonoConfiguration config = new MonoConfiguration.Builder(this,
            key,
            (code) -> {
              System.out.println("Successfully reauthorised account. Code: "+code.getCode());
            })
            .addReference("f8k1jg4a82ndb")
            .addReauthCode("code_xyz")
            .build();

    mConnectKit = Mono.reauthorise(config);

    View.OnClickListener onClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mConnectKit.show();
      }
    };

    findViewById(R.id.launch_widget).setOnClickListener(onClickListener);

  }

}
```

## Support
If you're having general trouble with Mono Connect Android SDK or your Mono integration, please reach out to us at <hi@mono.co> or come chat with us on Slack. We're proud of our level of service, and we're more than happy to help you out with your integration to Mono.

## Contributing
If you would like to contribute to the Mono Connect Android SDK, please make sure to read our [contributor guidelines](https://github.com/withmono/conect-android/tree/master/CONTRIBUTING.md).


## License

[MIT](https://github.com/withmono/conect-android/tree/master/LICENSE) for more information.



## Deprecated Implementation

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

## Reauthorisation
Read more about Reauthorisation [here](https://docs.mono.co/reference#reauthorisation).

```java
  mLaunchWidgetButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      mConnectWidget.reauthorise("reauth token");
    }
  });
```
