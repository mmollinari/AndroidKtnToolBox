package fr.isen.mollinari.androidktntoolbox

import android.app.DatePickerDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_storage.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class StorageActivity : AppCompatActivity() {

    private val JSON_FILE = "data_user_toolbox.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
            dateValue.text = sdf.format(cal.time)
        }

        save.setOnClickListener {
            saveDataToFile(lastName.text.toString(), firstName.text.toString(), dateValue.text.toString())
        }

        show.setOnClickListener {
            showDataFromFile()
        }

        date.setOnClickListener {
            showDatePicker(cal, dateSetListener)
        }

        dateValue.setOnClickListener {
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

    private fun saveDataToFile(name: String, firstName: String, date: String) {
        val fos: FileOutputStream
        val file = getFileStreamPath(JSON_FILE)
        try {
            fos = openFileOutput(JSON_FILE, Context.MODE_PRIVATE)
            Log.i("StorageActivity", "chemein du fichier : ${file.path}")
            val data =
                "{ 'nom': '$name', 'prenom': '$firstName', 'date_naissance': '$date' }"
            fos.write(data.toByteArray())
            fos.close()
            Toast.makeText(this@StorageActivity, "Sauvegarde des informations de l'utilisateur", Toast.LENGTH_LONG).show()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
            e.message
        }

    }

    private fun showDataFromFile() {
        val inputStream: FileInputStream
        try {
            inputStream = openFileInput(JSON_FILE)
            val strData = convertInputStreamToString(inputStream)
            if (strData != "") {
                val jsonData = JSONObject(strData)

                val strDate = jsonData.optString("date_naissance")
                val arrayStr =
                    strDate.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                val nom = jsonData.optString("nom")
                val prenom = jsonData.optString("prenom")
                val dateNaissance = jsonData.optString("date_naissance")

                val age = getAge(
                    Integer.parseInt(arrayStr[0]),
                    Integer.parseInt(arrayStr[1]),
                    Integer.parseInt(arrayStr[2])
                )

                val builder = AlertDialog.Builder(this@StorageActivity)

                builder.setTitle("Lecture du fichier")
                builder.setMessage("Nom : $nom\n Prenom : $prenom\n Date : $dateNaissance\n Age : $age")

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    @Throws(IOException::class)
    fun convertInputStreamToString(inputStream: InputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuffer = StringBuilder("")
        var readLine: String? = bufferedReader.readLine()

        while (readLine != null) {
            stringBuffer.append(readLine)
            stringBuffer.append("\n")
            readLine = bufferedReader.readLine()
        }

        inputStream.close()
        return stringBuffer.toString()

    }

    private fun getAge(day: Int, month: Int, year: Int): String {
        val dateNaissance = Calendar.getInstance()
        val today = Calendar.getInstance()

        dateNaissance.set(year, month, day)

        var age = today.get(Calendar.YEAR) - dateNaissance.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dateNaissance.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age.toString() + " ans"
    }
}
