package fr.isen.mollinari.androidktntoolbox

import org.junit.Test

import org.junit.Assert.*

class DateProviderTest {

    @Test
    fun should20AgeSuccess() {
        // init
        val dateProvider = DateProvider()

        // run
        val actualAge = dateProvider.getAge(27, 0, 2000)
        val expectedAge = 20

        //test
        assertEquals(expectedAge, actualAge)
    }

    @Test
    fun should30AgeSuccess() {
        // init
        val dateProvider = DateProvider()

        // run
        val actualAge = dateProvider.getAge(1, 0, 1990)
        val expectedAge = 30

        //test
        assertEquals(expectedAge, actualAge)
    }

    @Test
    fun shouldAgeSuccess() {
        // init
        val dateProvider = DateProvider()

        // run
        val actualAge = dateProvider.getAge(1, 2, 1990)
        val expectedAge = 29

        //test
        assertEquals(expectedAge, actualAge)
    }
}