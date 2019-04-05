package com.andrewkir.sleepproject.Adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.andrewkir.sleepproject.R
import com.andrewkir.sleepproject.Utilities.Post
import com.andrewkir.sleepproject.Utilities.PostMinified
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

class RecyclerPostsAdapter (val context: Context, val posts: List<PostMinified>, val itemClick: (PostMinified) -> Unit) :
    RecyclerView.Adapter<RecyclerPostsAdapter.Holder>(){

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


    inner class Holder(itemView: View?, val itemClick: (PostMinified) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val postUsername = itemView?.findViewById<TextView>(R.id.raw_username)
        val postBody = itemView?.findViewById<TextView>(R.id.raw_post)
        val postLikes = itemView?.findViewById<TextView>(R.id.raw_likes)
        val postLike = itemView?.findViewById<TextView>(R.id.raw_like)
        val postDate = itemView?.findViewById<TextView>(R.id.raw_date)
        val postComments = itemView?.findViewById<TextView>(R.id.raw_comments)
        fun bindSubject(post: PostMinified, context: Context){
            if(post.isLiked){
                postLike?.setBackgroundResource(R.drawable.ic_favorite_border_red_24dp)
            }
            postComments?.text = post.comments.toString()
            postDate?.text = post.date
            postUsername?.text = post.username
            postBody?.text = if (post.body.length > 120) post.body.slice(0..120)+"..." else post.body
            postLikes?.text = post.likes.toString()
            itemView.setOnClickListener { itemClick(post) }
        }
    }
}