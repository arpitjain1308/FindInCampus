package com.example.findit.data.model

data class ItemReport(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val type: String = "LOST",
    val location: String = "",
    val imageUrls: List<String> = emptyList(),
    val status: String = "ACTIVE",
    val campusId: String = "LPU",
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis()
)