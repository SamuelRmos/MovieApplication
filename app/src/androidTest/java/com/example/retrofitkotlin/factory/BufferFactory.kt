package com.example.retrofitkotlin.factory

class BufferFactory {

    companion object {
        fun makeCachedMovie() = DataFactory.randomList()
    }
}