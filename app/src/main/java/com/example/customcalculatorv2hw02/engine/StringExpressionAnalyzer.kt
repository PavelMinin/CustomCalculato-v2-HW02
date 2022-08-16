package com.example.customcalculatorv2hw02.engine

import java.util.*

class StringExpressionAnalyzer {

    val calculator: CustomMathRepository = Calculator()
    val priorityQueue: PriorityQueue<Handlers> = PriorityQueue<Handlers>(5,  HandlerComparator())

    private fun init() {
        priorityQueue.add(
            AddHandler(calculator),
            SubtractHandler(calculator),
            BracketsHandler(),
            MultiplyHandler(calculator),
            DivideHandler(calculator))
    }

    private fun PriorityQueue<Handlers>.add(vararg handlers: Handlers) {
        for(handler in handlers) {
            this.add(handler)
        }
    }

    fun calculateString(_str: String): Double {
        var str = _str

        while (true) {
            init()
            while(!priorityQueue.isEmpty()) {
                val handler = priorityQueue.poll()
                if (handler != null) {
                    str = handler.handle(str)
                }
            }
            try {
                return str.toDouble()
            } catch (e: Exception) { }
        }
    }
}