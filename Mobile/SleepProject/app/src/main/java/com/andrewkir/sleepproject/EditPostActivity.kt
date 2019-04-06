package com.andrewkir.sleepproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_edit_post.*

class EditPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)

        val postId = intent.getStringExtra("post_id")

        var edPost = edit_post.text.toString()
        val deleteButton = delete_btn
        val saveButton = save_btn

        deleteButton.setOnClickListener(){

        }

        saveButton.setOnClickListener(){

        }

    }
}
