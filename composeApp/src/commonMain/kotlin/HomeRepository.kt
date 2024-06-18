import data.Product
import database.datasource.ProductsLocalDataSource
import database.datasource.ProductsRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.flow

class HomeRepository(
    private val productsLocalDataSource: ProductsLocalDataSource,
    private val productsRemoteDataSource: ProductsRemoteDataSource
){

    private suspend fun getAllProducts(forceReload: Boolean = false): List<Product>{
        // 이 부분은  ProductsRemoteDataSource 함수쪽으로 옮겨짐
        // val response = httpClient.get("https://fakestoreapi.com/products")
        // return response.body()

        val cacheItems = productsLocalDataSource.getAllProducts()
        return if(cacheItems.isNotEmpty() && !forceReload) {
            println("fromCache")
            cacheItems
        }else{
            println("fromNetwork")
            productsRemoteDataSource.getAllProducts().also {
                productsLocalDataSource.clearDb()
                productsLocalDataSource.saveProducts(it)
            }
        }
    }

    fun getProducts(forceReload: Boolean = false) = flow {
        emit(getAllProducts())
    }
}