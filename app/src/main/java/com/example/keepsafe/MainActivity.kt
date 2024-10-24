package com.example.keepsafe

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.keepsafe.ui.theme.KeepSafeTheme

class MainActivity : ComponentActivity() {

    private lateinit var dpm: DevicePolicyManager
    private lateinit var compName: ComponentName
    private val REQUEST_CODE_ENABLE_ADMIN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        compName = ComponentName(this, MyDeviceAdminReceiver::class.java)

        enableEdgeToEdge()
        setContent {
            KeepSafeTheme {
                EnableAdminButton { enableDeviceAdmin() }
                DisableAdminButton { disableAdmin() }
            }
        }
    }

    private fun enableDeviceAdmin() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
        intent.putExtra(
            DevicePolicyManager.EXTRA_ADD_EXPLANATION,
            "App needs admin permissions to block uninstallation"
        )
        startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN)
    }

    private fun disableAdmin() {
        if (dpm.isAdminActive(compName)) {
            val intent = Intent(this, PinActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Admin permissions are not active", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ENABLE_ADMIN) {
            val message = if (resultCode == RESULT_OK) {
                "Admin permissions granted"
            } else {
                "Admin permissions denied"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun EnableAdminButton(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onClick) {
            Text(text = "Enable Admin Permissions")
        }
    }
}

@Composable
fun DisableAdminButton(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onClick) {
            Text(text = "Disable Admin Permissions")
        }
    }
}