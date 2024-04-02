package com.example.mvvmarchitecture.dialogbox

import android.app.Activity
import android.content.Intent
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.home.HomeActivity
import com.example.mvvmarchitecture.login.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogBox(private val activity: Activity) {

    fun commonDialog(title: String,type:String, message: String, isCheck: Int) {
        val materialDialog = MaterialAlertDialogBuilder(activity)
        materialDialog.setTitle(title)
        materialDialog.setMessage(message)
        materialDialog.setPositiveButton(activity.resources.getString(R.string.ok)) { _, _ ->
            if (type == "login") {
                if (isCheck == 1) {
                    activity.startActivity(Intent(activity, HomeActivity::class.java))
                    activity.finishAffinity()
                }
            } else if (type == "register") {
                if (isCheck == 1) {
                    activity.startActivity(Intent(activity, LoginActivity::class.java))
                    activity.finishAffinity()
                }
            }
        }
        materialDialog.show()

    }
}