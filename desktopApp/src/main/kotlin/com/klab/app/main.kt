package com.klab.app

import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.klab.app.database.BuildDesktop
import klab.shared.generated.resources.Res
import klab.shared.generated.resources.logo

fun main() = application {
    BuildDesktop().initDatabase()

    Window(
        onCloseRequest = ::exitApplication,
        title = "klab",
        icon = painterResource(Res.drawable.logo)
    ) {
        App()
    }
}