package com.andrewkir.sleepproject

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.andrewkir.sleepproject.Adapters.RecyclerComments
import com.andrewkir.sleepproject.Adapters.RecyclerPostsAdapter
import com.andrewkir.sleepproject.Services.Web
import com.andrewkir.sleepproject.Utilities.Comment
import com.andrewkir.sleepproject.Utilities.Post
import kotlinx.android.synthetic.main.activity_post.*


class PostActivity : AppCompatActivity() {

    var postId = ""
    var isMine = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppThemeBar)
        setContentView(R.layout.activity_post)

        supportActionBar?.title = ""
        postCard.visibility = View.INVISIBLE
        postId = intent.getStringExtra("post_id")
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
                    likeButton.setBackgroundResource(0)
                    likeButton.setBackgroundResource(R.drawable.ic_favorite_border_red_24dp)
                } else {
                    likeButton.setBackgroundResource(0)
                    likeButton.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp)
                }
                likes.text = like.amount.toString()
            }
        }

        fun change(post: Post) {
            isMine = post.isMine
            username.text = post.username
            date.text = post.date.split(" ")[0]
            body.text = post.body
            likes.text = post.likes.toString()
            comments.text = post.comments.toString()
            supportActionBar?.title = post.date.split(" ")[1]
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
        val recycler = comments_list
        val layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager
        recycler.setHasFixedSize(true)
        Web.getComments(this, App.prefs.userToken, postId) { comments ->
            recycler.adapter = RecyclerComments(this, comments) { comment ->
                if (comment.isMine) {
                    dialogDelete(comment, recycler)
                }
            }
        }

        comment_send.setOnClickListener {
            if (comment_input.text.toString().isNotEmpty()) {
                Web.sendComment(this, App.prefs.userToken, postId, comment_input.text.toString()) { comments ->
                    recycler.adapter = RecyclerComments(this, comments) { comment ->
                        if (comment.isMine) {
                            dialogDelete(comment, recycler)
                        }
                    }
                }
            }
        }
    }

    fun dialogDelete(comment: Comment, recycler: RecyclerView): Boolean {
        val builder = AlertDialog.Builder(this, R.style.AlertDialog)
        builder.setTitle("Delete Comment")
        var res = false
        builder.setMessage("Are you sure you want to delete this comment?")
        builder.setPositiveButton("Yes") { _, _ ->
            res = true
            Web.deleteComment(this, App.prefs.userToken, postId, comment.id) { comments ->
                recycler.adapter = RecyclerComments(this, comments) { comment ->
                }
            }
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        return res
    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            postSpinner.visibility = View.VISIBLE
        } else {
            postSpinner.visibility = View.INVISIBLE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isMine) {
            menuInflater.inflate(R.menu.post_activity_menu, menu)
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.edit_menu) {
            val editor = Intent(this, EditPostActivity::class.java)
            editor.putExtra("post_id", postId)
            startActivity(editor)
        }
        return true
    }
}
