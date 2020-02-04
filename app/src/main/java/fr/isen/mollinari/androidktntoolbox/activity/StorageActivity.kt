package fr.isen.mollinari.androidktntoolbox.activity

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.widget.Toast
import fr.isen.mollinari.androidktntoolbox.R
import kotlinx.android.synthetic.main.activity_storage.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class StorageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        val cal: Calendar = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
                date.text = sdf.format(cal.time)
            }

        save.setOnClickListener {
            saveDataToFile(
                lastName.text.toString(),
                firstName.text.toString(),
                date.text.toString()
            )
        }

        show.setOnClickListener {
            showDataFromFile()
        }

        dateTitle.setOnClickListener {
            showDatePicker(cal, dateSetListener)
        }

        date.setOnClickListener {
            showDatePicker(cal, dateSetListener)
        }
    }

    private fun showDatePicker(cal: Calendar, dateSetListener: DatePickerDialog.OnDateSetListener) {
        DatePickerDialog(
            this@StorageActivity, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun saveDataToFile(lastName: String, firstName: String, date: String) {
        if (firstName.isNotEmpty() && lastName.isNotEmpty() && date != getString(R.string.storage_date_value)) {
            val data =
                "{'$LAST_NAME_KEY': '$lastName', '$FIRST_NAME_KEY': '$firstName', '$DATE_KEY': '$date' }"

            File(cacheDir.absolutePath + JSON_FILE).writeText(data)
            Toast.makeText(
                this@StorageActivity,
                "Sauvegarde des informations de l'utilisateur",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun showDataFromFile() {
        val dataJson = File(cacheDir.absolutePath + JSON_FILE).readText()

        if (dataJson.isNotEmpty()) {
            val jsonObject = JSONObject(dataJson)

            val strDate = jsonObject.optString(DATE_KEY)
            val strLastName = jsonObject.optString(LAST_NAME_KEY)
            val strFirstName = jsonObject.optString(FIRST_NAME_KEY)

            val arrayStr =
                strDate.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val age = getAge(
                Integer.parseInt(arrayStr[0]),
                Integer.parseInt(arrayStr[1]),
                Integer.parseInt(arrayStr[2])
            )

            AlertDialog.Builder(this@StorageActivity)
                .setTitle("Lecture du fichier")
                .setMessage("Nom: $strLastName \n Prenom : $strFirstName\n Date : $strDate\n Age: $age")
                .create()
                .show()
        } else {
            Toast.makeText(
                this@StorageActivity,
                "Aucune information fournie",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun getAge(day: Int, month: Int, year: Int): Int {
        val dateOfBirth = Calendar.getInstance()
        val today = Calendar.getInstance()

        dateOfBirth.set(year, month, day)

        var age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR)
        if(today.get(Calendar.DAY_OF_YEAR) < dateOfBirth.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }

    companion object {
        private const val JSON_FILE = "data_user_toolbox.json"
        private const val LAST_NAME_KEY = "lastName"
        private const val FIRST_NAME_KEY = "firstName"
        private const val DATE_KEY = "date"
    }

}
