package com.pixora.volumemax

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    private lateinit var volumeController: VolumeController
    private lateinit var tvVolumeValue: TextView
    private lateinit var tvBoostValue: TextView
    private lateinit var seekVolume: SeekBar
    private lateinit var seekBoost: SeekBar
    private lateinit var switchLimiter: SwitchMaterial
    private lateinit var btnForegroundToggle: Button

    private var boostPercent: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        volumeController = VolumeController(this)
        bindViews()
        requestNotificationPermissionIfNeeded()
        setupVolumeControls()
        setupBoostControls()
        setupForegroundToggle()
        refreshUi()
    }

    private fun bindViews() {
        tvVolumeValue = findViewById(R.id.tvVolumeValue)
        tvBoostValue = findViewById(R.id.tvBoostValue)
        seekVolume = findViewById(R.id.seekVolume)
        seekBoost = findViewById(R.id.seekBoost)
        switchLimiter = findViewById(R.id.switchLimiter)
        btnForegroundToggle = findViewById(R.id.btnForegroundToggle)
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQ_NOTIFICATIONS
                )
            }
        }
    }

    private fun setupVolumeControls() {
        seekVolume.max = volumeController.maxVolume
        seekVolume.progress = volumeController.currentVolume

        seekVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    volumeController.setVolumeIndex(progress, showUi = true)
                    refreshUi()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

        findViewById<Button>(R.id.btnVolume75).setOnClickListener {
            volumeController.setVolumePercent(75, showUi = true)
            refreshUi()
        }

        findViewById<Button>(R.id.btnVolume100).setOnClickListener {
            volumeController.setVolumePercent(100, showUi = true)
            refreshUi()
        }
    }

    private fun setupBoostControls() {
        seekBoost.max = 100
        seekBoost.progress = boostPercent

        seekBoost.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    updateBoost(progress, confirmHigh = true)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })

        findViewById<Button>(R.id.btnBoostOff).setOnClickListener {
            updateBoost(0, confirmHigh = false)
        }

        findViewById<Button>(R.id.btnBoostLow).setOnClickListener {
            updateBoost(35, confirmHigh = false)
        }

        findViewById<Button>(R.id.btnBoostHigh).setOnClickListener {
            updateBoost(85, confirmHigh = true)
        }
    }

    private fun setupForegroundToggle() {
        btnForegroundToggle.setOnClickListener {
            val stage = boostStage()
            if (stage == BoostStage.OFF) {
                stopBoostService()
                Toast.makeText(this, "Boost desactivado", Toast.LENGTH_SHORT).show()
            } else {
                startBoostService()
            }
        }
    }

    private fun updateBoost(progress: Int, confirmHigh: Boolean) {
        val stage = BoostStage.fromProgress(progress)
        if (stage == BoostStage.HIGH && confirmHigh) {
            showHighBoostWarning {
                applyBoost(stage, progress)
            }
        } else {
            applyBoost(stage, progress)
        }
    }

    private fun applyBoost(stage: BoostStage, progress: Int) {
        boostPercent = progress
        seekBoost.progress = progress

        when (stage) {
            BoostStage.OFF -> stopBoostService()
            BoostStage.LOW -> {
                volumeController.setVolumePercent(stage.minVolumePercent, showUi = true)
                startBoostService()
            }
            BoostStage.HIGH -> {
                volumeController.setVolumePercent(stage.minVolumePercent, showUi = true)
                startBoostService()
            }
        }

        refreshUi()
    }

    private fun showHighBoostWarning(onAccept: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.warning_title))
            .setMessage(getString(R.string.warning_body))
            .setPositiveButton(getString(R.string.continue_label)) { dialog, _ ->
                dialog.dismiss()
                onAccept()
            }
            .setNegativeButton(getString(R.string.cancel_label), null)
            .show()
    }

    private fun startBoostService() {
        val intent = Intent(this, BoostForegroundService::class.java)
            .setAction(BoostForegroundService.ACTION_START_BOOST)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun stopBoostService() {
        val intent = Intent(this, BoostForegroundService::class.java)
            .setAction(BoostForegroundService.ACTION_STOP_BOOST)
        startService(intent)
    }

    private fun boostStage(): BoostStage = BoostStage.fromProgress(boostPercent)

    private fun refreshUi() {
        val percent = volumeController.currentPercent
        tvVolumeValue.text = getString(R.string.current_volume_format, percent)

        val stage = boostStage()
        tvBoostValue.text = getString(
            R.string.boost_format,
            stage.label,
            stage.estimatedDb
        )
    }

    companion object {
        private const val REQ_NOTIFICATIONS = 1001
    }
}
