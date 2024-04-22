package com.xinhua.language.wanbang.utils

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

fun AppCompatActivity.takeScreenshotAndShare(view: View,share: Boolean = false) {
    // 创建 bitmap
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    // 保存 bitmap 到文件
    val imagePath = saveBitmap(bitmap,!share)
    // 分享图片
    if (share)
    shareImage(imagePath)
}
private fun AppCompatActivity.saveBitmap(bitmap: Bitmap,showToast:Boolean = false): String {
    val imagesDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "screenshots")
    if (!imagesDir.exists()) {
        imagesDir.mkdirs()
    }
    val file = File(imagesDir, "screenshot_${System.currentTimeMillis()}.jpg")
    val fos: OutputStream?
    try {
        fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // 添加到媒体库
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.TITLE, "今日学习")
        put(MediaStore.Images.Media.DESCRIPTION, "新概念英语")
        put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.DATA, file.absolutePath)
    }
    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    if (showToast)
    Toast.makeText(this,"图片保存在${file.absolutePath}",Toast.LENGTH_SHORT).show()
    return file.absolutePath
}

private fun AppCompatActivity.shareImage(imagePath: String) {
    val imageFile = File(imagePath)
    val uri = FileProvider.getUriForFile(
        this,
        "${this.packageName}",
        imageFile
    )
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/jpeg"
    }
    startActivity(Intent.createChooser(shareIntent, "分享"))
}
