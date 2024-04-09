package xyz.androidrey.composepagination.data

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow


object DataHolder {
    private val _isVlineConnectedToSocket = MutableStateFlow(false)
    val isVlineConnectedToSocket get() = _isVlineConnectedToSocket

    fun setIsVlineConnectedToSocket(value: Boolean) {
        _isVlineConnectedToSocket.value = value
    }

    private val _shouldRemindToReconnectWifi = MutableSharedFlow<Boolean>()
    val shouldRemindToReconnectWifi get() = _shouldRemindToReconnectWifi

    suspend fun setShouldRemindToReconnectWifi(value: Boolean) {
        _shouldRemindToReconnectWifi.emit(value)
    }

    private val _receivedMessage = MutableSharedFlow<String>()
    val receivedMessage = _receivedMessage.asSharedFlow()

    suspend fun setReceivedMessage(value: String) {
        _receivedMessage.emit(value)
    }
}