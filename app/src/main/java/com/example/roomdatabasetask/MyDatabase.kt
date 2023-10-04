package com.example.roomdatabasetask


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [UserModel::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun myDao(): UserDataDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "users.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}