package com.example.mvvmarchitecture.registration

import android.app.Application
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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class RegistrationViewModel(application: Application) : AndroidViewModel(application) {

    var name: String = ""
    var email: String = ""
    var password: String = ""


    var nameerror: String = ""
    var emailerror: String = ""
    var passworderror: String = ""


    var responseCodeCheck: ResponseCodeCheck = ResponseCodeCheck()

    private var dataMutable: MutableLiveData<Resource<DataModel>> = MutableLiveData()
    var dataLiveData: LiveData<Resource<DataModel>> = dataMutable

    fun rgValidation(imagePath: String): Boolean {

        nameerror = ""
        emailerror = ""
        passworderror = ""


        if (name.isEmpty()) {
            nameerror = getApplication<Application>().getString(R.string.pleaseEnterName)
            return false
        }
        if (email.isEmpty()) {
            emailerror = getApplication<Application>().getString(R.string.please_enter_email)
            return false
        } else if (!isValidEmail(email)) {
            emailerror = getApplication<Application>().getString(R.string.please_enter_valid_email)
            return false
        }
        if (password.isEmpty()) {
            passworderror = getApplication<Application>().getString(R.string.pleaseEnterName)
            return false
        }
        if(imagePath.isEmpty()){
            Toast.makeText(getApplication(), "please select image", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    fun registerUser(imagePath: String) {

        val file = File(imagePath)
        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("profile_picture", file.name, requestFile)

        val fullName = RequestBody.create(MediaType.parse("multipart/form-data"), name)
        val fEmail = RequestBody.create(MediaType.parse("multipart/form-data"), email)
        val fPassword = RequestBody.create(MediaType.parse("multipart/form-data"), password)

        val hasMap: HashMap<String, RequestBody> = HashMap()
        hasMap.apply {
            put(nameParams, fullName)
            put(emailParams, fEmail)
            put(passwordParams, fPassword)
        }

        dataMutable.value = Resource.loading(null)

        RetrofitClient.retrofit.userRegister(body, hasMap).enqueue(object : Callback<DataModel> {

            override fun onResponse(call: Call<DataModel>, response: Response<DataModel>) {

                if (response.body() !== null) {
                    try {
                        dataMutable.postValue(
                            responseCodeCheck.getResponseResult(
                                Response.success(
                                    response.body()
                                )
                            )


                        )
                    } catch (e: Exception) {
                        dataMutable.postValue(
                            Resource.error(
                                getApplication<Application>().getString(
                                    R.string.error
                                ), null
                            )
                        )
                    }
                } else {
                    dataMutable.postValue(
                        Resource.error(
                            getApplication<Application>().getString(R.string.error),
                            null
                        )
                    )
                }
            }
            override fun onFailure(call: Call<DataModel>, t: Throwable) {
                dataMutable.postValue(
                    Resource.error(
                        getApplication<Application>().getString(R.string.error),
                        null
                    )
                )
                Toast.makeText(getApplication(), t.message, Toast.LENGTH_SHORT).show()
            }

        })


    }


}
