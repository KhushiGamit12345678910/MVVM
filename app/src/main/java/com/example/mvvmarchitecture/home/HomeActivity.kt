package com.example.mvvmarchitecture.home

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.const.USER_LOGIN
import com.example.mvvmarchitecture.databinding.ActivityHomeBinding
import com.example.mvvmarchitecture.dialogbox.DialogBox
import com.example.mvvmarchitecture.login.LoginActivity
import com.example.mvvmarchitecture.sharedpref.Sharedpref
import com.example.mvvmarchitecture.util.Status
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    lateinit var viewHomebinding: HomeViewModel
    lateinit var sharedPreferences: Sharedpref
    lateinit var dialog: DialogBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewHomebinding = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.homeViewModel = viewHomebinding
        binding.homeActivity = this
        dialog = DialogBox(this)
        sharedPreferences = Sharedpref(this)

        viewHomebinding.liveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {

                    binding.emailText.text = it.data?.data?.email
                    Glide.with(this).load(it.data!!.data!!.profilePicture)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.image)

                }
                Status.LOADING -> {
                    Log.d("data_information", "Status.LOADING")
                }
                Status.ERROR -> {
                    Log.d("data_information", "Status.ERROR")
                }
            }
        }
        viewHomebinding.userProfile()
    }

    // Logout user
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutMenu -> {
                val dialogBox = MaterialAlertDialogBuilder(this)
                dialogBox.setTitle(R.string.logout)
                dialogBox.setMessage(R.string.are_u_sure)
                dialogBox.setPositiveButton(R.string.ok, DialogInterface.OnClickListener { _, _ ->
                    sharedPreferences.putBoolean(USER_LOGIN, false)
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                })
                dialogBox.setNegativeButton(
                    R.string.cancel,
                    DialogInterface.OnClickListener { _, _ -> })
                dialogBox.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}