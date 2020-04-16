package fr.isen.mollinari.androidktntoolbox.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import fr.isen.mollinari.androidktntoolbox.R
import fr.isen.mollinari.androidktntoolbox.adapter.UsersAdapter
import fr.isen.mollinari.androidktntoolbox.model.UserResults
import kotlinx.android.synthetic.main.activity_web_service.recyclerView

class WebServiceActivity : AppCompatActivity() {

    private val url = "https://randomuser.me/api/?results=10&nat=fr"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_service)

        getUserFromApi()
    }

    private fun getUserFromApi() {
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val userResults = parseUserResultJSON(response)
                userResults?.results?.let {

                    val mAdapter =
                        UsersAdapter(
                            it
                        )
                    val mLayoutManager =
                        LinearLayoutManager(applicationContext)
                    recyclerView.layoutManager = mLayoutManager
                    recyclerView.addItemDecoration(
                        DividerItemDecoration(
                            this@WebServiceActivity,
                            LinearLayoutManager.VERTICAL
                        )
                    )
                    recyclerView.itemAnimator = DefaultItemAnimator()
                    recyclerView.adapter = mAdapter
                    mAdapter.notifyDataSetChanged()

                }
            },
            Response.ErrorListener { Log.e("WebServiceActivity", "Le serveur n'a pas pu récupérer les résultats") })

        Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun parseUserResultJSON(response: String): UserResults? {
        val gson = GsonBuilder().create()
        return gson.fromJson<UserResults>(response, UserResults::class.java)
    }
}