package com.example.mvvmarchitecture.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmarchitecture.apifiles.RetrofitClient
import com.example.mvvmarchitecture.const.ID
import com.example.mvvmarchitecture.const.userIDParams
import com.example.mvvmarchitecture.models.DataModel
import com.example.mvvmarchitecture.sharedpref.Sharedpref
import com.example.mvvmarchitecture.util.Resource
import com.example.mvvmarchitecture.util.ResponseCodeCheck
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var responseCodeCheck: ResponseCodeCheck = ResponseCodeCheck()
    private var mutableData: MutableLiveData<Resource<DataModel>> = MutableLiveData()
    var liveData: LiveData<Resource<DataModel>> = mutableData

    lateinit var sharedPreferences: Sharedpref
    lateinit var id: String

    fun userProfile() {

        sharedPreferences = Sharedpref(getApplication())
        id = sharedPreferences.getString(ID, "")!!

        val hashMap: HashMap<String, String> = HashMap()
        hashMap.apply {
            put(userIDParams,id)
        }

        RetrofitClient.retrofit.userProfile(hashMap).enqueue(object : Callback<DataModel> {
            override fun onResponse(call: Call<DataModel>, response: Response<DataModel>) {

                if (response.body() !== null) {
                    try {
                        mutableData.postValue(
                            responseCodeCheck.getResponseResult(
                                Response.success(
                                    response.body()
                                )
                            )
                        )
                    } catch (e: Exception) {
                        mutableData.postValue(Resource.error("enter Details", null))
                    }
                }else{
                    mutableData.postValue(Resource.error("enter Details", null))
                }
            }

            override fun onFailure(call: Call<DataModel>, t: Throwable) {
                mutableData.postValue(Resource.error(t.message.toString(), null))
                Toast.makeText(getApplication(), "something went wrong", Toast.LENGTH_SHORT).show()
            }

        })
    }


}