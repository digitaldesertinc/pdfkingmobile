package com.pdfking.ui

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView
import com.pdfking.ui.PdfUtils
import java.io.File

@Composable
fun PdfViewerScreen() {
    val context = LocalContext.current
    var annotateMode by remember { mutableStateOf(false) }

    // store list of strokes; each stroke is list of offsets
    val strokes = remember { mutableStateListOf<MutableList<Offset>>() }
    var canvasSize by remember { mutableStateOf(IntSize(1, 1)) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text(text = "PDF King - MVP") })

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            AndroidView(factory = { ctx ->
                PDFView(ctx, null).apply {
                    try {
                        fromAsset("sample.pdf").load()
                    } catch (e: Exception) {
                        // asset missing or error
                    }
                }
            }, modifier = Modifier.fillMaxSize())

            // Drawing overlay
            Canvas(modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            if (annotateMode) {
                                strokes.add(mutableListOf(offset))
                            }
                        },
                        onDrag = { change, dragAmount ->
                            if (annotateMode && strokes.isNotEmpty()) {
                                val last = strokes.last()
                                last.add(change.position)
                            }
                        },
                        onDragEnd = {
                            // stroke finished
                        },
                        onDragCancel = {}
                    )
                }
                .onGloballyPositioned { coords ->
                    canvasSize = coords.size
                }
            ) {
                // draw all strokes
                for (stroke in strokes) {
                    if (stroke.size > 1) {
                        val path = Path()
                        path.moveTo(stroke[0].x, stroke[0].y)
                        for (i in 1 until stroke.size) path.lineTo(stroke[i].x, stroke[i].y)
                        drawPath(path, color = Color.Red, strokeWidth = 6f)
                    } else if (stroke.size == 1) {
                        drawCircle(Color.Red, radius = 3f, center = stroke[0])
                    }
                }
            }

            // small status
            Box(modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .background(Color(0x77FFFFFF))) {
                Text(text = if (annotateMode) "Annotate: ON" else "Annotate: OFF", modifier = Modifier.padding(6.dp))
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                // Save: render overlay to bitmap and merge into PDF
                try {
                    // copy asset to a writable file first
                    val src = File(context.filesDir, "sample_copy.pdf")
                    PdfUtils.copyAssetToFile(context, "sample.pdf", src)

                    // render strokes into bitmap
                    val bmp = Bitmap.createBitmap(canvasSize.width.coerceAtLeast(1), canvasSize.height.coerceAtLeast(1), Bitmap.Config.ARGB_8888)
                    val androidCanvas = android.graphics.Canvas(bmp)
                    androidCanvas.drawColor(android.graphics.Color.TRANSPARENT)
                    // draw strokes
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.RED
                        strokeWidth = 12f
                        style = android.graphics.Paint.Style.STROKE
                        isAntiAlias = true
                    }
                    for (stroke in strokes) {
                        if (stroke.size > 1) {
                            val path = android.graphics.Path()
                            path.moveTo(stroke[0].x, stroke[0].y)
                            for (i in 1 until stroke.size) path.lineTo(stroke[i].x, stroke[i].y)
                            androidCanvas.drawPath(path, paint)
                        } else if (stroke.size == 1) {
                            androidCanvas.drawCircle(stroke[0].x, stroke[0].y, 4f, paint)
                        }
                    }

                    val outFile = File(context.getExternalFilesDir(null), "sample_annotated.pdf")
                    PdfUtils.stampBitmapOnPdf(src, outFile, bmp)
                    Toast.makeText(context, "Saved: ${outFile.absolutePath}", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Save failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }) { Text("Save") }

            Button(onClick = {
                annotateMode = !annotateMode
            }) { Text(if (annotateMode) "Stop" else "Annotate") }
        }
    }
}
