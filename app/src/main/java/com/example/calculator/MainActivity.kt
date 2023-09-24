package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var numberField: EditText
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
            numberField.setText("0")
            resetCalculatorState()
        }

        findViewById<Button>(R.id.sqrt).setOnClickListener {
            calculateSqrt()
        }
    }
    private fun appendToInput(view: View) {
        val inputValue = (view as Button).text.toString()
        if (isErrorDisplayed() || numberField.text.toString() == formatResult(accumulatedValue)) {
            numberField.setText("")
        }
        numberField.append(inputValue)
    }

    private fun handleOperation(operation: String) {
        if (isErrorDisplayed()) return

        if (currentOperation != null) {
            calculateIntermediateResult()
        } else {
            accumulatedValue = numberField.text.toString().toDoubleOrNull() ?: 0.0
        }
        displayResult(accumulatedValue)
        currentOperation = operation
    }

    private fun calculateIntermediateResult() {
        if (isErrorDisplayed()) return

        val inputValue = numberField.text.toString().toDoubleOrNull() ?: return

        when (currentOperation) {
            "+" -> accumulatedValue += inputValue
            "-" -> accumulatedValue -= inputValue
            "*" -> accumulatedValue *= inputValue
            "/" -> {
                if (inputValue == 0.0) {
                    displayError()
                    return
                } else {
                    accumulatedValue /= inputValue
                }
            }
        }
    }

    private fun calculateAndDisplayResult() {
        println("Calculate and Display")
        calculateIntermediateResult()
        if (!isErrorDisplayed()) {
            displayResult(accumulatedValue)
        }
        currentOperation = null
    }

    private fun calculateSqrt() {
        println("SQRT")
        val currentValue = numberField.text.toString().toDoubleOrNull()
        if (currentValue != null && currentValue >= 0) {
            accumulatedValue = sqrt(currentValue)
            displayResult(accumulatedValue)
            currentOperation = null
        } else {
            numberField.setText("Error")
        }
    }

    private fun displayResult(value: Double) {
        if (!isErrorDisplayed()) {
            numberField.setText(formatResult(value))
        } else {
            resetCalculatorState()
        }
    }

    private fun displayError() {
        numberField.setText("Error")
        resetCalculatorState()
    }

    private fun resetCalculatorState() {
        accumulatedValue = 0.0
        currentOperation = null
    }

    private fun isErrorDisplayed(): Boolean {
        return numberField.text.toString() == "Error"
    }

    private fun formatResult(value: Double): String {
        return if (value % 1 == 0.0) {
            value.toInt().toString()
        } else {
            value.toString()
        }
    }
}
