package com.example.similarbeers

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beers")
data class Beer(
    @PrimaryKey
    @ColumnInfo(name = "beer")
    var beerName: String,

    @NonNull
    @ColumnInfo(name = "style")
    val beerStyle: String
) {
    companion object {
        fun populateData(): Array<Beer> {
            return arrayOf(
                Beer("Heineken", "not_good"),
                Beer("Amstel", "good"),
                Beer("Alfa", "not_good"),
                Beer("Kaiser", "good"),
                Beer("Leffe", "good")
            )
        }
    }
}