package com.klab.app.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavBackStackEntry

class BackNavigationController {

    private val handlers =
        mutableMapOf<NavBackStackEntry, () -> Unit>()

    fun register(
        entry: NavBackStackEntry,
        onBack: () -> Unit
    ) {
        handlers[entry] = onBack
    }

    fun unregister(entry: NavBackStackEntry) {
        handlers.remove(entry)
    }

    fun requestBack(
        entry: NavBackStackEntry?,
        defaultAction: () -> Unit
    ) {
        val handler = handlers[entry]

        if (handler != null) {
            handler()
        } else {
            defaultAction()
        }
    }
}

val LocalBackNavigationController =
    staticCompositionLocalOf<BackNavigationController> {
        error("BackNavigationController não configurado.")
    }

val LocalBackNavigationEntry =
    staticCompositionLocalOf<NavBackStackEntry> {
        error("Entrada de navegação não configurada.")
    }

@Composable
fun RegisterBackNavigation(
    onBack: () -> Unit
) {
    val controller = LocalBackNavigationController.current
    val entry = LocalBackNavigationEntry.current
    val currentOnBack by rememberUpdatedState(onBack)

    DisposableEffect(controller, entry) {

        controller.register(entry) {
            currentOnBack()
        }

        onDispose {
            controller.unregister(entry)
        }
    }
}