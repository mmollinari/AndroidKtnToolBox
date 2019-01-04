package fr.isen.mollinari.androidktntoolbox

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        lifeCycle.setOnClickListener{
            val intent = Intent(this@HomeActivity, LifeCycleActivity::class.java)
            startActivity(intent)
        }

        data.setOnClickListener {
            val intent = Intent(this@HomeActivity, StorageActivity::class.java)
            startActivity(intent)
        }

        ibDeconnexion.setOnClickListener {
            logOut()
        }
    }

    private fun logOut() {
        val sharedPreferences = getSharedPreferences(LoginActivity.USER_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("id", "")
        editor.putString("mdp", "")
        editor.apply()

        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
