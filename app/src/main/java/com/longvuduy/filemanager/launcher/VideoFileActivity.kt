package com.longvuduy.filemanager.launcher

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.longvuduy.filemanager.R
import com.longvuduy.filemanager.util.References
import kotlinx.android.synthetic.main.activity_video_file.*


class VideoFileActivity : AppCompatActivity() {

    private var continueFrom = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        if (supportActionBar != null)
            supportActionBar?.hide()

        setContentView(R.layout.activity_video_file)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR

        val path = intent.getStringExtra(References.intentFilePath)

        val videoView = findViewById<VideoView>(R.id.videoView)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.keepScreenOn = true

        videoView.setOnErrorListener { _, _, _ ->
            Toast.makeText(applicationContext, "Error playing media", Toast.LENGTH_LONG).show()
            finish()
            false
        }

        videoView.setVideoPath(path)
        videoView.requestFocus()

        videoView.start()
    }

    override fun onPause() {
        super.onPause()
        continueFrom = videoView.currentPosition
        videoView.stopPlayback()
    }

    override fun onResume() {
        super.onResume()
        videoView.seekTo(continueFrom)
//        videoView.start()
    }

}
