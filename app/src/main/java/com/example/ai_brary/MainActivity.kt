package com.example.ai_brary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai_brary.module.AppComponent
import com.example.ai_brary.utils.Navigator
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as AIbraryApplication).appComponent.inject(this)
        enableEdgeToEdge()
        setContent {
            MyApp(navigator)
        }
    }
}

@Composable
fun MyApp(navigator: Navigator) {
    val context = LocalContext.current
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.welcome_message),
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navigator.navigateToBarcodeScanner(context)
            }) {
                Text(stringResource(R.string.click_me_button))
            }
        }
    }
}
