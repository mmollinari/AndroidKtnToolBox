package fr.isen.mollinari.androidktntoolbox.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Location {

    @SerializedName("street")
    @Expose
    lateinit var street: String
    @SerializedName("city")
    @Expose
    lateinit var city: String
    @SerializedName("state")
    @Expose
    lateinit var state: String

}