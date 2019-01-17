package fr.isen.mollinari.androidktntoolbox.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Picture {

    @SerializedName("large")
    @Expose
    lateinit var large: String
    @SerializedName("medium")
    @Expose
    lateinit var medium: String
    @SerializedName("thumbnail")
    @Expose
    lateinit var thumbnail: String

}