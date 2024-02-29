package cufoon.litkeep.android

import cufoon.litkeep.android.util.Calculator
import cufoon.litkeep.android.util.InfixToSuffix
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testCalculator() {
        val s = "1+2+6/(2+1)+6.12*5"
        println(s)
        val a = InfixToSuffix.infixStringToList(s)
        println(a)
        val b = InfixToSuffix.infixListToPostfixList(a)
        println(b)
        val xxx = Calculator.exec(s)
        println(xxx)
    }

    @Test
    fun testCalculator1() {
        val s = "-1"
        println(s)
        val a = InfixToSuffix.infixStringToList(s)
        println(a)
        val b = InfixToSuffix.infixListToPostfixList(a)
        println(b)
        val xxx = Calculator.exec(s)
        println(xxx)
    }
}
