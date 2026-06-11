package com.example.tetrisduel.di

import com.example.tetrisduel.repositories.JuegoRepository
import com.example.tetrisduel.socket.SocketManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun proveerJuegoRepository(socketManager: SocketManager): JuegoRepository {
        return JuegoRepository(socketManager)
    }
}
