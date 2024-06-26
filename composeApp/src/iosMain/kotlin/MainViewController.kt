import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import com.seiko.imageloader.intercept.imageMemoryCacheConfig
import com.seiko.imageloader.intercept.painterMemoryCacheConfig
import okio.Path.Companion.toPath
import root.DefaultRootComponent
import root.RootContent

fun MainViewController() = ComposeUIViewController {
    CompositionLocalProvider(
    LocalImageLoader provides remember { generateImageLoader() },
    ){
        // 라이프사이클 관련 부분은 설명하려다가 잘 안되서 별도 영상에서 추가 설명하겠다고 함.
        val lifecycle = LifecycleRegistry()
        lifecycle.subscribe(LifecycleCallbacksImpl())
        val homeViewModel = HomeViewModel()
        val root = DefaultRootComponent(
            componentContext = DefaultComponentContext(LifecycleRegistry()),
            homeViewModel
        )
        RootContent(root, modifier = Modifier)
    }
}

class LifecycleCallbacksImpl: Lifecycle.Callbacks {
    override fun onCreate(){
        super.onCreate()
        println("onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("onDestroy")
    }

    override fun onPause() {
        super.onPause()
        println("onPause")
    }
}

fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        components {
            setupDefaultComponents()
        }
        interceptor {
            // cache 32MB bitmap
            bitmapMemoryCacheConfig {
                maxSize(32 * 1024 * 1024) // 32MB
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
                directory(getCacheDir().toPath().resolve("image_cache"))
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}

private fun getCacheDir(): String {
    return NSSearchPathForDirectoriesInDomains(
        NSCachesDirectory,
        NSUserDomainMask,
        true,
    ).first() as String
}