package com.example.similarbeers

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BeerDao {

    @Query("SELECT * FROM beers  WHERE beer LIKE :query")
    fun getBeers(query: String): LiveData<List<Beer>>

    @Query("SELECT beer FROM beers")
    fun getAllBeers(): LiveData<Array<String>>

    @Query("SELECT beer FROM beers WHERE style = :inputStyle AND NOT beer = :input")
    fun getSimilarBeers(input: String, inputStyle: String): LiveData<Array<String>>

    @Query("SELECT style FROM beers WHERE beer = :input" )
    fun getStyle(input: String): LiveData<String>

    @Insert
    fun insert(beer: Array<Beer>)
}