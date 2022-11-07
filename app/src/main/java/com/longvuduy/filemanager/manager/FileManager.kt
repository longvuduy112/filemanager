package com.longvuduy.filemanager.manager


import android.util.Log
import com.longvuduy.filemanager.util.ClipboardMode
import com.longvuduy.filemanager.util.FileManagerChangeListener
import com.longvuduy.filemanager.util.MenuMode
import com.longvuduy.filemanager.versions.StateSaver
import java.io.File


object  FileManager {

    private var history: StateSaver<File> = StateSaver()

    val currentDirectory: File?
        get() = history.getCurrentInstance()
    var entries: ArrayList<FileEntry> = ArrayList()
        private set

    var menuMode: MenuMode = MenuMode.OPEN
        private set
    var clipboardMode: ClipboardMode = ClipboardMode.NONE
        private set
    var clipboard: ArrayList<File> = ArrayList()
        private set

    private var selectionSize : Int = 0

    private var listener: FileManagerChangeListener? = null


    private fun listFileEntries(file: File?): List<FileEntry> {
        if(file == null)
            return emptyList()
        return file.listFiles()
            .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
            .map { f -> FileEntry(f) }
    }

    fun goTo(file: File): Boolean {
        if(!file.exists())
            return false

        if(file.isDirectory) {
            history.goTo(file)
            refresh()
        } else {
            return requestFileOpen(file)
        }
        return true
    }

    fun goBack(): Boolean {
        if(history.goBack()) {
            refresh()
            return true
        }
        return false
    }

    fun goForward(): Boolean {
        if(history.goForward()) {
            refresh()
            return true
        }
        return false
    }

    fun canGoBack() : Boolean = history.canGoBack()

    fun canGoForward() : Boolean = history.canGoForward()

    fun refresh() {
        entries.clear()
        entries.addAll(listFileEntries(history.getCurrentInstance()))
        Log.d("long", "${listFileEntries(history.getCurrentInstance())}")

        if(menuMode == MenuMode.SELECT) toggleSelectionMode()
        notifyEntriesChanged()
    }

    fun toggleSelectionMode(){
        when(menuMode) {
            MenuMode.OPEN -> {
                menuMode = MenuMode.SELECT
            }
            MenuMode.SELECT -> {
                menuMode = MenuMode.OPEN
                for (f in entries){
                    f.selected = false
                    selectionSize = 0
                }


            }
        }
        notifySelectionModeChanged()
        notifyEntriesChanged()
    }

    fun toggleSelectionAt(i: Int){
        if(entries[i].selected){
            selectionSize--
        }else{
            selectionSize++
        }
        entries[i].selected = !entries[i].selected
        notifyEntriesChanged()
        notifySelectionModeChanged()
    }

    fun moveToClipboard(file: File, mode: ClipboardMode) {
        clipboardMode = mode
        clipboard.clear()
        clipboard.add(file)
        notifyClipboardChanged()
    }

    fun moveSelectedToClipboard(mode: ClipboardMode) {
        clipboardMode = mode
        when(menuMode){
            MenuMode.SELECT -> {
                clipboard.clear()
                clipboard.addAll(entries.filter { e -> e.selected }.map { e -> e.file })
            }
            MenuMode.OPEN -> {

                clipboard.clear()
            }

        }
        notifyClipboardChanged()
    }

    fun selectionEmpty(): Boolean {
        return entries.none { e -> e.selected }
    }

    fun canOpenWith(file: File) : Boolean {
        return !file.isDirectory
    }

    private fun copy() {
        listener?.copyFile(
            clipboard.filter { f -> currentDirectory?.startsWith(f) == false },
            currentDirectory as File
        )
    }

    private fun cut() {
        listener?.moveFile(
            clipboard
                .filter { f -> currentDirectory?.startsWith(f) == false }
                .filter { f -> currentDirectory?.resolve(f.name)?.exists() == false },
            currentDirectory as File
        )
    }

    fun delete(file: File) {
        listener?.deleteFile(
            listOf(file)
        )
    }

    fun deleteSelected() {
        listener?.deleteFile(
            entries.filter { e -> e.selected && e.file.exists() }.map {e -> e.file}
        )
    }

    fun paste() {
        when(clipboardMode) {
            ClipboardMode.NONE -> {
            }
            ClipboardMode.COPY -> {
                copy()

                clipboard.clear()
                clipboardMode = ClipboardMode.NONE
                notifyClipboardChanged()
            }
            ClipboardMode.CUT -> {
                cut()

                clipboard.clear()
                clipboardMode = ClipboardMode.NONE
                notifyClipboardChanged()
            }
        }
    }

    fun setListener(listener: FileManagerChangeListener) {
        this.listener = listener
    }

    private fun notifyEntriesChanged() {
        listener?.onEntriesChange()
    }

    private fun notifySelectionModeChanged() {
        listener?.onSelectionModeChange(menuMode)
    }

    private fun notifyClipboardChanged() {
        listener?.onClipboardChange(clipboardMode)
    }

    private fun requestFileOpen(file: File): Boolean {
        return listener?.onRequestFileOpen(file) == true
    }

    fun requestFileOpenWith(file: File): Boolean {
        return listener?.onRequestFileOpenWith(file) == true
    }

}