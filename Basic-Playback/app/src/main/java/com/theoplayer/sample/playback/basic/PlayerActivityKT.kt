package com.theoplayer.sample.playback.basic

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.theoplayer.android.api.event.player.*
import com.theoplayer.android.api.player.Player
import com.theoplayer.android.api.source.SourceDescription
import com.theoplayer.android.api.source.SourceType
import com.theoplayer.android.api.source.TypedSource
import com.theoplayer.sample.playback.basic.databinding.ActivityPlayerBinding

class PlayerActivityKT: AppCompatActivity() {

    private val TAG: String = "PlayerActivityKT"

    private var viewBinding: ActivityPlayerBinding? = null
    private var theoPlayer: Player? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.TheoTheme_Base)
        super.onCreate(savedInstanceState)


        // Inflating view and obtaining an instance of the binding class.
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_player)

        // Gathering THEO objects references.
        theoPlayer = viewBinding?.theoPlayerView?.player
        theoPlayer!!.audioTracks

        // Configuring action bar.
        setSupportActionBar(viewBinding?.toolbarLayout?.toolbar)

        // Configuring THEOplayer playback with default parameters.
        configureTHEOplayer()
    }

    private fun configureTHEOplayer() {
        // Coupling the orientation of the device with the fullscreen state.
        // The player will go fullscreen when the device is rotated to landscape
        // and will also exit fullscreen when the device is rotated back to portrait.
        viewBinding?.theoPlayerView?.settings?.isFullScreenOrientationCoupled = true
        val LIVE = "http://chromecast.cvattv.com.ar/live/c3eds/FoxSports/SA_Live_dash_enc/FoxSports.mpd"
        val Live2 = "http://chromecast.cvattv.com.ar/live/c3eds/TelefeHD/SA_Live_dash_enc/TelefeHD.mpd"
        val licenseServer = "https://wv-client.cvattv.com.ar/?deviceId=NjliMjhiZDQwMmJiMTlhY2NiYmI5NmU2NGQyZmVkNmQ="
        val vrStream = "https://bitmovin-a.akamaihd.net/content/playhouse-vr/mpds/105560.mpd"

        // Creating a TypedSource builder that defines the location of a single stream source.
        val typedSource = TypedSource.Builder.typedSource(getString(R.string.defaultSourceUrl))//.type(SourceType.DASH)
        //val typedSource = TypedSource.Builder.typedSource(Live2).type(SourceType.DASH)

        // Creating a SourceDescription builder that contains the settings to be applied as a new
        // THEOplayer source.
        val sourceDescription = SourceDescription.Builder
                .sourceDescription(typedSource.build())
                .poster(getString(R.string.defaultPosterUrl))

        // Configuring THEOplayer with defined SourceDescription object.
        theoPlayer!!.source = sourceDescription.build()

        // Adding listeners to THEOplayer basic playback events.
        theoPlayer!!.addEventListener(PlayerEventTypes.PLAY, { event: PlayEvent? -> Log.i(TAG, "Event: PLAY") })
        theoPlayer!!.addEventListener(PlayerEventTypes.PLAYING, { event: PlayingEvent? -> Log.i(TAG, "Event: PLAYING") })
        theoPlayer!!.addEventListener(PlayerEventTypes.PAUSE, { event: PauseEvent? -> Log.i(TAG, "Event: PAUSE") })
        theoPlayer!!.addEventListener(PlayerEventTypes.ENDED, { event: EndedEvent? -> Log.i(TAG, "Event: ENDED") })
        theoPlayer!!.addEventListener(PlayerEventTypes.ERROR, { event: ErrorEvent -> Log.i(TAG, "Event: ERROR, error=" + event.error) })
    }


    // In order to work properly and in sync with the activity lifecycle changes (e.g. device
    // is rotated, new activity is started or app is moved to background) we need to call
    // the "onResume", "onPause" and "onDestroy" methods of the THEOplayerView when the matching
    // activity methods are called.

    // In order to work properly and in sync with the activity lifecycle changes (e.g. device
    // is rotated, new activity is started or app is moved to background) we need to call
    // the "onResume", "onPause" and "onDestroy" methods of the THEOplayerView when the matching
    // activity methods are called.
    override fun onPause() {
        super.onPause()
        viewBinding?.theoPlayerView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewBinding?.theoPlayerView?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding?.theoPlayerView?.onDestroy()
    }
}