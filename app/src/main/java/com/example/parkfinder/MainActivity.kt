package com.example.parkfinder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.*



private var selectedDistrictName = ""
private var selectedNeigbourhoodName=""
private var selectedStreetName=""

internal lateinit var city_spinner: Spinner
internal lateinit var district_spinner: Spinner
internal lateinit var neighbourhood_spinner: Spinner
internal lateinit var street_spinner: Spinner


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val searchButton: Button = findViewById(R.id.search_button)
        city_spinner = findViewById(R.id.city_spinner)
        district_spinner = findViewById(R.id.district_spinner)
        neighbourhood_spinner = findViewById(R.id.neighbourhood_spinner)
        street_spinner = findViewById(R.id.street_spinner)

        cityAdapter()
        districtAdapter()


        //districtSelection
        district_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val district: String = parent?.getItemAtPosition(position).toString()

                selectedDistrictName = district

                if(district != "İlçe") neighbourhoodAdapter(district) else emptyNeighbourhoodAdapter()


            }

        }
        //NeighbourhoodSelection
        neighbourhood_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var neighbourhood: String = parent?.getItemAtPosition(position).toString()



                if(neighbourhood != "Mahalle") {
                    selectedNeigbourhoodName=neighbourhood
                    neighbourhood = selectedDistrictName+"_"+neighbourhood
                    streetAdapter(neighbourhood)

                }
                else emptyStreetAdapter()
            }

        }
        //StreetSelection
        street_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val street: String = parent?.getItemAtPosition(position).toString()
                selectedStreetName = street

            }

        }
        //search button Handling
        searchButton.setOnClickListener {

            val intent = Intent(this, MapsActivity::class.java)

            intent.putExtra("street", selectedStreetName)
            intent.putExtra("neighbourhood", selectedNeigbourhoodName)
            intent.putExtra("district", selectedDistrictName)
            intent.putExtra("connection", selectedDistrictName)
            startActivity(intent)
            finish()
        }
    }



    private fun cityAdapter(){
        ArrayAdapter.createFromResource(
            this,
            R.array.cities,
            R.layout.spinner_item_customize
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            city_spinner.adapter = adapter
        }
    }
    private fun districtAdapter(){
        ArrayAdapter.createFromResource(
            this,
            R.array.districts,
            R.layout.spinner_item_customize
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            district_spinner.adapter = adapter
        }
    }
    private fun neighbourhoodAdapter(district: String){
        ArrayAdapter.createFromResource(
            this,
            resources.getIdentifier(
                district, "array",
                packageName
            ),
            R.layout.spinner_item_customize
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            neighbourhood_spinner.adapter = adapter
        }
    }
    private fun emptyNeighbourhoodAdapter(){
        ArrayAdapter.createFromResource(
            this,
            R.array.emptyNeighbourhoodList,
            R.layout.spinner_item_customize
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            neighbourhood_spinner.adapter = adapter
        }
    }
    private fun streetAdapter(neighbourhood: String){
        ArrayAdapter.createFromResource(
            this,
            resources.getIdentifier(
                neighbourhood, "array",
                packageName
            ),
            R.layout.spinner_item_customize
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            street_spinner.adapter = adapter
        }
    }
    private fun emptyStreetAdapter(){
        ArrayAdapter.createFromResource(
            this,
            R.array.emptyStreetList,
            R.layout.spinner_item_customize
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            street_spinner.adapter = adapter
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.infoButton -> {
            val intent = Intent(this, InformationActivity::class.java)
            startActivity(intent)
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}