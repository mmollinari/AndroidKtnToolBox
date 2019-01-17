package fr.isen.mollinari.androidktntoolbox.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import fr.isen.mollinari.androidktntoolbox.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    private val GOOD_IDENTIFIANT = "admin"
    private val GOOD_MDP = "123"

    lateinit var sharedPreferences: SharedPreferences

    companion object {
        val USER_PREFS = "user_prefs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        val savedIdentifiant = sharedPreferences.getString("id", "")
        val savedMdp = sharedPreferences.getString("mdp", "")

        if (savedIdentifiant == GOOD_IDENTIFIANT && savedMdp == GOOD_MDP) {
            goToHome(savedIdentifiant, true)
        }

        loginAction.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin() {
        val ident = login.text.toString()
        val mdp = password.text.toString()

        if (GOOD_IDENTIFIANT == ident && GOOD_MDP == mdp) {
            saveUserCredential(ident, mdp)
            goToHome(ident, false)
        } else {
            Toast.makeText(
                this@LoginActivity,
                "Identifiant ou mot de passe incorrect",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveUserCredential(identifiant: String, mdp: String) {
        val editor = sharedPreferences.edit()
        editor.putString("id", identifiant)
        editor.putString("mdp", mdp)
        editor.apply()
    }

    private fun goToHome(identifiant: String, clearCache: Boolean) {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.putExtra("strIdentifiant", identifiant)
        if (clearCache) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        if (clearCache) finish()
    }
}
