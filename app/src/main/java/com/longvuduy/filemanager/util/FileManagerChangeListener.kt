package com.longvuduy.filemanager.util

import java.io.File

/**
 * Trình lắng nghe event từ FileManager
 * Sử dụng để cập nhật danh sách các tệp hoặc mở
 */
interface FileManagerChangeListener {
    /**
     * Callback khi tệp trong clipboard thay đổi
     */
    fun onEntriesChange()

    /**
     * Callback khi menu mode thay đổi (OPEN, SELECT)
     */
    fun onSelectionModeChange(mode : MenuMode)

    /**
     * Callback khi clipboard mode thay đổi
     */
    fun onClipboardChange(mode : ClipboardMode)

    /**
     * Callback khi yêu cầu mở tệp bằng ứng dụng này
     */
    fun onRequestFileOpen(file: File): Boolean

    /**
     * Callback khi yêu cầu mở tệp bằng ứng dụng hệ thống

     */
    fun onRequestFileOpenWith(file: File) : Boolean

    /**
     * Callback khi sao chép tệp
     */
    fun copyFile(targets: List<File>, dest: File)

    /**
     * Callback khi di chuyển tệp
     */
    fun moveFile(targets: List<File>, dest: File)

    /**
     * Callback khi xoá tệp
     */
    fun deleteFile(targets: List<File>)
}