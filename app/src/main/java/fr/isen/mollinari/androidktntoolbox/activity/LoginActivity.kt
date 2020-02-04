package fr.isen.mollinari.androidktntoolbox.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import fr.isen.mollinari.androidktntoolbox.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val GOOD_IDENTIFIANT = "admin"
        private const val GOOD_MDP = "123"
        private val USER_ID_KEY = "id"
        private val USER_PASS_KEY = "mdp"
        const val USER_PREFS = "user_prefs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        val savedIdentifiant = sharedPreferences.getString(USER_ID_KEY, "")
        val savedMdp = sharedPreferences.getString(USER_PASS_KEY, "")

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
            goToHome(ident, true)
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
        editor.putString(USER_ID_KEY, identifiant)
        editor.putString(USER_PASS_KEY, mdp)
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
