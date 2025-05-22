package com.example.prc_ecommerceapp.di

import android.content.Context
import com.example.prc_ecommerceapp.data.roomdatabase.AppDatabase
import com.example.prc_ecommerceapp.data.roomdatabase.CartDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/** Módulo que proporciona dependencias relacionadas con la base de datos. */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /** Provee la instancia de la base de datos.  */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    /** Provee el DAO de la base de datos.  */
    @Provides
    fun provideCartDao(database: AppDatabase): CartDao {
        return database.cartDao()
    }
}