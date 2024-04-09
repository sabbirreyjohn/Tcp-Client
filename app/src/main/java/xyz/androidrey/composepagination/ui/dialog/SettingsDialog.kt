package xyz.androidrey.composepagination.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SettingsDialog(onDismissRequest: () -> Unit) {
    // State for text inputs
    var settingOne by remember { mutableStateOf("") }
    var settingTwo by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = settingOne,
                    onValueChange = { settingOne = it },
                    label = { Text("Setting One") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = settingTwo,
                    onValueChange = { settingTwo = it },
                    label = { Text("Setting Two") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onDismissRequest() }) {
                    Text("Save")
                }
            }
        }
    }
}