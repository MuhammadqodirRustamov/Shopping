package uz.itschool.shopping.model

data class ProductData(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)