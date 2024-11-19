package com.example.movie.model

data class Video(val site: String, val type: String, val key: String)

data class VideosList(val results: List<Video>)