package mono.connect.widget.sample

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import mono.connect.widget.ConnectWidget
import mono.connect.widget.ConnectedAccount
import mono.connect.widget.EventListener

class KotlinMainActivity : AppCompatActivity() , EventListener {

    private lateinit var button: Button
    private lateinit var connectWidget: ConnectWidget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_main)

        setup()

    }

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