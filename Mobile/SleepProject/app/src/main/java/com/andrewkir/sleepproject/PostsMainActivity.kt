package com.andrewkir.sleepproject

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.andrewkir.sleepproject.Adapters.RecyclerPostsAdapter
import com.andrewkir.sleepproject.Utilities.Post
import kotlinx.android.synthetic.main.activity_posts_main.*
import kotlinx.android.synthetic.main.app_bar_posts_main.*
import kotlinx.android.synthetic.main.content_posts_main.*
import kotlinx.android.synthetic.main.nav_header_posts_main.*

class PostsMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
//        supportActionBar?.setHomeButtonEnabled(true)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_google__g__logo)
        val recycler = findViewById<RecyclerView>(R.id.postsRecycler)
//        val posts = List<Post>(Post())
//        recycler.adapter = RecyclerPostsAdapter(this, )
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun OnLogoutClick(view: View) {
        val builder = AlertDialog.Builder(this,R.style.AlertDialog)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to quit?")
        builder.setPositiveButton("Yes") { _, _ ->
            App.prefs.isLoggedIn = false
            App.prefs.userUsername = ""
            App.prefs.userName = ""
            App.prefs.userToken = ""

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }
}
