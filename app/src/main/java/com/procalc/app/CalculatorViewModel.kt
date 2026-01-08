package com.procalc.app

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.DecimalFormat

data class CalculatorState(
    val displayText: String = "0",
    val history: List<String> = emptyList(),
    val isResult: Boolean = false,
    val error: String? = null
)

class CalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorState())
    val uiState: StateFlow<CalculatorState> = _uiState.asStateFlow()

    private val decimalFormat = DecimalFormat("#.##########")

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> append(action.number)
            is CalculatorAction.Operation -> append(action.symbol)
            is CalculatorAction.Clear -> clear()
            is CalculatorAction.Delete -> delete()
            is CalculatorAction.Calculate -> calculate()
            is CalculatorAction.ClearHistory -> clearHistory()
        }
    }

    private fun append(str: String) {
        _uiState.update { state ->
            val current = if (state.displayText == "0" || state.isResult || state.error != null) "" else state.displayText
            
            // Basic check to prevent double operators
            val isOperator = str in listOf("+", "-", "×", "÷", "^", "%")
            val lastChar = current.lastOrNull()
            
            if (isOperator && lastChar != null && lastChar.toString() in listOf("+", "-", "×", "÷", "^", "%", ".")) {
                // Replace last operator
                state.copy(
                    displayText = current.dropLast(1) + str,
                    isResult = false,
                    error = null
                )
            } else {
                state.copy(
                    displayText = current + str,
                    isResult = false,
                    error = null
                )
            }
        }
    }

    private fun clear() {
        _uiState.update { it.copy(displayText = "0", error = null, isResult = false) }
    }

    private fun delete() {
        _uiState.update { state ->
            if (state.displayText.length <= 1 || state.isResult || state.error != null) {
                state.copy(displayText = "0", isResult = false, error = null)
            } else {
                state.copy(displayText = state.displayText.dropLast(1))
            }
        }
    }
    
    private fun clearHistory() {
        _uiState.update { it.copy(history = emptyList()) }
    }

    private fun calculate() {
        val expression = _uiState.value.displayText
        if (expression.isEmpty()) return

        try {
            val result = CalculationEngine.evaluate(expression)
            val formattedResult = decimalFormat.format(result)
            
            // Add to history
            val historyEntry = "$expression = $formattedResult"
            val newHistory = listOf(historyEntry) + _uiState.value.history

            _uiState.update {
                it.copy(
                    displayText = formattedResult,
                    isResult = true,
                    history = newHistory,
                    error = null
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Error", isResult = true) }
        }
    }
}

sealed class CalculatorAction {
    data class Number(val number: String) : CalculatorAction()
    data class Operation(val symbol: String) : CalculatorAction()
    object Clear : CalculatorAction()
    object Delete : CalculatorAction()
    object Calculate : CalculatorAction()
    object ClearHistory : CalculatorAction()
}