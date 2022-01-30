package com.example.myreminders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.fragmentContainerView2)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)


        bottomNavigationView.setupWithNavController(navController)
    }
}
