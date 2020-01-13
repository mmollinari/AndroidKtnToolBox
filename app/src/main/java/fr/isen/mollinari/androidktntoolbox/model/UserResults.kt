package fr.isen.mollinari.androidktntoolbox.model

import com.google.gson.annotations.SerializedName

/**
 * https://www.json2kotlin.com
 */

class UserResults(
    @SerializedName("results") val results: List<User>,
    @SerializedName("info") val info: Info
)
