package fr.isen.mollinari.androidktntoolbox.model

import com.google.gson.annotations.SerializedName

data class Timezone(
    @SerializedName("offset") val offset: String,
    @SerializedName("description") val description: String
)