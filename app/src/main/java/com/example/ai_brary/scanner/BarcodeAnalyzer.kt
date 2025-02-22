package com.example.ai_brary.scanner

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.barcode.BarcodeScanning
import androidx.camera.core.ImageProxy

class BarcodeAnalyzer(
    private val onBarcodeDetected: (String) -> Unit // Callback que recibe el ISBN
) : ImageAnalysis.Analyzer {

    private val scanner: BarcodeScanner = BarcodeScanning.getClient() // Cliente de ML Kit para escanear códigos de barras

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let { rawValue ->
                        onBarcodeDetected(rawValue)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("BarcodeAnalyzer", "Error al escanear: ${e.message}")
            }
            .addOnCompleteListener {
                imageProxy.close() // Cierra la imagen para liberar recursos
            }
    }
}
