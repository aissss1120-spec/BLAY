package com.blay.assistant

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 48, 32, 32)
        }

        status = TextView(this).apply {
            text = "BLAY v1.8 — siap"
            textSize = 22f
        }

        val standby = Button(this).apply {
            text = "Aktifkan Standby BLAY"
            setOnClickListener { startStandby() }
        }

        val stop = Button(this).apply {
            text = "Matikan Standby"
            setOnClickListener {
                stopService(Intent(this@MainActivity, BlayStandbyService::class.java))
                status.text = "Standby dimatikan"
            }
        }

        val speak = Button(this).apply {
            text = "Tes Suara"
            setOnClickListener { speak("Halo, saya BLAY.") }
        }

        layout.addView(status)
        layout.addView(standby)
        layout.addView(stop)
        layout.addView(speak)
        setContentView(layout)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                100
            )
        }
    }

    private fun startStandby() {
        val intent = Intent(this, BlayStandbyService::class.java)
        try {
            androidx.core.content.ContextCompat.startForegroundService(this, intent)
            status.text = "BLAY standby aktif"
        } catch (e: Exception) {
            status.text = "Gagal memulai standby: ${e.message}"
        }
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "blay")
    }

    override fun onInit(statusCode: Int) {
        if (statusCode == TextToSpeech.SUCCESS) {
            tts.language = Locale("id", "ID")
        }
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}
