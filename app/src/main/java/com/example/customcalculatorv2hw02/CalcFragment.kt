package com.example.customcalculatorv2hw02

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.customcalculatorv2hw02.databinding.FragmentCalcBinding
import com.example.customcalculatorv2hw02.engine.StringExpressionAnalyzer

class CalcFragment : Fragment() {

    private var _binding: FragmentCalcBinding? = null
    private val binding get() = requireNotNull(_binding) {
        "View was destroyed"
    }

    val calc = StringExpressionAnalyzer()

    private var isDigit = false
    private var isOperator = false
    private var isDot = false
    private var isLeftBracket = false
    private var isRightBracket = false
    private var bracketCounter = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return FragmentCalcBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            binding.mathExpressionInput.text = "0"

            binding.button0.setOnClickListener {
                if(!appendDigit(binding.button0.text)) {
                    showErrorToast()
                }
            }

            binding.button1.setOnClickListener {
                if(!appendDigit(binding.button1.text)) {
                    showErrorToast()
                }
            }

            binding.button2.setOnClickListener {
                if(!appendDigit(binding.button2.text)) {
                    showErrorToast()
                }
            }

            binding.button3.setOnClickListener {
                if(!appendDigit(binding.button3.text)) {
                    showErrorToast()
                }
            }

            binding.button4.setOnClickListener {
                if(!appendDigit(binding.button4.text)) {
                    showErrorToast()
                }
            }

            binding.button5.setOnClickListener {
                if(!appendDigit(binding.button5.text)) {
                    showErrorToast()
                }
            }

            binding.button6.setOnClickListener {
                if(!appendDigit(binding.button6.text)) {
                    showErrorToast()
                }
            }

            binding.button7.setOnClickListener {
                if(!appendDigit(binding.button7.text)) {
                    showErrorToast()
                }
            }

            binding.button8.setOnClickListener {
                if(!appendDigit(binding.button8.text)) {
                    showErrorToast()
                }
            }

            binding.button9.setOnClickListener {
                if(!appendDigit(binding.button9.text)) {
                    showErrorToast()
                }
            }

            binding.buttonDot.setOnClickListener {
                if(!appendDot(binding.buttonDot.text)) {
                    showErrorToast()
                }
            }

            binding.clearBtn.setOnClickListener {
                clearExpression()
                flagsReset()
            }

            binding.divBtn.setOnClickListener {
                appendOperator(binding.divBtn.text)
            }

            binding.mulBtn.setOnClickListener {
                appendOperator(binding.mulBtn.text)
            }

            binding.subtractBtn.setOnClickListener {
                appendOperator(binding.subtractBtn.text)
            }

            binding.plusBtn.setOnClickListener {
                appendOperator(binding.plusBtn.text)
            }

            binding.leftBr.setOnClickListener {
                appendLeftBracket(binding.leftBr.text)
            }

            binding.rightBr.setOnClickListener {
                appendRightBracket(binding.rightBr.text)
            }

            binding.deleteCharBtn.setOnClickListener {
                deleteCharFromEnd()
            }

            binding.buttonResult.setOnClickListener {
                calculateExpression()
            }
        }
    }

    private fun appendDigit(digit: CharSequence): Boolean {
        if(isRightBracket) {
            binding.mathExpressionInput.append("*$digit")
        } else if(binding.mathExpressionInput.text === "0") {
            clearExpression()
            binding.mathExpressionInput.text = digit
        } else {
            binding.mathExpressionInput.append(digit)
        }

        if(isDot) {
            flagsReset()
            isDigit = true
            isDot = true
        } else {
            flagsReset()
            isDigit = true
        }

        return true
    }

    private fun appendDot(dotChar: CharSequence): Boolean {
        if(isDigit && !isDot) {
            flagsReset()
            isDigit = true
            isDot = true
            binding.mathExpressionInput.append(dotChar)
            return true
        } else if(isOperator) {
            flagsReset()
            isDigit = true
            isDot = true
            binding.mathExpressionInput.append("0$dotChar")
            return true
        } else {
            return false
        }
    }

    private fun appendOperator(operator: CharSequence): Boolean {
        if(isOperator) {
            deleteCharFromEnd()
            binding.mathExpressionInput.append(operator)
            return true
        } else if(isLeftBracket || isDot) {
            flagsReset()
            isOperator = true
            binding.mathExpressionInput.append("0$operator")
            return true
        } else if(binding.mathExpressionInput.text === "0") {
            binding.mathExpressionInput.append(operator)
            flagsReset()
            isOperator = true
            return true
        } else {
            flagsReset()
            isOperator = true
            binding.mathExpressionInput.append(operator)
            return true
        }
    }

    private fun clearExpression() {
        binding.mathExpressionInput.text = "0"
        flagsReset()
        bracketCounter = 0
    }

    private fun deleteCharFromEnd() {
        if(binding.mathExpressionInput.text.length > 0
            && !(binding.mathExpressionInput.text === "0")) {
            binding.mathExpressionInput.text = binding.mathExpressionInput.text
                .substring(0,binding.mathExpressionInput.text.length-1)
            calculateBrackets()
            flagsReset() //TODO reset flags after deleting the char from the end of expression
        } else if(binding.mathExpressionInput.text === "0" || binding.mathExpressionInput.text.isEmpty()) {
            binding.mathExpressionInput.text = "0"
            Toast.makeText(context, "Math expression is empty", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun appendLeftBracket(leftBracket: CharSequence): Boolean {
        if(binding.mathExpressionInput.text === "0") {
            flagsReset()
            isLeftBracket = true
            binding.mathExpressionInput.text = leftBracket
            return true
        }
        flagsReset()
        isLeftBracket = true
        binding.mathExpressionInput.append(leftBracket)
        return true
    }

    private fun appendRightBracket(rightBracket: CharSequence): Boolean {
        if(binding.mathExpressionInput.text === "0") return false
        flagsReset()
        isRightBracket = true
        binding.mathExpressionInput.append(rightBracket)
        return true
    }

    private fun flagsReset() {
        isDigit = false
        isOperator = false
        isDot = false
        isLeftBracket = false
        isRightBracket = false
    }

    private fun showErrorToast() {
        Toast.makeText(
            context,
            "Input error",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun calculateBrackets() {
        val str = binding.mathExpressionInput.text
        for(char in str) {
            if(char == '(') bracketCounter++
            if(char == ')') bracketCounter--
        }
    }

    private fun calculateExpression() {
        calculateBrackets()
        if(bracketCounter > 0) {
            Toast.makeText(
                context,
                "You missed the right bracket",
                Toast.LENGTH_SHORT
            ).show()
        } else if(bracketCounter < 0) {
            Toast.makeText(
                context,
                "You missed the left bracket",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val mathExpression = binding.mathExpressionInput.text.toString()
            val result = calc.calculateString(
                mathExpression
            )

            binding.resultOutput.text = result.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}