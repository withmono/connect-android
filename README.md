# Mono Connect Android SDK

The Mono Connect SDK is a quick and secure way to link bank accounts to Mono from within your Android app. Mono Connect is a drop-in framework that handles connecting a financial institution to your app (credential validation, multi-factor authentication, error handling, etc).

For accessing customer accounts and interacting with Mono's API (Identity, Transactions, Income, DirectPay) use the server-side [Mono API](https://docs.mono.co/api).


## Version 2 Public Beta

<b>Important</b>: Version 2 is currently in the public beta phase. This means it's available for testing and feedback from the community. Please be aware that there may be bugs, and some features might undergo changes before the stable release.

## Documentation

For complete information about Mono Connect, head to the [docs](https://docs.mono.co/docs/financial-data/overview).


## Getting Started

1. Register on the [Mono](https://app.mono.com) website and get your public and secret keys.
2. Set up a server to [exchange tokens](https://docs.mono.co/api/bank-data/authorisation/exchange-token) to access user financial data with your Mono secret key.

### Installation Guides
Follow the integrations guides for [Java Language](#java-integration), [Kotlin Language](#kotlin-integration), and [Jetpack Compose](#jetpack-compose).

### Java Integration
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
  implementation 'com.github.withmono:mono-connect-android:v2.2.0'
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
  .addCustomer(new MonoCustomer("customer_id"))
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
- [`customer`](#customer)
- [`onSuccess`](#onSuccess)
- [`onClose`](#onClose)
- [`onEvent`](#onEvent)
- [`reference`](#reference)
- [`accountId`](#accountId)
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

### <a name="customer"></a> `customer`
**String: Required**

```java
// For an existing customer, their customer ID can be passed directly
MonoCustomer customer = new MonoCustomer("65ca02a74e0e963044f0229d");

// If you don't have an existing customer, you can create one by providing their details.
// The customer will be created after the account connection is successful.
MonoCustomerIdentity identity = new MonoCustomerIdentity("bvn", "2011119422");
MonoCustomer customer = new MonoCustomer("Samuel Olumide", "example@gmail.com", identity);

MonoConfiguration config = new MonoConfiguration.Builder(this,
  "test_pk_...", // your publicKey
  (account) -> {
    System.out.println("Successfully linked account. Code: " + account.getCode());
  }) // onSuccess function
  .addCustomer(customer)
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

### <a name="accountId"></a> `accountId`
**String: Optional**

### Re-authorizing an Account with Mono: A Step-by-Step Guide
#### Step 1: Fetch Account ID for previously linked account

Fetch the Account ID of the linked account from the [Mono dashboard](https://app.mono.co/customers) or [API](https://docs.mono.co/docs/customers).

Alternatively, make an API call to the [Exchange Token Endpoint](https://api.withmono.com/v2/accounts/auth) with the code from a successful linking and your mono application secret key. If successful, this will return an Account ID.

##### Sample request:
```shell
curl --request POST \
  --url https://api.withmono.com/v2/accounts/auth \
  --header 'Content-Type: application/json' \
  --header 'accept: application/json' \
  --header 'mono-sec-key: your_secret_key' \
  --data '{"code":"string"}'
```

##### Sample response:
```json
{
  "id": "661d759280dbf646242634cc"
}
```

#### Step 2: Initiate your SDK with re-authorisation config option
With step one out of the way, pass the customer's Account ID to your config option in your installed SDK. Implementation example provided below for the Android SDK

```java
MonoConfiguration config = new MonoConfiguration.Builder(this,
  "test_pk_...", // your publicKey
  (account) -> {
    System.out.println("Successfully linked account. Code: " + account.getCode());
  }) // onSuccess function
  .addAccountId("account_xyz")
  .build();
```

#### Step 3: Trigger re-authorisation widget
In this final step, ensure the widget is launched with the new config. Once opened the user provides a security information which can be: password, pin, OTP, token, security answer etc.
If the re-authorisation process is successful, the user's account becomes re-authorised after which two things happen.
a. The 'mono.events.account_reauthorized' webhook event is sent to the webhook URL that you specified on your dashboard app.
b. Updated financial data gets returned on the Mono connect data APIs when an API request is been made.


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
customer: String // optional
onSuccesss: (ConnectedAccount account) -> Void // required
onClose: () -> Void // optional
onEvent: (ConnectEvent event) -> Void // optional
reference: String // optional
accountId: String // optional
```
#### Usage

```java
MonoConfiguration config = new MonoConfiguration.Builder(this,
  "test_pk_...", // your publicKey
  (account) -> {
    System.out.println("Successfully linked account. Code: " + account.getCode());
  }) // onSuccess function
  .addReference("test")
  .addAccountId("account_xyz")
  .addCustomer(new MonoCustomer(id: "mono_customer_id"))
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

**Note:** Exchange tokens or a `code` must be passed to your backend for final verification with your `secretKey` for you can retrieve financial information. See [Exchange Token](https://api.withmono.com/v2/accounts/auth).

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
            .addCustomer(new MonoCustomer(id: "mono_customer_id"))
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
##### Reauthorising an account

1. First you will need to fetch the Account ID for the previously linked account.

2. Then add this ID to the widget configuration object and open the widget.

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
            .addAccountId("account_xyz")
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
### Kotlin Integration
## Installation

There are two options to add the Mono Android Kotlin SDK to your project:

Option 1: Add the following to your project's build.gradle file:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.withmono:mono-connect-android:v2.2.0'
}
```
Option 2: Add the following to your project's settings.gradle file:
```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.withmono:mono-connect-android:v2.2.0'
}

```
## Usage
Create an instance of the ConnectKit class by passing in your public key and a callback to handle the authorization code:

```kotlin
private lateinit var mConnectKit: ConnectKit

private fun setup() {
    // replace your public key in strings.xml
    val key = getString(R.string.connect_public_key)

    val config = MonoConfiguration.Builder(this, key) { code ->
        println("Successfully linked account. Code: ${code.code}")
    }
        .addReference("test")
        .addCustomer(new MonoCustomer("customer_id"))
        .addOnEvent { event ->
            println("Triggered: ${event.eventName}")
            if (event.data.has("reference")) {
                println("ref: ${event.data.getString("reference")}")
            }
        }
        .addOnClose { println("Widget closed.") }
        .build()

    mConnectKit = Mono.create(config)
}
```

Call the show() method of the ConnectKit instance to launch the Mono widget:

```kotlin
val onClickListener = View.OnClickListener { mConnectKit.show() }

findViewById<View>(R.id.launch_widget).setOnClickListener(onClickListener)

```


### Jetpack Compose
## Installation
### Set up dependencies
​
Make sure your `build.gradle` files are set up as follows:
​
In build.gradle (app module):
​
```gradle
android {
// Rest of the code
    kotlinOptions {
        jvmTarget = '1.8'
    }
    buildFeatures {
        compose true
    }
    composeOptions {
        kotlinCompilerExtensionVersion '1.4.1'
    }
}
```
In build.gradle (project level):
```gradle
buildscript {
    ext {
        compose_version = '1.4.1'
    }
}
​
plugins {
// Rest of the code
    id 'org.jetbrains.kotlin.android' version '1.8.0' apply false
}
```
​
In the `settings.gradle` file, add the following:
```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```
​
And in the `dependencies` section of the `build.gradle` file, add the following:
​
```gradle
implementation 'com.github.withmono:mono-connect-android:v2.2.0'
```
​
## Usage

### Add ConnectKitActivity to the manifest

Add the following code to your AndroidManifest.xml file:
```xml
<application>
    <!-- Rest of the code -->
    <activity
        android:name="mono.connect.kit.ConnectKitActivity"
        android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
</application>
```
### Add the Mono Connect SDK to your app

Add the following code to your Jetpack Compose activity file:

```kotlin
@Composable
fun ConnectKitExample() {
    val context = LocalContext.current
    val key = context.getString(R.string.connect_public_key)

    val config = MonoConfiguration.Builder(context, key) { code ->
        println("Successfully linked account. Code: ${code.code}")
    }
        .addReference("test")
        .addCustomer(new MonoCustomer("customer_id"))
        .addOnEvent { event ->
            println("Triggered: ${event.eventName}")
            if (event.data.has("reference")) {
                println("ref: ${event.data.getString("reference")}")
            }
        }
        .addOnClose { println("Widget closed.") }
        .build()

    val mConnectKit = Mono.create(config)

    Column {
        Button(onClick = { mConnectKit.show() }) {
            Text(text = "Launch Widget")
        }
    }
}

```

Replace connect_public_key with your public key obtained from Mono Dashboard.


## Support
If you're having general trouble with Mono Connect Android SDK or your Mono integration, please reach out to us at <support@mono.co> or come chat with us on Slack. We're proud of our level of service, and we're more than happy to help you out with your integration to Mono.

## Contributing
If you would like to contribute to the Mono Connect Android SDK, please make sure to read our [contributor guidelines](https://github.com/withmono/conect-android/tree/master/CONTRIBUTING.md).


## License

[MIT](https://github.com/withmono/conect-android/tree/master/LICENSE) for more information.
