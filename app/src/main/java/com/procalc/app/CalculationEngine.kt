package com.procalc.app

import java.lang.RuntimeException
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A robust Recursive Descent Parser for safe mathematical evaluation.
 * Does not require external libraries.
 * Supports: +, -, *, /, ^, %, sqrt, ( )
 */
object CalculationEngine {

    fun evaluate(expression: String): Double {
        val cleanExpr = expression.replace("×", "*")
            .replace("÷", "/")
            .replace(" ", "")
        
        return object : Any() {
            var pos = -1
            var ch = 0

            fun nextChar() {
                ch = if (++pos < cleanExpr.length) cleanExpr[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < cleanExpr.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            // Grammar:
            // expression = term | expression + term | expression - term
            // term = factor | term * factor | term / factor | term % factor
            // factor = +factor | -factor | ( expression ) | number | number ^ factor | sqrt(expression)

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.code)) x += parseTerm() // addition
                    else if (eat('-'.code)) x -= parseTerm() // subtraction
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.code)) x *= parseFactor() // multiplication
                    else if (eat('/'.code)) x /= parseFactor() // division
                    else if (eat('%'.code)) x %= parseFactor() // modulo
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor() // unary plus
                if (eat('-'.code)) return -parseFactor() // unary minus

                var x: Double
                val startPos = pos
                if (eat('('.code)) { // parentheses
                    x = parseExpression()
                    eat(')'.code)
                } else if ((ch in '0'.code..'9'.code) || ch == '.'.code) { // numbers
                    while ((ch in '0'.code..'9'.code) || ch == '.'.code) nextChar()
                    x = cleanExpr.substring(startPos, pos).toDouble()
                } else if (eat('s'.code) && eat('q'.code) && eat('r'.code) && eat('t'.code)) { // sqrt function
                    if (eat('('.code)) {
                        x = sqrt(parseExpression())
                        eat(')'.code)
                    } else {
                        x = sqrt(parseFactor())
                    }
                } else {
                    // Fallback for sqrt symbol if used directly
                    if (eat('√'.code)) {
                        x = sqrt(parseFactor())
                    } else {
                        throw RuntimeException("Unknown char: " + ch.toChar())
                    }
                }

                if (eat('^'.code)) x = x.pow(parseFactor()) // exponentiation

                return x
            }
        }.parse()
    }
}