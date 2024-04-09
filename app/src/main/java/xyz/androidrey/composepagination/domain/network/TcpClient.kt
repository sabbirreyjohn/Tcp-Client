package xyz.androidrey.composepagination.domain.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import xyz.androidrey.composepagination.data.DataHolder
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class TcpClient(private val host: String, private val port: Int) {
    private var socket: Socket? = null
    private var inputReader: BufferedReader? = null
    private var outputWriter: PrintWriter? = null

    val receivedMessages = MutableStateFlow("")

    suspend fun connect() = withContext(Dispatchers.IO) {
        try {
            socket ?: run {
                socket = Socket(host, port)
                inputReader = BufferedReader(InputStreamReader(socket?.getInputStream()))
                outputWriter = PrintWriter(socket?.getOutputStream()!!, true)
                Timber.d("Vline tcp connected")
                DataHolder.setIsVlineConnectedToSocket(true)
            }

        } catch (e: Exception) {
            Timber.e("Vline tcp connection error: \n$e")
            DataHolder.setIsVlineConnectedToSocket(false)
        }
    }

    suspend fun disconnect() = withContext(Dispatchers.IO) {
        try {
            socket?.close()
            inputReader?.close()
            outputWriter?.close()
            socket = null
            inputReader = null
            outputWriter = null
            DataHolder.setIsVlineConnectedToSocket(false)
            Timber.d("Vline tcp disconnected")
        } catch (e: Exception) {
            Timber.e("Error while disconnecting: \n$e")
        }
    }

    suspend fun sendMessage(message: String) = withContext(Dispatchers.IO) {
        try {
            outputWriter?.println(message)
            Timber.d("Message sent: $message")
        } catch (e: Exception) {
            Timber.e("Error while sending message: \n$e")
        }
    }

    suspend fun receiveMessages() = withContext(Dispatchers.IO) {
        try {
            while (socket?.isConnected == true) {
                val message = inputReader?.readLine()
                Timber.d("Incoming message is: $message")
                message?.let {
                    receivedMessages.emit(it)
                }
            }
        } catch (e: Exception) {
            Timber.e("Error while receiving message")
            disconnect()
            DataHolder.setShouldRemindToReconnectWifi(true)
        }
    }

}