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

        textView.visibility = View.INVISIBLE

        autocompleteFunctionality()

        adapteros = ArrayAdapter(this, R.layout.list_element, arrayListForAdapter)

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

                if(input == "") {
                    Toast.makeText(applicationContext, "You need to type a beer first!",
                        Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(
                        applicationContext, "That beer is not in the database yet :(",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                textView.visibility = View.INVISIBLE
                arrayListForAdapter.clear()
                adapteros.notifyDataSetChanged()
                return@nullCheck
            }
            textView.visibility = View.VISIBLE

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
        val list: LiveData<Array<String>> =
            BeerDatabase.getDatabase(this)!!.beerDao().getAllBeers()

        list.observe(this, Observer {
            val adapter = ArrayAdapter<String>(
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putStringArrayList("savedList", arrayListForAdapter)

        if(textView.visibility == View.VISIBLE) {
            outState.putInt("textView", 1)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val restoredArray = savedInstanceState.getStringArrayList("savedList")

        val textViewCheck = savedInstanceState.getInt("textView")

        if (textViewCheck == 1) {
            textView.visibility = View.VISIBLE
        }

        arrayListForAdapter.clear()
        restoredArray?.forEach {
            arrayListForAdapter.add(it)
            Log.d("TAG", "in foreach")
        }
        adapteros.notifyDataSetChanged()
    }
}
