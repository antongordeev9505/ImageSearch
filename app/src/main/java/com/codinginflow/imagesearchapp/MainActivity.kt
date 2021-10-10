package com.codinginflow.imagesearchapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint

//mainActivity should be injected themself
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //make Upbutton up
    //NavController - knows how to handle navigation in NavHost (navHost is MainActivity)
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //if we use FragmentContainerView - use this... insted of just findNavController
        //reference to navhost
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        navController = navHostFragment.findNavController()

        //connect appBar to Navigation graph
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        //setup Action Bar - to have UpButton
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    //Up Button
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}