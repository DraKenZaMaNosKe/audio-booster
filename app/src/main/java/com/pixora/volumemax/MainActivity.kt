package com.pixora.volumemax

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.google.android.material.dialog.MaterialAlertDialogBuilder

@OptIn(UnstableApi::class)
class MainActivity : AppCompatActivity() {
    private lateinit var volumeController: VolumeController
    private lateinit var player: ExoPlayer
    private val gainController = SessionGainController()
    private lateinit var externalMediaControls: ExternalMediaControls

    private lateinit var tvVolumeValue: TextView
    private lateinit var tvTrack: TextView
    private lateinit var tvPlayerState: TextView
    private lateinit var tvGlobalState: TextView
    private lateinit var tvExternalTrack: TextView
    private lateinit var seekVolume: SeekBar
    private lateinit var btnPlayPause: Button
    private lateinit var boostDial: BoostDialView
    private lateinit var knobSprite: ImageView
    private lateinit var thunderEqualizer: ThunderEqualizerView
    private lateinit var tvSafeLimiter: TextView

    private var selectedUri: Uri? = null
    private var gainDb = 0
    private var globalPercent = 0
    private var globalWarningAccepted = false

    private val globalStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.getBooleanExtra(GlobalBoostService.EXTRA_AVAILABLE, false) == false) {
                globalPercent = 0
                refreshGlobalUi()
                Toast.makeText(this@MainActivity, R.string.global_boost_unavailable, Toast.LENGTH_LONG).show()
            } else if (intent?.getBooleanExtra(GlobalBoostService.EXTRA_AVAILABLE, false) == true) {
                globalPercent = GainMath.relativePercent(
                    intent.getFloatExtra(GlobalBoostService.EXTRA_GAIN_DB, 0f)
                )
                refreshGlobalUi()
            }
        }
    }

    private val mediaReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val title = intent?.getStringExtra(MediaObserverService.EXTRA_TITLE).orEmpty()
            val artist = intent?.getStringExtra(MediaObserverService.EXTRA_ARTIST).orEmpty()
            val source = intent?.getStringExtra(MediaObserverService.EXTRA_SOURCE).orEmpty()
            tvExternalTrack.text = if (title.isBlank()) getString(R.string.media_access_needed)
            else getString(R.string.external_track_format, title, artist, source)
        }
    }

    private val openAudio = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let(::loadAudio)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(R.layout.activity_main)

        volumeController = VolumeController(this)
        externalMediaControls = ExternalMediaControls(this)
        bindViews()
        createPlayer()
        setupVolumeControls()
        setupGlobalControls()
        setupPlayerControls()
        val globalPrefs = getSharedPreferences(GlobalBoostService.PREFS_NAME, MODE_PRIVATE)
        globalPercent = if (GlobalBoostService.isRunning) {
            globalPrefs.getInt(GlobalBoostService.PREF_GLOBAL_PERCENT, 0)
        } else {
            globalPrefs.edit().remove(GlobalBoostService.PREF_GLOBAL_PERCENT).apply()
            0
        }
        refreshVolumeUi()
        refreshGlobalUi()
        refreshPlayerUi()
    }

    private fun bindViews() {
        tvVolumeValue = findViewById(R.id.tvVolumeValue)
        tvTrack = findViewById(R.id.tvTrack)
        tvPlayerState = findViewById(R.id.tvPlayerState)
        tvGlobalState = findViewById(R.id.tvGlobalState)
        tvExternalTrack = findViewById(R.id.tvExternalTrack)
        seekVolume = findViewById(R.id.seekVolume)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        tvSafeLimiter = findViewById(R.id.tvSafeLimiter)
        boostDial = findViewById(R.id.boostDial)
        knobSprite = findViewById(R.id.knobSprite)
        thunderEqualizer = findViewById(R.id.thunderEqualizer)
        listOf(
            R.id.btnMediaAccess,
            R.id.btnSelectAudio,
            R.id.btnPlayPause,
            R.id.btnGainOff,
            R.id.btnGain3,
            R.id.btnGain6
        ).forEach { findViewById<Button>(it).backgroundTintList = null }
        boostDial.onPercentChanged = ::applyGlobalPreset
        boostDial.onPercentPreview = { updateKnobRotation(it, animate = false) }
        findViewById<Button>(R.id.btnMediaAccess).setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
        findViewById<Button>(R.id.btnExternalPrev).setOnClickListener {
            runExternalControl(externalMediaControls::previous)
        }
        findViewById<Button>(R.id.btnExternalPlayPause).setOnClickListener {
            runExternalControl(externalMediaControls::togglePlayPause)
        }
        findViewById<Button>(R.id.btnExternalNext).setOnClickListener {
            runExternalControl(externalMediaControls::next)
        }
        findViewById<Button>(R.id.btnThunderEq).setOnClickListener {
            Toast.makeText(this, R.string.equalizer_not_ready, Toast.LENGTH_LONG).show()
        }
    }

    private fun runExternalControl(action: () -> Boolean) {
        if (!action()) Toast.makeText(this, R.string.external_controls_unavailable, Toast.LENGTH_LONG).show()
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (android.os.Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 201)
        }
    }

    private fun setupGlobalControls() {
        val presets = mapOf(
            R.id.btnGlobalOff to 0,
            R.id.btnGlobal30 to 30,
            R.id.btnGlobal60 to 60,
            R.id.btnGlobal100 to 100,
            R.id.btnGlobal125 to 125,
            R.id.btnGlobal150 to 150,
            R.id.btnGlobal175 to 175,
            R.id.btnGlobalMax to 200
        )
        presets.forEach { (buttonId, percent) ->
            findViewById<Button>(buttonId).apply {
                backgroundTintList = ColorStateList.valueOf(presetColor(percent))
                setOnClickListener { applyGlobalPreset(percent) }
            }
        }
    }

    private fun presetColor(percent: Int): Int {
        if (percent == 0) return Color.rgb(53, 48, 88)
        val heat = (percent / 200f).coerceIn(0f, 1f)
        return Color.HSVToColor(floatArrayOf(205f * (1f - heat), .72f, .86f))
    }

    private fun applyGlobalPreset(percent: Int) {
        if (percent > 100 && !globalWarningAccepted) {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.warning_title)
                .setMessage(R.string.global_warning_body)
                .setPositiveButton(R.string.continue_label) { _, _ ->
                    globalWarningAccepted = true
                    applyGlobalPreset(percent)
                }
                .setNegativeButton(R.string.cancel_label) { _, _ ->
                    boostDial.percent = globalPercent
                    updateKnobRotation(globalPercent, animate = true)
                }
                .show()
            return
        }
        if (percent <= 100) {
            stopService(Intent(this, GlobalBoostService::class.java).setAction(GlobalBoostService.ACTION_STOP))
            volumeController.setVolumePercent(percent, showUi = true)
        } else {
            requestNotificationPermissionIfNeeded()
            volumeController.setVolumePercent(100, showUi = true)
            val gainDb = (20.0 * kotlin.math.log10(percent / 100.0)).toFloat()
                .coerceAtMost(GlobalAudioBoostController.MAX_GAIN_DB)
            val serviceIntent = Intent(this, GlobalBoostService::class.java)
                .setAction(GlobalBoostService.ACTION_SET_GAIN)
                .putExtra(GlobalBoostService.EXTRA_GAIN_DB, gainDb)
            ContextCompat.startForegroundService(this, serviceIntent)
        }
        globalPercent = percent
        refreshVolumeUi()
        refreshGlobalUi()
    }

    private fun refreshGlobalUi() {
        boostDial.percent = globalPercent
        updateKnobRotation(globalPercent, animate = true)
        thunderEqualizer.intensity = globalPercent
        val limiterDescription = getString(
            if (globalPercent > 100) R.string.safe_limiter_active else R.string.safe_limiter_idle
        )
        tvSafeLimiter.text = "SAFE"
        tvSafeLimiter.contentDescription = limiterDescription
        tvGlobalState.text = if (globalPercent > 100) "$globalPercent%" else "OFF"
        tvGlobalState.contentDescription = if (globalPercent > 100) {
            getString(R.string.global_state_format, globalPercent)
        } else getString(R.string.global_state_off)
    }

    private fun updateKnobRotation(percent: Int, animate: Boolean) {
        val target = percent.coerceIn(0, 200) * 2.7f
        if (animate) knobSprite.animate().rotation(target).setDuration(420L).start()
        else {
            knobSprite.animate().cancel()
            knobSprite.rotation = target
        }
    }

    private fun createPlayer() {
        val audioManager = getSystemService(AudioManager::class.java)
        val sessionId = audioManager.generateAudioSessionId()
        player = ExoPlayer.Builder(this).build().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                true
            )
            if (sessionId != AudioManager.ERROR) setAudioSessionId(sessionId)
            addListener(object : Player.Listener {
                override fun onAudioSessionIdChanged(audioSessionId: Int) {
                    val attached = gainController.attach(audioSessionId)
                    if (attached && gainDb > 0) gainController.setGainDb(gainDb)
                    Log.i(TAG, "Audio session changed: id=$audioSessionId, gainAttached=$attached")
                }
                override fun onIsPlayingChanged(isPlaying: Boolean) = refreshPlayerUi()
                override fun onPlaybackStateChanged(playbackState: Int) = refreshPlayerUi()
                override fun onPlayerError(error: PlaybackException) {
                    Toast.makeText(this@MainActivity, R.string.playback_error, Toast.LENGTH_LONG).show()
                    refreshPlayerUi()
                }
            })
        }

        if (player.audioSessionId > 0 && !gainController.attach(player.audioSessionId)) {
            Toast.makeText(this, R.string.gain_unavailable, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupVolumeControls() {
        seekVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    volumeController.setVolumeIndex(progress, showUi = true)
                    refreshVolumeUi()
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

        findViewById<Button>(R.id.btnVolume75).setOnClickListener {
            volumeController.setVolumePercent(75, showUi = true)
            refreshVolumeUi()
        }
        findViewById<Button>(R.id.btnVolume100).setOnClickListener {
            volumeController.setVolumePercent(100, showUi = true)
            refreshVolumeUi()
        }
    }

    private fun setupPlayerControls() {
        findViewById<Button>(R.id.btnSelectAudio).setOnClickListener {
            openAudio.launch(arrayOf("audio/*"))
        }
        btnPlayPause.setOnClickListener {
            if (selectedUri == null) openAudio.launch(arrayOf("audio/*"))
            else if (player.isPlaying) player.pause() else player.play()
        }
        findViewById<Button>(R.id.btnGainOff).setOnClickListener { applyGain(0) }
        findViewById<Button>(R.id.btnGain3).setOnClickListener { applyGain(3) }
        findViewById<Button>(R.id.btnGain6).setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.warning_title)
                .setMessage(R.string.warning_body)
                .setPositiveButton(R.string.continue_label) { _, _ -> applyGain(6) }
                .setNegativeButton(R.string.cancel_label, null)
                .show()
        }
    }

    private fun loadAudio(uri: Uri) {
        runCatching {
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        selectedUri = uri
        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare()
        tvTrack.text = displayName(uri)
        refreshPlayerUi()
    }

    private fun displayName(uri: Uri): String {
        contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) return cursor.getString(0)
        }
        return getString(R.string.selected_audio)
    }

    private fun applyGain(db: Int) {
        if (gainController.setGainDb(db)) {
            gainDb = db
            Log.i(TAG, "Local session gain applied: ${db}dB")
        } else {
            gainDb = 0
            Toast.makeText(this, R.string.gain_unavailable, Toast.LENGTH_LONG).show()
        }
        refreshPlayerUi()
    }

    private fun refreshVolumeUi() {
        seekVolume.max = volumeController.maxVolume
        seekVolume.progress = volumeController.currentVolume
        tvVolumeValue.text = getString(R.string.current_volume_format, volumeController.currentPercent)
    }

    private fun refreshPlayerUi() {
        btnPlayPause.text = if (player.isPlaying) "Ⅱ" else "▶"
        btnPlayPause.contentDescription = getString(if (player.isPlaying) R.string.pause else R.string.play)
        tvPlayerState.text = getString(
            R.string.player_state_format,
            GainMath.relativePercent(gainDb),
            gainDb
        )
    }

    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(
            this,
            globalStateReceiver,
            IntentFilter(GlobalBoostService.ACTION_STATE),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        ContextCompat.registerReceiver(
            this,
            mediaReceiver,
            IntentFilter(MediaObserverService.ACTION_MEDIA_UPDATE),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        if (::volumeController.isInitialized) refreshVolumeUi()
    }

    override fun onPause() {
        runCatching { unregisterReceiver(globalStateReceiver) }
        runCatching { unregisterReceiver(mediaReceiver) }
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        player.pause()
    }

    override fun onDestroy() {
        gainController.release()
        player.release()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "AudioBooster"
    }
}
