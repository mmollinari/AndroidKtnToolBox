package fr.isen.mollinari.androidktntoolbox.model

import com.google.gson.annotations.SerializedName

data class Registered(
    @SerializedName("date") val date: String,
    @SerializedName("age") val age: Int
)