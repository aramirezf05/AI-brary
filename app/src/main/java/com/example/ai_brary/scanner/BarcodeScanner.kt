import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.ai_brary.viewmodel.ScannerViewModel
import com.example.ai_brary.scanner.BarcodeAnalyzer
import com.google.common.util.concurrent.ListenableFuture

class BarcodeScannerActivity : ComponentActivity() {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private val viewModel: ScannerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                startCamera()
            }

            // Observar cambios en el libro escaneado
            viewModel.scannedBook.observe(this) { book ->
                book?.let {
                    Toast.makeText(this, "Libro encontrado: ${it.title}", Toast.LENGTH_SHORT).show()
                    //navigateToBookDetail(it)
                }
            }
        }
    }

    private fun startCamera() {
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

            val camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageAnalysis
            )

            preview.surfaceProvider = findViewById<PreviewView>(android.R.id.content).surfaceProvider

        }, ContextCompat.getMainExecutor(this))
    }

    private fun handleBarcode(isbn: String) {
        Toast.makeText(this, "Código ISBN detectado: $isbn", Toast.LENGTH_SHORT).show()
        Log.d("BarcodeScanner", "Código ISBN: $isbn")

        viewModel.fetchBookByIsbn(isbn)
    }
}
