package fr.isen.mollinari.androidktntoolbox.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import fr.isen.mollinari.androidktntoolbox.R
import fr.isen.mollinari.androidktntoolbox.fragment.LifeCycleFragment
import kotlinx.android.synthetic.main.activity_life_cycle.message

class LifeCycleActivity : AppCompatActivity() {

    private var isActivityRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle)

        isActivityRunning = true

        if (savedInstanceState != null) {
            val lifeCycleFragment = LifeCycleFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment, lifeCycleFragment)
                .commit()
        } else {
            supportFragmentManager
                .findFragmentById(R.id.fragment)
        }

        showLog("Cycle de vie Activité : onCreate")
    }

    override fun onStart() {
        super.onStart()
        showLog("Cycle de vie Activité : onStart")
    }

    override fun onRestart() {
        super.onRestart()
        showLog("Cycle de vie Activité : onRestart")
    }

    override fun onResume() {
        super.onResume()
        showLog("Cycle de vie Activité : onResume")
    }

    override fun onPause() {
        super.onPause()
        isActivityRunning = false
        showLog("Cycle de vie Activité : onPause")
    }

    override fun onStop() {
        super.onStop()
        showLog("Cycle de vie Activité : onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        showLog("Cycle de vie Activité : onDestroy")
        Toast.makeText(this, "L'activité est détruite", Toast.LENGTH_SHORT).show()
    }


    fun showLog(logMessage: String) {
        if (isActivityRunning) {
            val text = message.text.toString() + logMessage + "\n"
            message.text = text
        } else {
            Log.d("LifeCycleActivity", logMessage)
        }
    }
}
