package com.example.mvvm_demo

data class QuestionDTO(
    val dealerID: String,
    val lastUpdatedTime: String,
    val logFilePath: String,
    val resourceID: String,
    val resourceType: String,
    val status: String,
    val supportPerson: String,
    val tenantName: String
)