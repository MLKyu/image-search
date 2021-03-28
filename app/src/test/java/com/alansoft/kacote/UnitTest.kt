package com.alansoft.kacote

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

inline fun <reified T> mock(): T = Mockito.mock(T::class.java)