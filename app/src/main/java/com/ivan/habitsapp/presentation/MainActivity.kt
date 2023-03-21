package com.ivan.habitsapp.presentation

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.ivan.habitsapp.R
import com.ivan.habitsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDrawerLayoutNavigation()

        openHabitsListFragment()
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
                R.id.menuItemHomeScreen -> Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                R.id.menuItemAppInfo -> Toast.makeText(this, "App Info", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
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

    private fun openHabitsListFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainActivityFragmentContainer, HabitsListFragment())
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}