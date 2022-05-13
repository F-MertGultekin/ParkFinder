package com.example.parkfinder

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.sql.Connection
import java.sql.DriverManager
import android.Manifest;
import android.util.Log
import android.view.View;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class ParkFinderApplication : AppCompatActivity(){

    companion object {

        private const val ip = "smartcitydeu2.database.windows.net"
        private const val port = "1433"
        private const val Classes = "net.sourceforge.jtds.jdbc.Driver"
        private const val database = "SmartCityDB"
        private const val username = "SmartCityAdmin"
        private const val password = "Smart_City_Admin"
        private const val url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database + ";ssl=require"
    }


        private var connection: Connection? = null
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            //setContentView(R.layout.activity_main)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.INTERNET),
                PackageManager.PERMISSION_GRANTED
            )

            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            try {
                Class.forName(Classes)
                connection = DriverManager.getConnection(url, username, password)
                Log.d("Succes","Succes")
                sqlButton()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
                Log.d("Error","Error")
            } catch (e: SQLException) {
                e.printStackTrace()
                Log.d("Failure","Failure")
            }
        }

        fun sqlButton() {
            if (connection != null) {
                var statement: Statement? = null
                try {
                    statement = connection!!.createStatement()
                    val resultSet: ResultSet = statement.executeQuery("SELECT isParkingAvailable FROM [dbo].[ParkPlaces]")
                    //val resultSet: ResultSet = statement.executeQuery("SELECT isParkingAvailable FROM [dbo].[ParkPlaces] WHERE location = '38.3746648,27.1888535'")

                    Log.d("Result","--")
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



}