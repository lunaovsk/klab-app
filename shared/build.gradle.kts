import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kover)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    jvm()

    android {
        namespace = "com.klab.app.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            
            // SQLDelight Driver para Android
            implementation(libs.sqldelight.android)
        }
        
        iosMain.dependencies {
            // SQLDelight Driver para iOS
            implementation(libs.sqldelight.native)
        }
        
        jvmMain.dependencies {
            // SQLDelight Driver para Desktop/JVM
            implementation(libs.sqldelight.sqlite)
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            
            // Injeção de Dependência
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            
            // Threads e Assincronismo
            implementation(libs.kotlinx.coroutines.core)
            
            // SQLDelight (Extensão para suportar Flow/Coroutines)
            implementation(libs.sqldelight.coroutines)
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.mockk.common)
            }
        }
        val androidHostTest by getting {
            dependencies {
                implementation(libs.mockk.common)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

sqldelight {
    databases {
        create("KlabDatabase") {
            packageName.set("com.klab.app.infra.local") 
        }
    }
}

kover {
    reports {
        filters {
            excludes {
                packages(
                    "com.klab.app.presentation.*",
                    "com.klab.app.di.*"
                )
            }
        }
        verify {
            rule("Cobertura Minima da Regra de Negocio") {
                // TODO: Descomentar essa trava quando o projeto tiver mais testes
                /*
                bound {
                    minValue = 80 
                }
                */
            }
        }
    }
}
