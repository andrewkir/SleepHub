package com.andrewkir.sleepproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.content.Intent
import android.util.Log
import android.view.View
import com.andrewkir.sleepproject.Services.Web
import com.andrewkir.sleepproject.Utilities.Post
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.raw_post_item.*
import java.time.LocalDate


class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        postCard.visibility = View.INVISIBLE
        val postId = intent.getStringExtra("post_id")
        val username = postUsername
        val date = postDate
        val body = postBody
        val like = postLike
        val likes = postLikes
        val comments = postComments
        enableSpinner(true)
        val commentsList = comments_list
        val edComment = comment_input
        val sendComment = comment_send
        fun change(post: Post) {
            username.text = post.username
            date.text = post.date
            body.text = post.body
            likes.text = post.likes.toString()
            comments.text = post.comments.toString()
            if(post.isLiked) like.setBackgroundResource(R.drawable.ic_favorite_border_red_24dp)
            postCard.visibility = View.VISIBLE
            enableSpinner(false)
        }
        Log.d("RES", postId.toString())
        Web.getPost(this, App.prefs.userToken, postId) { post ->
            change(post)
        }
        //val post = Post()
        //username.setText(post.username)
        //date.setText(post.date)
        //postTitle.setText(post.postTitle)
        //body.setText(post.body)


    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            postSpinner.visibility = View.VISIBLE
        } else {
            postSpinner.visibility = View.INVISIBLE
        }
    }
}
