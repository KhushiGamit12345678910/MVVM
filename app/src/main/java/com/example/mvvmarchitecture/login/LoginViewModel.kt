package com.example.mvvmarchitecture.login

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.apifiles.RetrofitClient
import com.example.mvvmarchitecture.const.*
import com.example.mvvmarchitecture.models.DataModel
import com.example.mvvmarchitecture.util.Resource
import com.example.mvvmarchitecture.util.ResponseCodeCheck
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var email: String = ""
    var password: String = ""

    var emailError: String = ""
    var passwordError: String = ""


    var responseCodeCheck: ResponseCodeCheck = ResponseCodeCheck()
    private var dataMutable: MutableLiveData<Resource<DataModel>> = MutableLiveData()
    var liveData: LiveData<Resource<DataModel>> = dataMutable

    fun loginValidation(): Boolean {

        emailError = ""
        passwordError = ""

        if (email.isEmpty()) {
            emailError = getApplication<Application>().getString(R.string.please_enter_email)
            return false
        } else if (!isValidEmail(email)) {
            emailError = getApplication<Application>().getString(R.string.please_enter_valid_email)
            return false
        }
        if (password.isEmpty()) {
            passwordError = getApplication<Application>().getString(R.string.please_enter_password)
            return false
        }
        return true
    }

    fun loginUser() {
        val hashMap: HashMap<String, String> = HashMap()
        hashMap.apply {
            put(emailParams, email)
            put(passwordParams, password)
        }

        dataMutable.value = Resource.loading(null)

        RetrofitClient.retrofit.userLogin(hashMap).enqueue(object : Callback<DataModel> {
            override fun onResponse(call: Call<DataModel>, response: Response<DataModel>) {
                if (response.body() !==null){
                    try {
                        dataMutable.postValue(
                            responseCodeCheck.getResponseResult(
                                Response.success(
                                    response.body()
                                )
                            )
                        )
                    } catch (e: Exception) {
                        dataMutable.postValue(Resource.error(getApplication<Application>().getString(R.string.error), null))
                    }
                }else{
                    dataMutable.postValue(Resource.error(getApplication<Application>().getString(R.string.error), null))
                }
            }
            override fun onFailure(call: Call<DataModel>, t: Throwable) {
                dataMutable.postValue(Resource.error(getApplication<Application>().getString(R.string.error), null))
            }
        })

    }


}