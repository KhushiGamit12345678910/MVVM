package com.example.mvvmarchitecture.registration

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.const.isNetworkAvailable
import com.example.mvvmarchitecture.databinding.ActivityRegistrationBinding
import com.example.mvvmarchitecture.dialogbox.DialogBox
import com.example.mvvmarchitecture.login.LoginActivity
import com.example.mvvmarchitecture.util.Status
import com.example.viewmodeldemo.utils.PathUtil

class RegistrationActivity : AppCompatActivity() {

    lateinit var binding:ActivityRegistrationBinding
    lateinit var viewBinding: RegistrationViewModel
    private lateinit var dialog: DialogBox
    private lateinit var processDialog : ProgressDialog
    private lateinit var inputMethodManager: InputMethodManager
    var imagePath : String=""

    companion object{
        private const val IMAGE_CHOOSE = 100;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        viewBinding = ViewModelProvider(this)[RegistrationViewModel::class.java]

        inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        // Initialize
        binding.registrationViewModel = viewBinding
        dialog= DialogBox(this)
        processDialog = ProgressDialog(this)

        viewBinding.dataLiveData.observe(this) {

            when (it.status) {

                Status.SUCCESS -> {
                    processDialog.dismiss()
                    Log.d("data_information", "Status.SUCCESS ${it.data?.message}")
                    if (it.data?.status==true){
                        dialog.commonDialog(resources.getString(R.string.app_name),"register",it.data.message.toString(),1)
                    }else{
                        dialog.commonDialog(resources.getString(R.string.app_name),"register",it.data?.message.toString(),0)
                    }
                }
                Status.LOADING -> {
                    processDialog.setMessage(resources.getString(R.string.loading))
                    processDialog.setCancelable(false)
                    processDialog.show()

                    Log.d("data_information", "Status.LOADING")
                }
                Status.ERROR -> {
                    processDialog.dismiss()
                    Log.d("data_information", "Status.ERROR")
                }
            }
        }

        binding.loginText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signupButton.setOnClickListener {

            viewBinding.nameerror = ""
            viewBinding.emailerror = ""
            viewBinding.passworderror = ""



            if (viewBinding.rgValidation(imagePath)) {

                if (isNetworkAvailable(this@RegistrationActivity)){
                    viewBinding.registerUser(imagePath)


                }else {
                    Toast.makeText(
                        this@RegistrationActivity,
                        resources.getString(R.string.internet_connection_not_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {

                binding.signupFullname.helperText = viewBinding.nameerror
                binding.signupEmail.helperText = viewBinding.emailerror
                binding.signupPassword.helperText = viewBinding.passworderror

            }
        }

        binding.selectedImage.setOnClickListener {
            checkGalleryPermission()
        }
    }
    private fun openGallery() {
        val  intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_CHOOSE)
//        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//        startActivityIfNeeded(gallery, IMAGE_CHOOSE)

    }


    private fun checkGalleryPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions, IMAGE_CHOOSE)
        } else {
            openGallery()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CHOOSE) {
            if (data!= null){
                val uri = data.data
                binding.selectedImage.setImageURI(uri)
                imagePath = PathUtil.getPath(this,uri)!!.toString()
                Glide.with(binding.selectedImage).load(uri).placeholder(R.drawable.placeholder).into(binding.selectedImage)
            }else{
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "no match request code", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == IMAGE_CHOOSE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@RegistrationActivity, "Gallery Permission Granted", Toast.LENGTH_SHORT).show()
                openGallery()
            } else {
                Toast.makeText(this@RegistrationActivity, "Gallery Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}