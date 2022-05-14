package com.example.parkfinder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.parkfinder.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.sql.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private val TAG = "MyActivity"
    private var connection: Connection? = null
    private lateinit var binding: ActivityMapsBinding

    companion object {

        private const val ip = "smartcitydeu2.database.windows.net"
        private const val port = "1433"
        private const val Classes = "net.sourceforge.jtds.jdbc.Driver"
        private const val database = "SmartCityDB"
        private const val username = "SmartCityAdmin"
        private const val password = "Smart_City_Admin"
        private const val url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database + ";ssl=require"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectToDB()

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
        val street = intent.getStringExtra("street")
        val neighbourhood = intent.getStringExtra("neighbourhood")
        val district = intent.getStringExtra("district")
        val address = "$street $neighbourhood $district"
        Log.d(TAG,  "address $address")
        showMapForPlace(address,mMap)
    }

    private fun showMapForPlace(placeName: String, googleMap: GoogleMap) {
        val geocoder = Geocoder(this)
        try {
            val addresses = geocoder.getFromLocationName(placeName, 1)
            if (addresses.isNotEmpty()) {
                val address = addresses.first()
                val location = LatLng(address.latitude,address.longitude)
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(location))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15F))
                googleMap.addMarker(MarkerOptions().position(location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                Log.d(TAG, "latitude $address.latitude")
                Log.d(TAG, "longitude $address.longitude")
                Log.d(TAG, "location $location")
                Log.d(TAG,  "place name $placeName")
                query(address.latitude,address.longitude)
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
    private fun query(latitude: Double, longitude: Double) {
        var location = "$latitude,$longitude"

        if (connection != null) {
            var statement: Statement? = null
            try {
                statement = connection!!.createStatement()
                //val resultSet: ResultSet = statement.executeQuery("SELECT isParkingAvailable FROM [dbo].[ParkPlaces]")
                val resultSet: ResultSet = statement.executeQuery("SELECT isParkingAvailable FROM [dbo].[ParkPlaces] WHERE location = '$location'")

                Log.d("Result",location)
                while (resultSet.next()) {
                    Thread.sleep(100)
                    //Log.d("Result",resultSet.getString(1) + " - " +resultSet.getString(2)+ " - " +resultSet.getString(3)+ " - " +resultSet.getString(4)+ " - " +resultSet.getByte(5).toString()+ " - " + resultSet.getString(6))
                    Log.d("Result",resultSet.getString(1))
                }
            } catch (e: SQLException) {
                e.printStackTrace()
                Log.d("Hata","Hata")
            }
        } else {

        }
    }
    private fun connectToDB(){

        //GlobalScope.launch(Dispatchers.IO) {
        //}

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.INTERNET),
            PackageManager.PERMISSION_GRANTED
        )

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            Class.forName(Classes)
            connection = DriverManager.getConnection(
                url,
                username,
                password
            )
            Log.d("Succes","Succes")

        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            Log.d("Error","Error")
        } catch (e: SQLException) {
            e.printStackTrace()
            Log.d("Failure","Failure")
        }
    }
}


