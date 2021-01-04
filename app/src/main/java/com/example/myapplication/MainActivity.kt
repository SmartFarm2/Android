package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.common.net.HttpHeaders
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val LOG_KEY = "MAIN_LOG"

    lateinit var playerView: PlayerView
    var player: SimpleExoPlayer? = null

    private val currentWindow = 0
    private val playBackPos = 0
    override fun onStart() {
        super.onStart()

        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = playerV

        var bool = false;
        SocketManager.getInstance(application).addEvent(Constants.SOCKET_DOOR) {
            val temp = it.get(0)
            CoroutineScope(Dispatchers.Main).launch {
                test.text = "$temp"
                //SocketService.makeNotification(this@MainActivity, Constants.CONNECTED_KEY)
            }
        }
        button.setOnClickListener {
            SocketManager.getInstance(application).emit(Constants.SOCKET_DOOR_CHANGE, "1,$bool")
            bool = !bool
        }
    }

    fun initializePlayer() {
        if (player == null) {
            player = SimpleExoPlayer.Builder(applicationContext).build()
            player!!.repeatMode = Player.REPEAT_MODE_ALL
            playerView.player = player
        }
        val agent = Util.getUserAgent(applicationContext, HttpHeaders.USER_AGENT)
        val factory = DefaultDataSourceFactory(applicationContext, agent)
        val src = "http://demo.unified-streaming.com/video/tears-of-steel/tears-of-steel.ism/.m3u8"
        val uri = src.toUri()

        player!!.addMediaSource(HlsMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(uri)))
        player!!.prepare()
        player!!.playWhenReady = true
    }
    fun releasePlayer() {
        if(player != null) {
            playerView.player = null
            player!!.release()
            player = null
        }
    }

    override fun onDestroy() {
        SocketManager.getInstance(application).removeEvent(Constants.SOCKET_TEMP)
        super.onDestroy()
    }
}