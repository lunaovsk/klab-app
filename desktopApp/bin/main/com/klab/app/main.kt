package com.klab.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.klab.app.database.BuildDesktop

fun main() = application {
    BuildDesktop().initDatabase()

    Window(
        onCloseRequest = ::exitApplication,
        title = "klab",
    ) {
        App()
    }
}
