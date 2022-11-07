package com.longvuduy.filemanager.versions

/**
 * Interface hỗ trợ các đối tượng ghi nhớ lịch sử thay đổi
 */
interface Versionable<T> {

    // Trả về trạng thái hiện tại
    fun getCurrentInstance(): T?

    // Chuyển sang trạng thái mới
    fun goTo(newElement: T): Boolean

    // Chuyển về trạng thái trước đó
    fun goBack(): Boolean

    // Trở lại trạng thái tiếp theo, chỉ gọi khi hàm goBack được gọi trước đó
    fun goForward(): Boolean

}