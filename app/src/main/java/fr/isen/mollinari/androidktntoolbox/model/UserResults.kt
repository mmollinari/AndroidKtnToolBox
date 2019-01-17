package fr.isen.mollinari.androidktntoolbox.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * http://www.jsonschema2pojo.org/
 */

class UserResults {
    @SerializedName("results")
    @Expose
    val results: List<User>? = null
}
