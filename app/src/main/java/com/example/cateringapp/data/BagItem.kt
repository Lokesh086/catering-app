package com.example.cateringapp.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class BagItem(
    val customer: Customer,
    val product: Product,
    val timestamp: String
)