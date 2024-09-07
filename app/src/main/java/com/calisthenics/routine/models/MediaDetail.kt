package com.calisthenics.routine.models

data class MediaDetail(
    val imageType: String = "GIF",
    val base64Image: String = "",
    val imagePath: String = "",
    val mediaSource: String = "LOCAL"
)