class JSPlatform: Platform {
    override val name: String = "Compose from Web using Kotlin/JS"
}

actual fun getPlatform(): Platform = JSPlatform()