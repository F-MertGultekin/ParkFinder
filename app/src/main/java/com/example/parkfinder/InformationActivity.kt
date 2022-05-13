package com.example.parkfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

class InformationActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        val informationContent = findViewById<TextView>(R.id.InfoScreen)
        val informationTV = findViewById<TextView>(R.id.informationTV)
        val whatisthisapp = findViewById<TextView>(R.id.whatisthisapp)
        val whatisthisappexplanation = findViewById<TextView>(R.id.whatisthisappexplanation)
        val howtousethisapp = findViewById<TextView>(R.id.howtousethisapp)
        val howtousethisappexplanation = findViewById<TextView>(R.id.howtousethisappexplanation)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.information_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.informationBackButton -> {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}