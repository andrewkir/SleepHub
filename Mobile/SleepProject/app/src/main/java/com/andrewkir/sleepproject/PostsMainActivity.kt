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
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.andrewkir.sleepproject.Adapters.RecyclerPostsAdapter
import com.andrewkir.sleepproject.Services.Web
import com.andrewkir.sleepproject.Utilities.Comment
import com.andrewkir.sleepproject.Utilities.Post
import com.andrewkir.sleepproject.Utilities.PostMinified
import kotlinx.android.synthetic.main.activity_posts_main.*
import kotlinx.android.synthetic.main.app_bar_posts_main.*
import kotlinx.android.synthetic.main.content_posts_main.*
import kotlinx.android.synthetic.main.nav_header_posts_main.*
import android.support.v4.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.raw_post_item.*


class PostsMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var posts = mutableListOf<PostMinified>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts_main)
        setSupportActionBar(toolbar)
        enableSpinner(true)
        val recycler = findViewById<RecyclerView>(R.id.postsRecycler)
        recycler.adapter = RecyclerPostsAdapter(this, posts) { post ->
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra("post_id", post.postId)
            startActivity(intent)
        }
        val layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager
        recycler.setHasFixedSize(true)
        Web.getPosts(this, App.prefs.userToken, 0) { receivedPosts ->
            changePosts(receivedPosts)
            enableSpinner(false)
            recycler.adapter = RecyclerPostsAdapter(this, posts) { post ->
                val intent = Intent(this, PostActivity::class.java)
                intent.putExtra("post_id", post.postId)
                startActivity(intent)
            }
        }
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)

        val name_drawer = headerView.findViewById<TextView>(R.id.drawerName)
        val username_drawer = headerView.findViewById<TextView>(R.id.drawerUsername)

        name_drawer.text = App.prefs.userName
        username_drawer.text = App.prefs.userUsername
        val swipeContainer = findViewById<SwipeRefreshLayout>(R.id.swipe_container)
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener {
            Web.getPosts(this, App.prefs.userToken, 0) { receivedPosts ->
                changePosts(receivedPosts)
                swipeContainer.isRefreshing = false
                recycler.adapter = RecyclerPostsAdapter(this, posts) { post ->
                    val intent = Intent(this, PostActivity::class.java)
                    intent.putExtra("post_id", post.postId)
                    startActivity(intent)
                }
                enableSpinner(false)
            }
        }
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )


        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun changePosts(resPosts: MutableList<PostMinified>) {
        posts = resPosts
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun OnLogoutClick(view: View) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialog)
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

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            postsSpinner.visibility = View.VISIBLE
        } else {
            postsSpinner.visibility = View.INVISIBLE
        }
    }
}
