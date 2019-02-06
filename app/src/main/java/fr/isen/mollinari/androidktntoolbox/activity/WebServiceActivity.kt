package fr.isen.mollinari.androidktntoolbox.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.gson.GsonBuilder
import fr.isen.mollinari.androidktntoolbox.CallBackInterface
import fr.isen.mollinari.androidktntoolbox.R
import fr.isen.mollinari.androidktntoolbox.WebServiceTask
import fr.isen.mollinari.androidktntoolbox.model.UserResults
import kotlinx.android.synthetic.main.activity_web_service.recyclerView

class WebServiceActivity : AppCompatActivity() {

    val url = "https://randomuser.me/api/?results=10"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_service)

        getUserFromApi()
    }

    private fun getUserFromApi() {
        WebServiceTask(object : CallBackInterface {

            override fun success(json: String) {
                val userResults = parseUserResultJSON(json)
                userResults?.results?.let {

                    val mAdapter = UsersAdapter(it, this@WebServiceActivity)
                    val mLayoutManager = LinearLayoutManager(applicationContext)
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
            }

            override fun error() {
                Log.e("WebServiceActivity", "Le serveur n'a pas pu récupérer les résultats")
            }
        }).execute(url)
    }

    private fun parseUserResultJSON(response: String): UserResults? {
        val gson = GsonBuilder().create()
        return gson.fromJson<UserResults>(response, UserResults::class.java)
    }
}