package com.andrewkir.sleepproject.Utilities

class Post(var likes:Int, var comments: Int, var body:String, var username: String, var isMine:Boolean, var date: String, var isLiked: Boolean)

class Comment(var id:String,var body: String, var username: String, var isMine: Boolean, var isLiked: Boolean,var likes:Int){
}

class PostMinified(var username: String, var body: String,var isLiked: Boolean,var likes: Int, var postId: String,
                   var date: String, var comments:Int){
}

class likeClass(var amount:Int, var isLiked: Boolean)