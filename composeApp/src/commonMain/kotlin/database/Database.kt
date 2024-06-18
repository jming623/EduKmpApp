package database

import app.cash.sqldelight.async.coroutines.awaitAsList
import com.arkivanov.decompose.extensions.compose.jetbrains.pages.defaultVerticalPager
import data.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class Database(
    private val dbHelper: DbHelper,
    private val scope: CoroutineScope
) {
    fun clearDatabase() {
        scope.launch {
            dbHelper.withDatabase { database ->
                database.appDatabaseQueries.removeAllProducts()
            }
        }
    }

    suspend fun getAllProducts(): List<Product> {
        // suspend fun은 쓰레드를 효율적으로 사용하기 위해 잠시 멈추는 지점을 가리키는 키워드
        // 더 세부적인 예로는 비동기 실행을 했을 때, 해당 쓰레드가 비동기 함수의 동작이 끝나기까지 기다리는게 아니라, 다른 작업을 처리하러간다. (효율적인 자원관리, 그에따른 작업 처리 속도 향상)
        var items: List<Product>
        val result = scope.async {
            dbHelper.withDatabase { database ->
                items = database.appDatabaseQueries.selectAllProducts(::mapProductSelecting).awaitAsList() // async scope에서 대기하는데 필요한 함수
                items
            }
        }

         return result.await() // async작업이 끝날때 결과를 반환
    }

    private fun mapProductSelecting(
        id: Long,
        title: String,
        image: String,
        price: Double,
        category: String?,
        description: String?,
    ): Product {
        return Product(
            id = id.toInt(),
            price = price,
            category = category,
            description = description,
            title = title,
            image = image
        )
    }

    suspend fun createProducts(items: List<Product>) {
        val result = scope.async {
            dbHelper.withDatabase { database ->
                items.forEach {
                    insertProduct(it)
                }
            }
        }
    }


    suspend fun insertProduct(item: Product) {
        val result = scope.async {
            dbHelper.withDatabase { database ->
                println("insertProduct_item=$item")
                database.appDatabaseQueries.insertProduct(
                    id = item.id?.toLong(),
                    title = item.title.toString(),
                    image = item.image.toString(),
                    price = item.price ?: 0.0,
                    category = item.category.toString(),
                    description = item.description.toString()
                )
            }
        }
    }
}