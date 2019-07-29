package com.iven.musicplayergo

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.iven.musicplayergo.player.MediaPlayerHolder
import com.iven.musicplayergo.player.MusicNotificationManager

class PlayerService : Service() {

    // Binder given to clients
    private val binder = LocalBinder()

    //media player
    var mediaPlayerHolder: MediaPlayerHolder? = null
    lateinit var musicNotificationManager: MusicNotificationManager
    var isRestoredFromPause = false

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayerHolder != null) {
            mediaPlayerHolder!!.registerNotificationActionsReceiver(false)
            mediaPlayerHolder!!.release()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        /*This mode makes sense for things that will be explicitly started
        and stopped to run for arbitrary periods of time, such as a service
        performing background music playback.*/
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        if (mediaPlayerHolder == null) {
            mediaPlayerHolder = MediaPlayerHolder(this)
            musicNotificationManager = MusicNotificationManager(this)
            mediaPlayerHolder!!.registerNotificationActionsReceiver(true)
        }
        return binder
    }

    inner class LocalBinder : Binder() {
        // Return this instance of PlayerService so we can call public methods
        fun getService(): PlayerService = this@PlayerService
    }
}