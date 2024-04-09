package xyz.androidrey.composepagination.domain.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import xyz.androidrey.composepagination.domain.network.TcpClient
import com.volarious.vlinestatus.domain.notification.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.androidrey.composepagination.data.DataHolder
import xyz.androidrey.composepagination.data.IP_ADDRESS
import xyz.androidrey.composepagination.data.PORT_NUMBER
import javax.inject.Inject


@AndroidEntryPoint
class TheForegroundService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())


    @Inject
    lateinit var notificationHelper: NotificationHelper

    var tcpClient: TcpClient? = null

    override fun onCreate() {
        super.onCreate()
        tcpClient = TcpClient(IP_ADDRESS, PORT_NUMBER)
        notificationHelper.createNotificationChannel(NotificationHelper.SERVICE_CHANNEL)

        serviceScope.launch {
            launch {
                tcpClient?.receivedMessages?.collect {
                    DataHolder.setReceivedMessage(it)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP_SERVICE") {
            stopForeground(true)
            stopSelf()
            return START_NOT_STICKY
        }
        startForeground(
            NotificationHelper.SERVICE_NOTIFICATION_ID,
            notificationHelper.createNotification(
                NotificationHelper.SERVICE_CHANNEL,
                NotificationHelper.SERVICE_CHANNEL,
                "App is running in the background",
                android.R.drawable.ic_media_play,
                TheForegroundService::class.java

            )
        )
        connect()
        return START_NOT_STICKY
    }

    private fun connect() {
        serviceScope.launch {
            tcpClient?.connect()
            if (DataHolder.isVlineConnectedToSocket.value) {
                tcpClient?.receiveMessages()
                launch {
                    DataHolder.isVlineConnectedToSocket.collect {
                        if (!it) {
                            stopSelf()
                        }
                    }
                }
            }
        }
    }

    private fun disconnect() {
        serviceScope.launch {
            Timber.d("Disconnecting tcp")
            tcpClient?.disconnect()
            Timber.d("Disconnected TCP, now Delaying before canceling the job")
            delay(500)
            serviceScope.cancel()
            Timber.d("Job cancelled")
            tcpClient = null
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        disconnect()
        super.onDestroy()
        Timber.d("Service destroyed")
    }

}