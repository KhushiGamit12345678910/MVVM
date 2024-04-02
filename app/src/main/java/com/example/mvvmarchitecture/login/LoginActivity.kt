package com.example.mvvmarchitecture.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.const.ID
import com.example.mvvmarchitecture.const.USER_LOGIN
import com.example.mvvmarchitecture.const.isNetworkAvailable
import com.example.mvvmarchitecture.databinding.ActivityLoginBinding
import com.example.mvvmarchitecture.dialogbox.DialogBox
import com.example.mvvmarchitecture.registration.RegistrationActivity
import com.example.mvvmarchitecture.sharedpref.Sharedpref
import com.example.mvvmarchitecture.util.Status

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var viewLoginBinding: LoginViewModel
    lateinit var sharedPreferences: Sharedpref
    private lateinit var dialog: DialogBox
    private lateinit var processDialog : ProgressDialog
    private lateinit var inputMethodManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewLoginBinding = ViewModelProvider(this)[LoginViewModel::class.java]

        inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        // initialization
        binding.loginViewModel = viewLoginBinding
        binding.loginActivity = this
        sharedPreferences = Sharedpref(this)
        dialog = DialogBox(this)
        processDialog = ProgressDialog(this)

        viewLoginBinding.liveData.observe(this) {

            when (it.status) {
                Status.SUCCESS -> {
                    val isValue: Int
                    if (it.data?.status == true) {
                        isValue = 1
                        sharedPreferences.putString(ID, it.data.data?.id.toString())
                        sharedPreferences.putBoolean(USER_LOGIN, true)
                    } else {
                        isValue = 0
                    }
                    dialog.commonDialog(
                        resources.getString(R.string.app_name),
                        "login",
                        it.data?.message.toString(),
                        isValue
                    )
                    Toast.makeText(this@LoginActivity, it.data?.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    processDialog.setMessage(resources.getString(R.string.loading))
                    processDialog.setCancelable(false)
                    processDialog.show()
                    Log.d("data_information", "Status.LOADING")
                }
                Status.ERROR -> {
                    Log.d("data_information", "Status.ERROR")
                }
            }
        }

        binding.loginButton.setOnClickListener {

            viewLoginBinding.emailError = ""
            viewLoginBinding.passwordError = ""

            if (viewLoginBinding.loginValidation()) {

                if (isNetworkAvailable(this@LoginActivity)){
                    viewLoginBinding.loginUser()

                }else {
                    Toast.makeText(
                        this@LoginActivity,
                        resources.getString(R.string.internet_connection_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                binding.loginEmail.helperText = viewLoginBinding.emailError
                binding.loginPassword.helperText = viewLoginBinding.passwordError
            }
        }
        binding.registerText.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}


