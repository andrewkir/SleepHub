package com.andrewkir.sleepproject.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.andrewkir.sleepproject.R
import com.andrewkir.sleepproject.Utilities.Comment

class RecyclerComments(val context: Context, val comments: List<Comment>, val itemClick: (Comment) -> Unit) :
    RecyclerView.Adapter<RecyclerComments.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.raw_comment_item, parent,false)
        //view.setBackgroundColor(Color.parseColor("#ff9966"))
        return Holder(view,itemClick)
    }


    override fun getItemCount(): Int {
        return comments.count()
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindSubject(comments[position],context)
    }


    inner class Holder(itemView: View?, val itemClick: (Comment) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val postUsername = itemView?.findViewById<TextView>(R.id.raw_username_comment)
        val postBody = itemView?.findViewById<TextView>(R.id.raw_post_comment)
//        val postLikes = itemView?.findViewById<TextView>(R.id.raw_likes_comment)
//        val postLike = itemView?.findViewById<TextView>(R.id.raw_like_comment)

        fun bindSubject(comment: Comment, context: Context){
//            if(comment.isLiked){
//                postLike?.setBackgroundResource(R.drawable.ic_favorite_border_red_24dp)
//            }
            postBody?.text = comment.body
            postUsername?.text = comment.username
//            postLikes?.text = comment.likes.toString()
            itemView.setOnClickListener { itemClick(comment) }
        }
    }
}