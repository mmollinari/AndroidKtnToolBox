package fr.isen.mollinari.androidktntoolbox

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WebServiceTask(private val callBackInterface: CallBackInterface) :
    AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String): String {

        var result = ""
        try {
            val url = URL(params[0])
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()

            if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                result = convertInputStreamToString(urlConnection.inputStream)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return result

    }

    override fun onPostExecute(result: String?) {
        if (!result.isNullOrEmpty()) {
            callBackInterface.success(result)
        } else {
            callBackInterface.error()
        }
    }

    @Throws(IOException::class)
    private fun convertInputStreamToString(inputStream: InputStream): String {
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

}
