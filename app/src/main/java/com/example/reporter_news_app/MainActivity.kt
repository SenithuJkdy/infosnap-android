package com.example.reporter_news_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.navigation.ui.NavigationUI
import com.example.reporter_news_app.databinding.ActivityMainBinding
import com.example.reporter_news_app.ui.landing.LandingFragment
import com.example.reporter_news_app.ui.login.LoginFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_MyApp)
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()

        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                //When you navigate to NewsFragment, the Navigation Component treats it as a sub-screen, enabling the Up (Back) button instead of the Drawer Toggle.
                 R.id.nav_news ,R.id.nav_reporter_home, R.id.nav_editor_home
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        // show/hide navigation drawer
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_landing || destination.id == R.id.nav_login || destination.id == R.id.nav_signup) {
                supportActionBar?.hide()
                binding.appBarMain.fab.hide()
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                supportActionBar?.show()
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    showLogoutDialog()
                    true
                }
                R.id.nav_news -> {
                    // Navigate to news fragment and close drawer
                    navController.navigate(R.id.nav_news)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_edit_profile -> {
                    // Navigate to news fragment and close drawer
                    navController.navigate(R.id.nav_edit_profile)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
                    NavigationUI.onNavDestinationSelected(menuItem, navController) || super.onOptionsItemSelected(menuItem)
                }
            }
        }


    }

    private fun performLogout() {
    // Clear user session
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // Navigate to LandingFragment
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.nav_login)
    }


    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ ->
                performLogout()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}