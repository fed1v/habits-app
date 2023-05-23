package com.ivan.habitsapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ivan.habitsapp.R
import com.ivan.habitsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("DEBUGGG", "OnCreate!!!1!")
        super.onCreate(savedInstanceState)
        Log.d("DEBUGGG", "OnCreate!!!..................!2")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("DEBUGGG", "OnCreate!!!..................!3")


        navController = Navigation.findNavController(this, R.id.mainActivityNavHost)

        initDrawerLayoutNavigation()
        openHabitsViewPagerFragment()

        Log.d("DEBUGGG", "OnCreate!!!..................!4")

    }

    private fun initDrawerLayoutNavigation() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.mainActivityDrawerLayout,
            R.string.open,
            R.string.close
        )
        binding.mainActivityDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.mainActivityNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuItemHomeScreen -> {
                    binding.mainActivityDrawerLayout.closeDrawer(GravityCompat.START)
                    openHabitsViewPagerFragment()
                }
                R.id.menuItemAppInfo -> {
                    binding.mainActivityDrawerLayout.closeDrawer(GravityCompat.START)
                    openAppInfoFragment()
                }
                else -> Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun openAppInfoFragment() {
        navController.navigate(R.id.appInfoFragment)
    }

    private fun openHabitsViewPagerFragment() {
        navController.navigate(R.id.habitsViewPagerFragment)
    }

    override fun onBackPressed() {
        println(supportFragmentManager.fragments)
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}