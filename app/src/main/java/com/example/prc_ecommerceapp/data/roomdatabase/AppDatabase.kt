package com.example.prc_ecommerceapp.data.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/** Clase que representa la base de datos de la aplicación.*/
@Database(
    entities = [CartItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        /** Instancia de la base de datos. */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /** Obtiene la instancia de la base de datos.
         * @param context El contexto de la aplicación.
         * @return La instancia de la base de datos.*/
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                /** Construye y devuelve la instancia de la base de datos. */
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Prc_shop_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}