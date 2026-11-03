package com.nexauren.scanflow.data

data class ScanDocument(
    val id: String,
    val name: String,
    val path: String,
    val mimeType: String,
    val size: Long,
    val createdAt: Long,
    val folder: String,
    val favorite: Boolean = false
)
