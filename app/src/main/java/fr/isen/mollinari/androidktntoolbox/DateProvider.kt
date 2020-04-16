package fr.isen.mollinari.androidktntoolbox

import java.util.Calendar

class DateProvider {

    fun getAge(day: Int, month: Int, year: Int): Int {
        val dateOfBirth = Calendar.getInstance()
        val today = Calendar.getInstance()

        dateOfBirth.set(year, month, day)

        var age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR)
        if ((today.get(Calendar.DAY_OF_MONTH) < dateOfBirth.get(Calendar.DAY_OF_MONTH) &&
            today.get(Calendar.MONTH) == dateOfBirth.get(Calendar.MONTH)) ||
            (today.get(Calendar.MONTH) < dateOfBirth.get(Calendar.MONTH))
        ) {
            age--
        }

        return age
    }
}