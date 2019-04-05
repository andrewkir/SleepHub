package com.andrewkir.sleepproject.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.andrewkir.sleepproject.R
import com.andrewkir.sleepproject.Utilities.Post

class RecyclerPostsAdapter (val context: Context, val posts: List<Post>, val itemClick: (Post) -> Unit) : RecyclerView.Adapter<RecyclerPostsAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.raw_post_item, parent,false)
        //view.setBackgroundColor(Color.parseColor("#ff9966"))
        return Holder(view,itemClick)
    }


    override fun getItemCount(): Int {
        return posts.count()
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindSubject(posts[position],context)
    }


    inner class Holder(itemView: View?, val itemClick: (Post) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val postUsername = itemView?.findViewById<TextView>(R.id.raw_username)
        val postBody = itemView?.findViewById<TextView>(R.id.raw_post)
        val postLikes = itemView?.findViewById<TextView>(R.id.raw_likes)
        val postLike = itemView?.findViewById<TextView>(R.id.raw_like)
        fun bindSubject(post: Post, context: Context){
            postUsername?.text = post.username
            postBody?.text = post.body
            postLikes?.text = post.likes.toString()
            itemView.setOnClickListener { itemClick(post) }
        }
    }
}