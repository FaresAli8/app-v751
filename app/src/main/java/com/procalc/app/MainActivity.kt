package com.procalc.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.procalc.app.ui.CalculatorScreen
import com.procalc.app.ui.theme.ProCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProCalcTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}