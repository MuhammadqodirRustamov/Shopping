package uz.itschool.shopping.networking

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uz.itschool.shopping.model.ProductData

interface APIService {
    @GET("/products")
    fun getAll(): Call<ProductData>

    @GET("/products/categories")
    fun getCategories(): Call<List<String>>

    @GET("/products/category/{category}")
    fun getByCategory(@Path("category") category : String): Call<ProductData>


    @GET("/products/search")
    fun search(@Query("q") query : String): Call<ProductData>


}