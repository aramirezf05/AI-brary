package com.example.ai_brary

import android.app.Application
import com.example.ai_brary.module.AppComponent
import com.example.ai_brary.module.DaggerAppComponent

class AIbraryApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.create()
    }

    override fun onCreate() {
        super.onCreate()
        // Additional setup if necessary
    }
}