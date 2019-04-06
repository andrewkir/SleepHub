package com.andrewkir.sleepproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.andrewkir.sleepproject.Services.Web
import com.andrewkir.sleepproject.Utilities.Post
import kotlinx.android.synthetic.main.activity_post.*


class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        postCard.visibility = View.INVISIBLE
        val postId = intent.getStringExtra("post_id")
        val username = postUsername
        val date = postDate
        val body = postBody
        val likeButton = postLike
        val likes = postLikes
        val comments = postComments
        enableSpinner(true)
        val commentsList = comments_list
        val edComment = comment_input
        val sendComment = comment_send

        likeButton.setOnClickListener {
            Web.toggleLike(this, App.prefs.userToken, postId) { like ->
                if (like.isLiked) {
                    if (like.isLiked) {
                        likeButton.setBackgroundResource(0)
                        likeButton.setBackgroundResource(R.drawable.ic_favorite_border_red_24dp)
                    } else {
                        likeButton.setBackgroundResource(0)
                        likeButton.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp)
                    }
                }
                likes.text = like.amount.toString()
            }
        }

        fun change(post: Post) {
            username.text = post.username
            date.text = post.date
            body.text = post.body
            likes.text = post.likes.toString()
            comments.text = post.comments.toString()
            if (post.isLiked) {
                likeButton.setBackgroundResource(R.drawable.ic_favorite_border_red_24dp)
            } else {
                likeButton.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp)
            }
            postCard.visibility = View.VISIBLE
            enableSpinner(false)
        }
        Log.d("RES", postId.toString())
        Web.getPost(this, App.prefs.userToken, postId) { post ->
            change(post)
        }

    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            postSpinner.visibility = View.VISIBLE
        } else {
            postSpinner.visibility = View.INVISIBLE
        }
    }
}
