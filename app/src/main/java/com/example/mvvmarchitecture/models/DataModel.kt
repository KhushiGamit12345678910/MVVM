package com.example.mvvmarchitecture.models

import com.google.gson.annotations.SerializedName

data class DataModel(
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: Data? = null

) {
    class Data(
        @SerializedName("id") var id: String? = null,
        @SerializedName("name") var name: String? = null,
        @SerializedName("email") var email: String? = null,
        @SerializedName("profile_picture") var profilePicture: String? = null
    )
}