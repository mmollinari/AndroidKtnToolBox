package fr.isen.mollinari.androidktntoolbox.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Id {

    @SerializedName("name")
    @Expose
    lateinit var name: String
    @SerializedName("value")
    @Expose
    lateinit var value: Any

}
