package fr.isen.mollinari.androidktntoolbox.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserDate {

    @SerializedName("date")
    @Expose
    lateinit var date: String
    @SerializedName("age")
    @Expose
    var age: Int = 0
}