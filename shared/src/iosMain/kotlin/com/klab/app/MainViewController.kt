package com.klab.app

import androidx.compose.ui.window.ComposeUIViewController
import com.klab.app.database.BuilderIOS

fun MainViewController() = ComposeUIViewController {
    BuilderIOS().initDatabase()
    App()
}