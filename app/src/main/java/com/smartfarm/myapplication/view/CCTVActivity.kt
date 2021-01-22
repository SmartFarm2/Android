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
import com.smartfarm.myapplication.viewmodel.CCTVActivityViewModel
import com.smartfarm.myapplication.viewmodel.CCTVActivityViewModelFactory

class CCTVActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCctvBinding
    private lateinit var viewModel: CCTVActivityViewModel
    private lateinit var viewModelFactory: CCTVActivityViewModelFactory

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
        viewModelFactory = CCTVActivityViewModelFactory(20, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CCTVActivityViewModel::class.java)

        manager.emit(Constants.CCTV, true)

        with(binding) {
            myViewModel = viewModel
            lifecycleOwner = this@CCTVActivity

            cycle.infoBox.setOnClickListener {
                viewModel.setCycle()
            }

            door.infoBox.setOnClickListener {
                viewModel.setDoor()
            }

            playerView = playerV
        }

        with(viewModel){
            observing()
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
        viewModel.deobserving()
        super.onDestroy()
    }
}