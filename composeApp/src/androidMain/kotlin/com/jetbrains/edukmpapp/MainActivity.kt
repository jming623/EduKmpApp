package com.jetbrains.edukmpapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import com.seiko.imageloader.intercept.imageMemoryCacheConfig
import com.seiko.imageloader.intercept.painterMemoryCacheConfig
import com.seiko.imageloader.option.androidContext
import okio.Path.Companion.toOkioPath
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import root.DefaultRootComponent
import root.RootComponent
import root.RootContent


class MainActivity : ComponentActivity() {

    private val modules = module {
        single<ComponentContext> { defaultComponentContext() }
    }

    init {
        loadKoinModules(modules)
    }

    private val rootComponent: RootComponent by inject() // by inject() = Koin에 등록된 객체를 lazy 하게 주입

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalImageLoader provides remember { generateImageLoader() },
            ) {
//              HomeViewModel을 클래스내부에서 초기화하지 않고, commonModule쪽에서 초기화 함
//              val homeViewModel = HomeViewModel()
//              DefaultRootComponent는 클래스 내부에서 초기화하지 않고, commonModule쪽에서 초기화 하고, by inject()를 통해 주입을 받음
//              val root = DefaultRootComponent(defaultComponentContext(), homeViewModel)
                RootContent(rootComponent, modifier = Modifier)
            }
        }
    }

    private fun generateImageLoader(): ImageLoader {
        return ImageLoader {
            options {
                androidContext(applicationContext)
            }
            components {
                setupDefaultComponents()
            }
            interceptor {
                // cache 25% memory bitmap
                bitmapMemoryCacheConfig {
                    maxSizePercent(applicationContext, 0.25)
                }
                // cache 50 image
                imageMemoryCacheConfig {
                    maxSize(50)
                }
                // cache 50 painter
                painterMemoryCacheConfig {
                    maxSize(50)
                }
                diskCacheConfig {
                    directory(applicationContext.cacheDir.resolve("image_cache").toOkioPath())
                    maxSizeBytes(512L * 1024 * 1024) // 512MB
                }
            }

        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    //App()
}