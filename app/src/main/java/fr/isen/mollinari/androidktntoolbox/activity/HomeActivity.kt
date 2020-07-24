package fr.isen.mollinari.androidktntoolbox.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.mollinari.androidktntoolbox.R
import fr.isen.mollinari.androidktntoolbox.ble.BLEScanActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        lifeCycleTitle.setOnClickListener{
            goToActivity(LifeCycleActivity::class.java)
        }

        saveTitle.setOnClickListener {
            goToActivity(StorageActivity::class.java)
        }

        permissionTitle.setOnClickListener {
            goToActivity(PermissionActivity::class.java)
        }

        webserviceTitle.setOnClickListener {
            goToActivity(WebServiceActivity::class.java)
        }

        bleTitle.setOnClickListener {
            goToActivity(BLEScanActivity::class.java)
        }

        logout.setOnClickListener {
            logOut()
        }
    }

    private fun goToActivity(destClass: Class<*>) {
        val intent = Intent(this@HomeActivity, destClass)
        startActivity(intent)
    }

    private fun logOut() {
        val sharedPreferences = getSharedPreferences(LoginActivity.USER_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
