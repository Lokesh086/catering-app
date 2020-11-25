package com.example.cateringapp.data

data class OrderItem(
    val orderId: String,
    val productList: List<Product> = listOf<Product>(),
    val price: Float,
    val isDelivered: Boolean = false
)