package com.longvuduy.filemanager.service

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver

/**
 * Lắng nghe kết quả FileActionService
 * Thông báo về việc hoàn thành thao tác sao chép, di chuyển, xoá tệp
 */
class FileActionReceiver(handler: Handler?) : ResultReceiver(handler) {
    private var receiver: Receiver? = null

    fun setReceiver(receiver: Receiver?) {
        this.receiver = receiver
    }

    interface Receiver {
        fun onReceiveResult(resultCode: Int, resultData: Bundle?)
    }

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        receiver?.onReceiveResult(resultCode, resultData)
    }
}