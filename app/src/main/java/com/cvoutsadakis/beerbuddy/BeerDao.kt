package com.cvoutsadakis.beerbuddy

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BeerDao {

    @Query("SELECT beer FROM beers")
    fun getAllBeers(): LiveData<Array<String>>

    @Query("SELECT beer FROM beers WHERE style = :inputStyle AND NOT beer = :input ORDER BY beer")
    fun getSimilarBeers(input: String, inputStyle: String): LiveData<Array<String>>

    @Query("SELECT style FROM beers WHERE beer = :input" )
    fun getStyle(input: String): LiveData<String>

    @Insert
    fun insert(beer: Array<Beer>)
}