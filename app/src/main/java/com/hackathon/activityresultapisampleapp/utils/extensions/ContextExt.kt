package com.hackathon.activityresultapisampleapp.utils.extensions

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.util.*

/**
 * @Author: Anurag Kumar kachhala
 * @Date: 03/06/22
 */

fun Context?.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.checkPermission(permission: String) =
    ContextCompat.checkSelfPermission(
        this, permission
    ) == PackageManager.PERMISSION_GRANTED

fun Context.showPermissionRequestExplanation(
    permission: String,
    message: String,
    buttonTextPositive: String,

    callback: (() -> Unit)? = null
) {
    AlertDialog.Builder(this).apply {
        setTitle("$permission")
        setMessage(message)
        setPositiveButton(buttonTextPositive) { _, _ -> callback?.invoke() }
        setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
    }.show()
}

fun Context.createImageUri(): Uri {
    val file = File("${this.externalCacheDir}${File.separator}${System.currentTimeMillis()}.jpg")
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //  FileProvider.getUriForFile(this,  "${this.packageName}.provider", file)
        FileProvider.getUriForFile(
            Objects.requireNonNull(this),
            "com.hackathon.activityresultapisampleapp.provider", file
        );
    } else {
        Uri.fromFile(file)
    }
}