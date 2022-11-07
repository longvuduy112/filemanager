package com.longvuduy.filemanager.launcher

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.longvuduy.filemanager.R
import com.longvuduy.filemanager.manager.FileManager
import com.longvuduy.filemanager.util.References
import java.io.File


class AddFolderActivity : AppCompatActivity() {
    private lateinit var edtFolderName: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private lateinit var filePath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_folder)

        filePath = intent.getStringExtra(References.intentAddFolder)
        Log.d("long", filePath)


        edtFolderName = findViewById(R.id.edtFolderName)

        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)

        btnCancel.setOnClickListener {
            onBackPressed()
        }
        btnSave.setOnClickListener {
            if(edtFolderName.text.isNullOrEmpty()) {
                Toast.makeText(this, "Blank name", Toast.LENGTH_LONG).show()
            }
            else {
                val mediaStorageDir = File(filePath, "${edtFolderName.text}")
                FileManager.refresh()
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                onBackPressed()
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d("App", "failed to create directory")
                    }
                }
            }
        }


    }
}