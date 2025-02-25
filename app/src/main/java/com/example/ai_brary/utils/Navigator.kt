package com.example.ai_brary.utils

import com.example.ai_brary.scanner.BarcodeScannerActivity
import android.content.Context
import android.content.Intent

class Navigator {

    fun navigateToBarcodeScanner(context: Context) {
        val intent = Intent(context, BarcodeScannerActivity::class.java)
        context.startActivity(intent)
    }
}