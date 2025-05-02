package com.example.talkandtravel
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val videoView = findViewById<VideoView>(R.id.splashVideo)

        // Reference the MP4 file from res/raw folder
        val videoPath = "android.resource://${packageName}/${R.raw.tandt}"
        val uri = Uri.parse(videoPath)

        videoView.setVideoURI(uri)

        // Handle video playback
        videoView.setOnPreparedListener { mediaPlayer: MediaPlayer ->

            mediaPlayer.isLooping = false  // Loop the video
            videoView.start()
        }

        videoView.setOnCompletionListener {
            val intent = Intent(this, chooselanguage::class.java)  // Move to MainPage
            startActivity(intent)
            finish()
        }

    }
}