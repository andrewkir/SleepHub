package com.andrewkir.sleepproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.content.Intent
import com.andrewkir.sleepproject.Utilities.Post
import kotlinx.android.synthetic.main.activity_post.*


class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val postId = intent.getStringExtra("post_id")

        val avatar = avatar
        val username = username
        val date = date
        val postTitle = post_title
        val body = body
        val likes = likes
        val comments = comments
        val commentsList = comments_list
        val edComment = comment_input
        val sendComment = comment_send

        //val post = Post()
        //username.setText(post.username)
        //date.setText(post.date)
        //postTitle.setText(post.postTitle)
        //body.setText(post.body)
        





    }


}
