package com.example.movieapp.factory

class BufferFactory {

    companion object {
        fun makeCachedMovie() = DataFactory.randomList()
    }
}