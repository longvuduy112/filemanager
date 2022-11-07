package com.longvuduy.filemanager.launcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.longvuduy.filemanager.R
import com.longvuduy.filemanager.manager.FileManager
import com.longvuduy.filemanager.util.References
import java.io.*
import java.nio.charset.Charset

class AddTextFileActivity : AppCompatActivity() {

    private lateinit var edtFileName: EditText
    private lateinit var edtContent: TextView
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_text_file)

        edtFileName = findViewById(R.id.edtFileName)
        edtContent = findViewById(R.id.edtContent)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        filePath = intent.getStringExtra(References.intentAdd)
        Log.d("long", filePath)



        btnSave.setOnClickListener {
            val fileName = edtFileName.text
            val content = edtFileName.text
            if (fileName.isNullOrEmpty() || content.isNullOrEmpty()) {
                Toast.makeText(this, "Blank name or content", Toast.LENGTH_LONG).show()
            } else {
                saveTextFile()

            }

        }
        btnCancel.setOnClickListener {
            onBackPressed()
        }

    }

    private fun saveTextFile() {
        val fileName = edtFileName.text
        val content = edtContent.text
        try {
            val myObj = File("$filePath/${fileName}.txt")
            if (myObj.createNewFile()) {
                myObj.appendText(content.toString(), Charset.defaultCharset())
                FileManager.refresh()
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                onBackPressed()
            } else
                println("File already exists.")
        } catch (e: IOException) {
            println("An error occurred.")
            e.printStackTrace()
        }
    }

}
