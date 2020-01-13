package fr.isen.mollinari.androidktntoolbox.model

import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("large") val large: String,
    @SerializedName("medium") val medium: String,
    @SerializedName("thumbnail") val thumbnail: String
)