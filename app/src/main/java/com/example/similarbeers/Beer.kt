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
                Beer("Leffe Radieuse", "good"),
                Beer("Leffe Blonde", "mediocre"),
                Beer("Leffe Brune", "idk"),
                Beer("McFarland", "good"),
                Beer("Corona", "tbd"),
                Beer("Mythos", "mediocre"),
                Beer("Vergina", "not_good"),
                Beer("Fix", "not_good"),
                Beer("Mamos", "good"),
                Beer("Murphy's", "not_good"),
                Beer("Guiness", "not_good"),
                Beer("Erdinger", "idk"),
                Beer("Paulaner", "mediocre"),
                Beer("Ardwen", "idk"),
                Beer("Excelsior", "idk"),
                Beer("Budweiser", "idk"),
                Beer("Pils", "mediocre"),
                Beer("Chimay", "good"),
                Beer("La Trappe", "mediocre")

            )
        }
    }
}