package com.smartfarm.myapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.smartfarm.myapplication.R
import com.smartfarm.myapplication.databinding.ActivityCctvBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.common.net.HttpHeaders
import com.smartfarm.myapplication.application.MyApp
import com.smartfarm.myapplication.data.Constants
import com.smartfarm.myapplication.network.SocketManager

class CCTVActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCctvBinding

    private lateinit var playerView: PlayerView
    private var player: SimpleExoPlayer? = null

    private lateinit var manager : SocketManager

    override fun onRestart() {
        super.onRestart()
        manager.emit(Constants.CCTV, true)
    }

    override fun onStart() {
        super.onStart()

        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        manager.emit(Constants.CCTV, false)
        releasePlayer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = SocketManager.getInstance(application)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cctv)

        manager.emit(Constants.CCTV, true)

        with(binding) {
            lifecycleOwner = this@CCTVActivity
            playerView = playerV
        }


    }

    private fun initializePlayer() {
        if (player == null) {
            player = SimpleExoPlayer.Builder(applicationContext).build()
            player!!.repeatMode = Player.REPEAT_MODE_ALL
            playerView.player = player
        }
        val agent = Util.getUserAgent(applicationContext, HttpHeaders.USER_AGENT)
        val factory = DefaultDataSourceFactory(applicationContext, agent)
        val src = "${MyApp.pref.serverAddress}${Constants.SOCKET_CCTV}"
        val uri = src.toUri()

        player!!.addMediaSource(
            HlsMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(uri))
        )
        player!!.prepare()
        player!!.playWhenReady = true
    }

    private fun releasePlayer() {
        if (player != null) {
            playerView.player = null
            player!!.release()
            player = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}