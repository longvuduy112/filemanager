package com.longvuduy.filemanager.launcher

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.longvuduy.filemanager.R
import com.longvuduy.filemanager.util.References
import java.io.File


class ImageFileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_file)

        val imageTextView = findViewById<TextView>(R.id.imageTitletv)
        val imageView = findViewById<ImageView>(R.id.imageView)

        val filePath = intent.getStringExtra(References.intentFilePath)
        val f = File(filePath)

        imageTextView.text = f.name
        if(f.exists()){
            val myBitmap: Bitmap = BitmapFactory.decodeFile(f.absolutePath)
            imageView.setImageBitmap(myBitmap)
        }
    }
}
