package fr.isen.mollinari.androidktntoolbox.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Name {

    @SerializedName("title")
    @Expose
    lateinit var title: String
    @SerializedName("first")
    @Expose
    lateinit var first: String
    @SerializedName("last")
    @Expose
    lateinit var last: String

}
