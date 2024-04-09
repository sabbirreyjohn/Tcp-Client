package xyz.androidrey.composepagination.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import xyz.androidrey.composepagination.domain.entity.ChatMessage
import xyz.androidrey.composepagination.domain.viewmodel.VlineViewModel
import java.util.UUID

@Composable
fun MessageBubble(message: ChatMessage) {
    // Define colors for user and server messages
    val bubbleColor = if (message.isUserMessage) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
    val textColor = if (message.isUserMessage) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
    val nameColor = if (message.isUserMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val userName = if (message.isUserMessage) "You" else "Server"
    val horizontalArrangement = if (message.isUserMessage) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = horizontalArrangement
    ) {
        Column(
            modifier = Modifier
                .background(color = bubbleColor, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = userName,
                color = nameColor,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInput(onMessageSent: (String) -> Unit) {
    var inputText by remember { mutableStateOf("") }
    val isEnabled = inputText.isNotBlank()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = inputText,
            onValueChange = { newText -> inputText = newText },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message") },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                onMessageSent(inputText)
                inputText = ""
            },
            modifier = Modifier
                .padding(start = 8.dp),
            enabled = isEnabled
        ) {
            Text("Send")
        }
    }
}

@Composable
fun ChatScreen() {

    val viewModel: VlineViewModel = viewModel()
    val messages by viewModel.messages.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(index = messages.size)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(state = listState,modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                MessageBubble(message = message)
            }
        }
        // Input field with elevation
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surfaceVariant, // Use a slightly different color to distinguish the input area
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp) // Rounded corners at the top
        ) {
            ChatInput {
                val userMessage = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    text = it,
                    timestamp = System.currentTimeMillis(),
                    isUserMessage = true
                )
                coroutineScope.launch {
                    viewModel.addMessageToChatBubble(userMessage)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChatScreen() {
    val sampleMessages = listOf(
        ChatMessage("1", "Hello", System.currentTimeMillis(), false),
        ChatMessage("2", "Hi, how are you?", System.currentTimeMillis(), true),
        ChatMessage("3", "I'm good, thanks! And you?", System.currentTimeMillis(), false)
    )

    ChatScreen()
}