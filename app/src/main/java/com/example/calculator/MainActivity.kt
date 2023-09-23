package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var numberField: TextView
    private var accumulatedValue: Double = 0.0
    private var currentOperation: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberField = findViewById(R.id.numberField)
        initCalculatorButtons()
    }

    private fun initCalculatorButtons() {
        val numberButtons = listOf(R.id.one, R.id.two, R.id.three, R.id.four, R.id.five,
            R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero, R.id.dot)

        for (id in numberButtons) {
            findViewById<Button>(id).setOnClickListener {
                appendToInput(it)
            }
        }

        val operationButtons = mapOf(
            R.id.plus to "+",
            R.id.minus to "-",
            R.id.times to "*",
            R.id.fraction to "/",
        )

        for ((id, operation) in operationButtons) {
            findViewById<Button>(id).setOnClickListener {
                handleOperation(operation)
            }
        }

        findViewById<Button>(R.id.equal).setOnClickListener {
            calculateAndDisplayResult()
        }

        findViewById<Button>(R.id.clean).setOnClickListener {
            numberField.text = "0"
            accumulatedValue = 0.0
            currentOperation = null
        }
    }

    private fun appendToInput(view: View) {
        val inputValue = (view as Button).text.toString()
        if (numberField.text == formatResult(accumulatedValue)) {
            numberField.text = ""
        }
        numberField.text = "${numberField.text}$inputValue"
    }

    private fun handleOperation(operation: String) {
        if (currentOperation != null) {
            calculateIntermediateResult()
        } else {
            accumulatedValue = numberField.text.toString().toDoubleOrNull() ?: 0.0
        }

        displayResult(accumulatedValue)
        currentOperation = operation
    }

    private fun calculateIntermediateResult() {
        val inputValue = numberField.text.toString().toDoubleOrNull() ?: return

        when (currentOperation) {
            "+" -> accumulatedValue += inputValue
            "-" -> accumulatedValue -= inputValue
            "*" -> accumulatedValue *= inputValue
            "/" -> if (inputValue != 0.0) {
                accumulatedValue /= inputValue
            } else {
                numberField.text = "Error"
                accumulatedValue = 0.0
                currentOperation = null
                return
            }
        }
    }

    private fun calculateAndDisplayResult() {
        calculateIntermediateResult()
        displayResult(accumulatedValue)
        currentOperation = null
    }

    private fun displayResult(value: Double) {
        numberField.text = formatResult(value)
    }

    private fun formatResult(value: Double): String {
        return if (value % 1 == 0.0) {
            value.toInt().toString()
        } else {
            value.toString()
        }
    }
}
