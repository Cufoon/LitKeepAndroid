package cufoon.litkeep.android.util


object Calculator {
    fun exec(cs: String): Double? {
        try {
            val infixExpressionList = InfixToSuffix.infixStringToList(cs)
            val suffixExpressionList = InfixToSuffix.infixListToPostfixList(infixExpressionList)
            val assistStack = ArrayDeque<String>()
            suffixExpressionList?.forEach {
                if (it.matches("-?\\d+(\\.\\d+)?".toRegex())) {
                    assistStack.addLast(it)
                } else {
                    val num2 = assistStack.removeLast().toDouble()
                    val num1 = assistStack.removeLast().toDouble()
                    val res: Double = when (it) {
                        "+" -> num1 + num2
                        "-" -> num1 - num2
                        "*" -> num1 * num2
                        "/" -> num1 / num2
                        else -> throw RuntimeException("运算符有误")
                    }
                    assistStack.addLast(res.toString())
                }
            }
            return assistStack.removeLastOrNull()?.toDouble()
        } catch (e: Exception) {
            return null
        }
    }
}

object InfixToSuffix {

    private fun getPriority(ch: String?): Int {
        when (ch) {
            "+", "-" -> return 1
            "*", "/" -> return 2
            "^" -> return 3
        }
        return -1
    }

    fun infixStringToList(ife: String): MutableList<String> {
        val res = mutableListOf<String>()
        var numberStr = ""
        var lastChar: Char? = null
        ife.forEach {
            if (it.code in 48..57 || it.code == 46) {
                numberStr += it
            } else {
                if (it == '-' && (lastChar == null || lastChar!! in "+-*/(")) {
                    numberStr = "-$numberStr"
                } else {
                    if (numberStr.isNotEmpty()) {
                        res.add(numberStr)
                        numberStr = ""
                    }
                    res.add(it.toString())
                }
            }
            lastChar = it
        }
        if (numberStr.isNotEmpty()) {
            res.add(numberStr)
        }
        return res
    }

    fun infixListToPostfixList(exp: MutableList<String>): MutableList<String>? {
        val res = mutableListOf<String>()
        val stack = ArrayDeque<String>()
        val regNumber = "-?\\d+(\\.\\d+)?".toRegex()
        exp.forEach { element ->
            // If the scanned character is an
            // operand, add it to output.
            if (element.matches(regNumber)) {
                res.add(element)
            } else if (element == "(") {
                stack.addLast(element)
            } else if (element == ")") {
                while (!stack.isEmpty() && stack.lastOrNull() != "(") {
                    stack.lastOrNull()?.let {
                        res.add(it)
                    }
                    stack.removeLastOrNull()
                }
                stack.removeLastOrNull()
            } else {
                while (!stack.isEmpty() && getPriority(element) <= getPriority(stack.lastOrNull())) {
                    stack.lastOrNull()?.let {
                        res.add(it)
                    }
                    stack.removeLastOrNull()
                }
                stack.addLast(element)
            }
        }

        // pop all the operators from the stack
        while (!stack.isEmpty()) {
            if (stack.lastOrNull() == "(") return null
            stack.lastOrNull()?.let {
                res.add(it)
            }
            stack.removeLastOrNull()
        }
        return res
    }
}
