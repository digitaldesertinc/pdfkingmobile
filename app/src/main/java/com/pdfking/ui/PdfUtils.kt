package com.pdfking.ui

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory

object PdfUtils {
    // copy an asset file to destination file
    fun copyAssetToFile(context: Context, assetName: String, outFile: File) {
        context.assets.open(assetName).use { input ->
            FileOutputStream(outFile).use { output ->
                input.copyTo(output)
            }
        }
    }

    // Stamp a bitmap (annotations) onto the first page of the PDF and save to outFile
    fun stampBitmapOnPdf(srcPdf: File, outFile: File, overlay: Bitmap) {
        // Load document
        PDDocument.load(srcPdf).use { doc ->
            if (doc.numberOfPages > 0) {
                val page: PDPage = doc.getPage(0)

                // Convert bitmap to JPEG bytes via PDFBox's JPEGFactory
                val img = JPEGFactory.createFromImage(doc, overlay)

                // Create a content stream appending the image
                PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true).use { cs ->
                    val mediaBox = page.mediaBox
                    val pageW = mediaBox.width
                    val pageH = mediaBox.height

                    // Draw image to fill the page (scale overlay to page size)
                    cs.drawImage(img, 0f, 0f, pageW, pageH)
                }
            }

            // save to outFile
            FileOutputStream(outFile).use { fos ->
                doc.save(fos)
            }
        }
    }
}
