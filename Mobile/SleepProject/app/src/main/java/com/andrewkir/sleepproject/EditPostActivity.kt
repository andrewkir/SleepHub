package com.andrewkir.sleepproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.andrewkir.sleepproject.Services.Web
import com.andrewkir.sleepproject.Utilities.Post
import kotlinx.android.synthetic.main.activity_edit_post.*
import kotlinx.android.synthetic.main.activity_post.*

class EditPostActivity : AppCompatActivity() {

    var isNew = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)

        val postId = intent.getStringExtra("post_id")
        editCard.visibility = View.VISIBLE
        var edPost = edit_post.text.toString()
        val deleteButton = delete_btn
        val saveButton = save_btn

        fun change(post: Post) {
            editCard.visibility = View.VISIBLE
            edit_post.setText(post.body)
            enableSpinner(false)
            isNew = false
        }
        Log.d("RES", postId.toString())
        if(postId != "0") {
            Web.getPost(this, App.prefs.userToken, postId) { post ->
                change(post)
            }
        } else {
            editCard.visibility = View.VISIBLE
            enableSpinner(false)
        }
        deleteButton.setOnClickListener {

        }

        saveButton.setOnClickListener {
            if(isNew){
                Web.sendPost(this,App.prefs.userToken, edit_post.text.toString()){
                    success->
                    if(success){
                        finish()
                    } else {
                        Toast.makeText(this, "Error",Toast.LENGTH_SHORT).show()
                    }
                    enableSpinner(false)
                }
            }
        }

    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            editSpinner.visibility = View.VISIBLE
        } else {
            editSpinner.visibility = View.INVISIBLE
        }
    }
}
