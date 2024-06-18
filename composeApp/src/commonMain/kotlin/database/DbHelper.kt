package database

import com.jetbrains.edukmpapp.AppDatabase
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DbHelper(private val driverFactory: DriverFactory) {
    private var db: AppDatabase? = null

    private val mutex = Mutex() // Mutex = 쓰레드 안전성을 제공

    suspend fun <Result: Any> withDatabase(block: suspend (AppDatabase) -> Result ): Result = mutex.withLock {
        // mutex.withLock = 코루틴이 동시에 함수를 호출하더라도 한번에 하나의 코루틴만 데이터베이스 객체를 생성함.
        // 아래 코드를 통해 한번만 데이터베이스 객체를 생성함
        if (db == null){
            db = createDb(driverFactory)
        }

        return@withLock block(db!!) // @withLock = withLock Scope에서 반환되는 값임을 명시해줌 만약 @withLock을 선언하지 않게되면 withLock블록 내의 코드가 아니라 withDatabase 함수에서 값이 반환됨.
    }

    private suspend fun createDb(driverFactory: DriverFactory): AppDatabase {
        return AppDatabase(driver = driverFactory.createDriver())
    }
}