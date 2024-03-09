//package com.example.movie.di
//
//import android.content.Context
//import androidx.room.Room
//import com.example.movie.R
//import com.example.movie.persistence.AppDataBase
//import com.example.movie.persistence.MovieDao
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object PersistenceModule {
//
//    @Singleton
//    @Provides
//    fun provideRoomDatabase(@ApplicationContext context: Context): AppDataBase =
//        Room.databaseBuilder(
//            context,
//            AppDataBase::class.java,
//            context.getString(R.string.movie_db)
//        )
//            .allowMainThreadQueries()
//            .fallbackToDestructiveMigration()
//            .build()
//
//    @Singleton
//    @Provides
//    fun provideMovieDao(appDataBase: AppDataBase): MovieDao = appDataBase.movieDao()
//}