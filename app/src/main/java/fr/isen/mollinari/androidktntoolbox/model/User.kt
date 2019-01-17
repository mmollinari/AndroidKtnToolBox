package fr.isen.mollinari.androidktntoolbox.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class User {

    @SerializedName("gender")
    @Expose
    lateinit var gender: String
    @SerializedName("name")
    @Expose
    lateinit var name: Name
    @SerializedName("location")
    @Expose
    lateinit var location: Location
    @SerializedName("email")
    @Expose
    lateinit var email: String
    @SerializedName("login")
    @Expose
    lateinit var login: Login
    @SerializedName("dob")
    @Expose
    lateinit var dob: UserDate
    @SerializedName("registered")
    @Expose
    lateinit var registered: UserDate
    @SerializedName("phone")
    @Expose
    lateinit var phone: String
    @SerializedName("cell")
    @Expose
    lateinit var cell: String
    @SerializedName("id")
    @Expose
    lateinit var id: Id
    @SerializedName("picture")
    @Expose
    lateinit var picture: Picture
    @SerializedName("nat")
    @Expose
    lateinit var nat: String

}
