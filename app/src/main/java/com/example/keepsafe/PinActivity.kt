package com.example.keepsafe

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

class PinActivity : AppCompatActivity() {

    private lateinit var dpm: DevicePolicyManager
    private lateinit var compName: ComponentName
    private val correctPin = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName = ComponentName(this, MyDeviceAdminReceiver::class.java)

        setContent {
            PinScreen(
                onSubmit = { enteredPin ->
                    if (enteredPin == correctPin) {
                        dpm.removeActiveAdmin(compName)
                        Toast.makeText(this, "Admin permissions removed", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}

@Composable
fun PinScreen(onSubmit: (String) -> Unit) {
    var pin by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = pin,
            onValueChange = { pin = it },
            label = { Text("Enter PIN") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onSubmit(pin) }) {
            Text(text = "Submit PIN")
        }
    }
}