package com.example.ai_brary.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.ai_brary.data.local.BookDatabase
import com.example.ai_brary.repository.BookRepository
import com.example.ai_brary.viewmodel.ScannerViewModel
import com.example.ai_brary.viewmodel.ScannerViewModelFactory
import com.example.bibliotecaapp.data.remote.RetrofitInstance
import com.google.common.util.concurrent.ListenableFuture

class BarcodeScannerActivity : ComponentActivity() {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private val viewModel: ScannerViewModel by viewModels {
        val appDatabase = BookDatabase.getInstance(applicationContext)
        val apiService = RetrofitInstance.apiService
        val repository = BookRepository(appDatabase.bookDao(), apiService)
        ScannerViewModelFactory(repository)
    }

    private lateinit var previewView: PreviewView

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                setupContent()
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (allPermissionsGranted()) {
            setupContent()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        baseContext, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    private fun setupContent() {
        setContent {
            val context = LocalContext.current
            var isCameraActive by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    isCameraActive = true
                }) {
                    Text(text = "Iniciar Cámara")
                }

                if (isCameraActive) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { ctx ->
                                previewView = PreviewView(ctx)
                                startCamera(previewView)
                                previewView
                            }
                        )
                    }
                }
            }
        }
    }

    private fun startCamera(previewView: PreviewView) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), BarcodeAnalyzer { isbn ->
                handleBarcode(isbn)
            })

            preview.setSurfaceProvider(previewView.surfaceProvider)

            val camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageAnalysis
            )
        }, ContextCompat.getMainExecutor(this))
    }

    private fun handleBarcode(isbn: String) {
        Toast.makeText(this, "Código ISBN detectado: $isbn", Toast.LENGTH_SHORT).show()
        Log.d("BarcodeScanner", "Código ISBN: $isbn")

        viewModel.fetchBookByIsbn(isbn)
    }
}