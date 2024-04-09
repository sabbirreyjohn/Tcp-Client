package xyz.androidrey.composepagination.domain.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.androidrey.composepagination.data.DataHolder
import xyz.androidrey.composepagination.domain.entity.ChatMessage
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class VlineViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    val isVlineSocketConnected = DataHolder.isVlineConnectedToSocket
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()


    init {
        viewModelScope.launch {
            DataHolder.receivedMessage.collect {
                if (it.isNotBlank()) {
                    val newMessage = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        text = it,
                        timestamp = System.currentTimeMillis(),
                        isUserMessage = false
                    )
                    addMessageToChatBubble(newMessage)
                }
            }
        }
    }

    fun addMessageToChatBubble(message: ChatMessage) {
        _messages.value += message
    }
}