package com.example.mvvmarchitecture.const

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.text.TextUtils
import android.util.Patterns
import com.example.mvvmarchitecture.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun isValidEmail(email: CharSequence): Boolean {
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun imageDialog(context: Context) {
    var dialogBox = MaterialAlertDialogBuilder(context)
    dialogBox.setTitle("Image")
    dialogBox.setMessage("Please select image")
    dialogBox.setPositiveButton(R.string.ok, DialogInterface.OnClickListener { _, _ ->
    })
    dialogBox.setNegativeButton(
        R.string.cancel,
        DialogInterface.OnClickListener { _, _ -> })
    dialogBox.show()

}