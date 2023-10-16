package uz.itschool.shopping.networking

import retrofit2.Call
import retrofit2.http.GET
import uz.itschool.shopping.model.ProductData

interface APIService {
    @GET("/products")
    fun getAll(): Call<ProductData>
}