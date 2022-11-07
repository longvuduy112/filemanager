package com.longvuduy.filemanager.manager

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import com.longvuduy.filemanager.launcher.AudioFileActivity
import com.longvuduy.filemanager.launcher.ImageFileActivity
import com.longvuduy.filemanager.launcher.TextFileActivity
import com.longvuduy.filemanager.launcher.VideoFileActivity
import com.longvuduy.filemanager.service.FileActionReceiver
import com.longvuduy.filemanager.service.FileActionService
import com.longvuduy.filemanager.util.*
import java.io.File


abstract class DefaultFileManagerListener(private val context: Context, private val fileActionReceiver: FileActionReceiver):
    FileManagerChangeListener {


    override fun onRequestFileOpen(file: File): Boolean {
        val intent: Intent? = when(getTypeFromExtension(file.extension)) {
            FileTypes.TEXT, FileTypes.HTML -> Intent(context, TextFileActivity::class.java)
            FileTypes.IMAGE -> Intent(context, ImageFileActivity::class.java)
            FileTypes.VIDEO -> Intent(context, VideoFileActivity::class.java)
            FileTypes.AUDIO -> Intent(context, AudioFileActivity::class.java)

            else -> null
        }
        if(intent != null) {
            intent.putExtra(References.intentFilePath, file.absolutePath.toString())
            context.startActivity(intent)
            return true
        }
        return false
    }


    override fun onRequestFileOpenWith(file: File): Boolean {
        val uri = FileProvider.getUriForFile(context, "android.longvuduy", file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val mimeType: String = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension) ?: "*/*"
        intent.setDataAndType(uri, mimeType)
        try {
            context.startActivity(intent)
        }catch (e: ActivityNotFoundException){
            Toast.makeText(context, "No app can open this file.", Toast.LENGTH_LONG).show()
        }
        return true
    }

    override fun copyFile(targets: List<File>, dest: File) {
        val i = Intent(context, FileActionService::class.java)

        i.putExtra(References.intentAction, FileActions.COPY)
        i.putExtra(References.intentTargets, targets.map { f -> f.absolutePath }.toTypedArray())
        i.putExtra(References.intentDest, dest.absolutePath)
        i.putExtra(References.intentReceiver, fileActionReceiver)

        FileActionService.enqueueWork(context, i)
    }

    override fun moveFile(targets: List<File>, dest: File) {
        val i = Intent(context, FileActionService::class.java)

        i.putExtra(References.intentAction, FileActions.MOVE)
        i.putExtra(References.intentTargets, targets.map { f -> f.absolutePath }.toTypedArray())
        i.putExtra(References.intentDest, dest.absolutePath)
        i.putExtra(References.intentReceiver, fileActionReceiver)

        FileActionService.enqueueWork(context, i)
    }

    override fun deleteFile(targets: List<File>) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Confirm")
        builder.setMessage("Are you sure you want to delete selected files?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { _, _ ->
            val i = Intent(context, FileActionService::class.java)
            i.putExtra(References.intentAction, FileActions.DELETE)
            i.putExtra(References.intentTargets, targets.map { f -> f.absolutePath }.toTypedArray())
            i.putExtra(References.intentReceiver, fileActionReceiver)

            FileActionService.enqueueWork(context, i)
        }
        builder.setNegativeButton("No") { _, _ -> }

        builder.show()
    }

}