package com.example.parkfinder

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.parkfinder.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var colorView :ImageView
    private lateinit var informationTextView: TextView
    private val TAG = "MyActivity"
    private lateinit var binding: ActivityMapsBinding
    private  lateinit var geocoder: Geocoder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        colorView = findViewById(R.id.imageView)
        informationTextView = findViewById(R.id.parkingInformationTV)


        colorView.setImageResource(R.drawable.ic_baseline_brightness_1_24)
        informationTextView.text ="There are many park slots"

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        val street = intent.getStringExtra("street")
        val neighbourhood = intent.getStringExtra("neighbourhood")
        val district = intent.getStringExtra("district")
        var address = "$street $neighbourhood $district"
        showMapForPlace(address,mMap)
    }

    private fun showMapForPlace(placeName: String, googleMap: GoogleMap) {
        // geocode place name to get list of locations
        val geocoder = Geocoder(this)
        // get first and most likely match only
        try {
            val addresses = geocoder.getFromLocationName(placeName, 1)
            // if user enters nonsense into the search bar, addresses list will be empty
            // use and intent to launch map app, for first location, if a location is found
            if (addresses.isNotEmpty()) {
                // display place
                val address = addresses.first()
                Log.d(TAG, "First address is $address")
                // makes a string in the form "geo:45, -90" (mpls approximate result)
                val geoUriString = "geo:${address.latitude},${address.longitude}"
                Log.d(TAG, "Using geo uri $geoUriString")
                val geoUri = Uri.parse(geoUriString)
                Log.d(TAG, "Launching map activity")



                // Zoom in the Google Map



                val location = LatLng(address.latitude,address.longitude)
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(location))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15F));
                googleMap.addMarker(MarkerOptions().position(location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                // this will send a message to the android system to say this activity wants to launch another activity
            } else {
                Log.d(TAG, "No places found for string $placeName")

            }
        } catch (e: IOException) {
            Log.e(TAG, "Unable to geocode place $placeName", e)
        }

    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.maps_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.backButton -> {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}


