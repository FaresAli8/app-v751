package com.procalc.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.procalc.app.CalculatorAction
import com.procalc.app.CalculatorViewModel

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Display Area
        Box(
            modifier = Modifier
                .weight(0.35f)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(horizontalAlignment = Alignment.End) {
                // History Peek (Last Item)
                if (state.history.isNotEmpty()) {
                    Text(
                        text = state.history.first().split("=")[0] + " =",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        maxLines = 1
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))

                // Main Display
                Text(
                    text = state.error ?: state.displayText,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (state.displayText.length > 10) 48.sp else 64.sp
                    ),
                    color = if (state.error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.End,
                    maxLines = 2,
                    lineHeight = 64.sp
                )
            }
        }
        
        Divider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(modifier = Modifier.height(16.dp))

        // Buttons Grid
        Column(
            modifier = Modifier.weight(0.65f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Row 1: AC, DEL, %, /
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CalcButton("AC", Modifier.weight(1f), MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer) { viewModel.onAction(CalculatorAction.Clear) }
                CalcButton("⌫", Modifier.weight(1f), MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer) { viewModel.onAction(CalculatorAction.Delete) }
                CalcButton("%", Modifier.weight(1f), MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer) { viewModel.onAction(CalculatorAction.Operation("%")) }
                CalcButton("÷", Modifier.weight(1f), MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer) { viewModel.onAction(CalculatorAction.Operation("÷")) }
            }

            // Row 2: 7, 8, 9, x
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CalcButton("7", Modifier.weight(1f)) { viewModel.onAction(CalculatorAction.Number("7")) }
                CalcButton("8", Modifier.weight(1f)) { viewModel.onAction(CalculatorAction.Number("8")) }
                CalcButton("9", Modifier.weight(1f)) { viewModel.onAction(CalculatorAction.Number("9")) }
                CalcButton("×", Modifier.weight(1f), MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer) { viewModel.onAction(CalculatorAction.Operation("×")) }
            }

            // Row 3: 4, 5, 6, -
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CalcButton("4", Modifier.weight(1f)) { viewModel.onAction(CalculatorAction.Number("4")) }
                CalcButton("5", Modifier.weight(1f)) { viewModel.onAction(CalculatorAction.Number("5")) }
                CalcButton("6", Modifier.weight(1f)) { viewModel.onAction(CalculatorAction.Number("6")) }
                CalcButton("-", Modifier.weight(1f), MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer) { viewModel.onAction(CalculatorAction.Operation("-")) }
            }

            // Row 4: 1, 2, 3, +
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CalcButton("1", Modifier.weight(1f)) { viewModel.onAction(CalculatorAction.Number("1")) }
                CalcButton("2", Modifier.weight(1f)) { viewModel.onAction(CalculatorAction.Number("2")) }
                CalcButton("3", Modifier.weight(1f)) { viewModel.onAction(CalculatorAction.Number("3")) }
                CalcButton("+", Modifier.weight(1f), MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer) { viewModel.onAction(CalculatorAction.Operation("+")) }
            }
            
            // Row 5: More Advanced (sqrt, ^, (, )) - Toggleable or fixed? Let's make a 6th row for these
             Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CalcButton("√", Modifier.weight(1f), MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer) { viewModel.onAction(CalculatorAction.Operation("√(")) } // Auto open parenthesis
                CalcButton("^", Modifier.weight(1f), MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer) { viewModel.onAction(CalculatorAction.Operation("^")) }
                CalcButton("(", Modifier.weight(1f), MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer) { viewModel.onAction(CalculatorAction.Operation("(")) }
                CalcButton(")", Modifier.weight(1f), MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer) { viewModel.onAction(CalculatorAction.Operation(")")) }
            }

            // Row 6: 0, ., =
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CalcButton("0", Modifier.weight(2f)) { viewModel.onAction(CalculatorAction.Number("0")) }
                CalcButton(".", Modifier.weight(1f)) { viewModel.onAction(CalculatorAction.Number(".")) }
                CalcButton("=", Modifier.weight(1f), MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary) { viewModel.onAction(CalculatorAction.Calculate) }
            }
        }
    }
}

@Composable
fun CalcButton(
    symbol: String,
    modifier: Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(if (symbol == "0") 2f else 1f, matchHeightConstraintsFirst = false) // Adjust 0 aspect ratio
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .clickable { onClick() }
    ) {
        Text(
            text = symbol,
            fontSize = 28.sp,
            color = contentColor,
            fontWeight = FontWeight.Medium
        )
    }
}