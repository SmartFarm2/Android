package com.smartfarm.myapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
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
import kotlinx.android.synthetic.main.activity_cctv.*

class CCTVActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCctvBinding

    lateinit var playerView: PlayerView
    var player: SimpleExoPlayer? = null

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cctv)

        playerView = playerV
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
}