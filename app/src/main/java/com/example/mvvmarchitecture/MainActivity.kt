package com.example.mvvmarchitecture

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mvvmarchitecture.const.MY_PREF
import com.example.mvvmarchitecture.const.USER_LOGIN
import com.example.mvvmarchitecture.databinding.ActivityLoginBinding
import com.example.mvvmarchitecture.databinding.ActivityMainBinding
import com.example.mvvmarchitecture.home.HomeActivity
import com.example.mvvmarchitecture.login.LoginActivity
import com.example.mvvmarchitecture.registration.RegistrationActivity
import com.example.mvvmarchitecture.sharedpref.Sharedpref

class MainActivity : AppCompatActivity() {

    lateinit var databinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences(MY_PREF, MODE_PRIVATE)
        if (sharedPreferences.getBoolean(USER_LOGIN,false)){
            startActivity(Intent(this,HomeActivity::class.java))
            finishAffinity()
        }else{
            startActivity(Intent(this,LoginActivity::class.java))
            finishAffinity()
        }
        databinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        databinding.getStarted.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}