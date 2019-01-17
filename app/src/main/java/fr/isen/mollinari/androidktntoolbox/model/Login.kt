package fr.isen.mollinari.androidktntoolbox.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Login {

    @SerializedName("username")
    @Expose
    lateinit var username: String
    @SerializedName("password")
    @Expose
    lateinit var password: String
    @SerializedName("salt")
    @Expose
    lateinit var salt: String
    @SerializedName("md5")
    @Expose
    lateinit var md5: String
    @SerializedName("sha1")
    @Expose
    lateinit var sha1: String
    @SerializedName("sha256")
    @Expose
    lateinit var sha256: String

}
