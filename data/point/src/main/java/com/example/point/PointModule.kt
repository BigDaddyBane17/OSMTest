package com.example.point

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PointModule {
    
    @Provides
    @Singleton
    fun providePointRepository(pointDao: PointDao): PointRepository {
        return PointRepositoryImpl(pointDao)
    }
    
    @Provides
    @Singleton
    fun providePointDatabase(@ApplicationContext context: Context): PointDatabase {
        return PointDatabase.create(context)
    }
    
    @Provides
    fun providePointDao(database: PointDatabase): PointDao {
        return database.pointDao()
    }
}