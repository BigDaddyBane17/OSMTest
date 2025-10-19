package com.example.map

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapModule {
    
    @Provides
    @Singleton
    fun provideCameraStateManager(@ApplicationContext context: Context): CameraStateManager {
        return CameraStateManager(context)
    }
}
