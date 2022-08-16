package com.example.customcalculatorv2hw02.engine

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * В данном файле реализованы классы и функции для разбора строки на операнды с использованием
 * регулярных выражений. Данно решение позволяет без особых сложностей расширять функционал
 * калькулятора, добавляя новый хэндлер в данном файле и добавляя его в PriorityQueue в файле
 * StringExpressionAnalyzer
 */

private const val NUMBER_PATTERN: String = "(-?\\d+(\\.\\d+){0,1})"

private const val ADD_PATTERN: String = " *\\+ *"

private const val OP_BR_PATTERN: String = "\\("

private const val CL_BR_PATTERN: String = "\\)"

private const val DIVIDE_PATTERN: String = " *\\/ *"

private const val MULT_PATTERN: String = " *\\* *"

private const val SUBTRACT_PATTERN: String = " *\\- *"

interface Handlers {
    val priority: Int
    fun handle (_str: String) : String
}

/**
 * Абстрактный класс. Реализован цикл для выполнения математический операций с операндамиЮ пока в
 * выражении присутствуют части, удовлетворяющие паттерну. Сама функция расчета абстрактная и
 * зависит от выполняемой операции. Это реализуется в хэндлерах.
 */
abstract class OperatorHandlers(_regexp: String): Handlers {

    val regexp = _regexp

    override fun handle (_str: String) : String {
        var str = _str
        val pattern: Pattern = Pattern.compile(regexp)
        do{
            val matcher: Matcher = pattern.matcher(str)
            val isMatch = matcher.find()
            if(isMatch) {
                val result = calculate(matcher)
                do {
                    str = str.replace(matcher.group(), result.toString())
                } while (str.contains(matcher.group()))
            }
        } while(isMatch)
        return str
    }

    abstract fun calculate(matcher: Matcher): Double
}

class AddHandler(_calculator: CustomMathRepository) : Handlers,
    OperatorHandlers("$NUMBER_PATTERN$ADD_PATTERN$NUMBER_PATTERN") {

    private val calculator = _calculator

    override val priority: Int
        get() {
            return 1
        }

    override fun calculate(matcher: Matcher): Double {
        val operand1 = matcher.group(1)
        val operand2 = matcher.group(3)
        if (operand1 != null && operand2 != null) {
            return calculator.add(operand1.toDouble(), operand2.toDouble())
        } else {
            return Double.NaN
        }
    }
}

class BracketsHandler() : Handlers {

    private val calc = StringExpressionAnalyzer()

    override val priority: Int
        get() {
            return 99
        }

    override fun handle(_str: String): String {
        val pattern: Pattern = Pattern.compile(
            "(.*)($OP_BR_PATTERN[^()]+$CL_BR_PATTERN)(.*)"
        )
        var str = _str
        do {
            val matcher: Matcher = pattern.matcher(str)
            val isMatch = matcher.find()
            if (isMatch) {
                val withoutBrackets = matcher.group(2)
                    .substring(1, matcher.group(2).length - 1)
                val calculated = calc.calculateString(withoutBrackets)
                str = str.replace(matcher.group(2),calculated.toString())
            }
        } while (isMatch)
        return str
    }
}

class DivideHandler(_calculator: CustomMathRepository) : Handlers,
    OperatorHandlers("$NUMBER_PATTERN$DIVIDE_PATTERN$NUMBER_PATTERN") {

    private val calculator = _calculator

    override val priority: Int
        get() {
            return 4
        }

    override fun calculate(matcher: Matcher): Double {
        val operand1 = matcher.group(1)
        val operand2 = matcher.group(3)
        if (operand1 != null && operand2 != null) {
            return calculator.divide(operand1.toDouble(), operand2.toDouble())
        } else {
            return Double.NaN
        }
    }
}

class MultiplyHandler(_calculator: CustomMathRepository) : Handlers,
    OperatorHandlers("$NUMBER_PATTERN$MULT_PATTERN$NUMBER_PATTERN") {

    private val calculator = _calculator

    override val priority: Int
        get() {
            return 3
        }

    override fun calculate(matcher: Matcher): Double {
        val operand1 = matcher.group(1)
        val operand2 = matcher.group(3)
        if (operand1 != null && operand2 != null) {
            return calculator.multiply(operand1.toDouble(), operand2.toDouble())
        } else {
            return Double.NaN
        }
    }
}

class SubtractHandler(_calculator: CustomMathRepository) : Handlers,
    OperatorHandlers ("$NUMBER_PATTERN$SUBTRACT_PATTERN$NUMBER_PATTERN") {

    private val calculator = _calculator

    override val priority: Int
        get() {
            return 2
        }

    override fun calculate(matcher: Matcher): Double {
        val operand1 = matcher.group(1)
        val operand2 = matcher.group(3)
        if (operand1 != null && operand2 != null) {
            return calculator.subtract(operand1.toDouble(), operand2.toDouble())
        } else {
            return Double.NaN
        }
    }
}

/**
 * Компаратор сравнивает хэндлеры по приоритету для PriorityQueue.
 */
class HandlerComparator: Comparator<Handlers> {
    override fun compare(p0: Handlers?, p1: Handlers?): Int {
        if (p0 == null && p1 == null) {
            return 0
        } else if (p0 == null) {
            return -1
        } else if (p1 == null) {
            return 1
        }
        return p1.priority - p0.priority
    }
}