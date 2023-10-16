package uz.itschool.shopping.model

data class ProductData(
    val limit: Int,
    val skip: Int,
    val total: Int,
    val products: List<Product>
)