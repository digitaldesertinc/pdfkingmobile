package com.pdfking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pdfking.ui.PdfViewerScreen
import com.pdfking.ui.theme.PdfKingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PdfKingTheme {
                PdfViewerScreen()
            }
        }
    }
}
