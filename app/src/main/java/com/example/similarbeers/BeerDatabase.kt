package com.example.similarbeers

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors

@Database(entities = arrayOf(Beer::class), version = 1)
abstract class BeerDatabase : RoomDatabase() {
    abstract fun beerDao(): BeerDao
    companion object {
        private var INSTANCE: BeerDatabase? = null
        fun getDatabase(context: Context): BeerDatabase? {
            if (INSTANCE == null) {
                synchronized(BeerDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        BeerDatabase::class.java, "version3.db" //CHANGE THE NAME HERE AFTER EVERY NEW ENTRY TO THE DATABASE
                    ).addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Executors.newSingleThreadScheduledExecutor().execute(object : Runnable {
                                override fun run() {
                                    getDatabase(context)!!.beerDao().insert(Beer.populateData())
                                    Log.d("DatabaseFilled", "DatabaseFilled")
                                }
                            })
                        }
                    })
                        .build()
                }
            }
            return INSTANCE
        }
    }
}