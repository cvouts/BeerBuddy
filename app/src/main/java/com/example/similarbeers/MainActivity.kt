package com.example.similarbeers

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var arrayListForAdapter = ArrayList<String>()
    private lateinit var adapteros: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        autocompleteFunctionality()

        adapteros = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, arrayListForAdapter
        )
        resultListView.adapter = adapteros

        imageButtonView.setOnClickListener {
            closeKeyboard()
            buttonClicked(autocompleteView.text.toString())
        }
    }

    private fun buttonClicked(input: String) {
        val beerStyleLive: LiveData<String> =
            BeerDatabase.getDatabase(this)!!.beerDao().getStyle(input)

        beerStyleLive.observe(this, Observer nullCheck@ { beerStyle ->

            if(beerStyle == null) {
                Toast.makeText(applicationContext, "That beer is not in the database yet :(",
                    Toast.LENGTH_SHORT).show()
                return@nullCheck
            }
            
            val listLive: LiveData<Array<String>> =
                BeerDatabase.getDatabase(this)!!.beerDao()
                    .getSimilarBeers(input, beerStyle)

            listLive.observe(this, Observer { list ->

                arrayListForAdapter.clear()
                list.forEach {
                    arrayListForAdapter.add(it)
                }
                adapteros.notifyDataSetChanged()
            })
        })
    }

    private fun autocompleteFunctionality() {
        var list: LiveData<Array<String>> =
            BeerDatabase.getDatabase(this)!!.beerDao().getAllBeers()

        list.observe(this, Observer {

            var adapter = ArrayAdapter<String>(
                this, android.R.layout.simple_expandable_list_item_1, it
            )

            autocompleteView.setAdapter(adapter)
            autocompleteView.threshold = 1
        })
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
