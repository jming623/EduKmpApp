import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "2.0.0-RC3" //Decompose Step2
    id("app.cash.sqldelight") version "2.0.2" // SQLDelight Step1
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    jvm("desktop")

    // JSPlatform Step1
    js(IR){
        moduleName = "EduKmpApp"
        browser(){
            // Tool bundler for converting kotlin code to js code
            commonWebpackConfig(){
                outputFileName = "EduKmpApp.js"

                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).copy()
            }

            binaries.executable() // it will generate executable js files
        }

    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)

            //Decompose Step3
            implementation("com.arkivanov.decompose:decompose:2.2.2-compose-experimental")
            implementation("com.arkivanov.decompose:extensions-compose-jetbrains:2.2.2-compose-experimental")

            // SQLDelight Step3
            implementation("app.cash.sqldelight:android-driver:2.0.2")
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)

            // SQLDelight Step3
            implementation("app.cash.sqldelight:native-driver:2.0.2")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.mvvm.core)

            api(libs.image.loader)
            // Decompose Step1 - navigation사용 목적
            implementation(libs.arkivanov.decompose.v222composeexperimental)
            implementation(libs.arkivanov.extensions.compose.jetbrains)

            // Koin Step1 - 의존성 주입 목적
            implementation(libs.koin.core)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)

            // SQLDelight Step3
            implementation("app.cash.sqldelight:sqlite-driver:2.0.2")
        }

        jsMain.dependencies {
            // SQLDelight Step3
            implementation("app.cash.sqldelight:web-worker-driver:2.0.2")
            implementation(devNpm("copy-webpack-plugin", "9.1.0"))
            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.0.2"))
            implementation(npm("sql.js", "1.8.0"))
        }

    }
}

android {
    namespace = "com.jetbrains.edukmpapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.jetbrains.edukmpapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
        // Koin Step2
        implementation(libs.koin.android)
    }
}
dependencies {
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.material)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.jetbrains.edukmpapp"
            packageVersion = "1.0.0"
        }
    }
}

// Step3
compose.experimental {
    web.application {}
}

// SQLDelight Step2
sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.jetbrains.edukmpapp")
            generateAsync.set(true)
        }
    }
}