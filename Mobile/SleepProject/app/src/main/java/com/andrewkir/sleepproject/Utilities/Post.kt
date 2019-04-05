package com.andrewkir.sleepproject.Utilities

class Post(var postId: String, var username: String, var displayName: String,
           var body: String, var postTitle: String, var date: String, var likes: ArrayList<String>,
           var comments: ArrayList<Comment>, var viewers: ArrayList<String>, var views: Int, var isLiked: Boolean
)

class Comment(var commenterId: String, var commentBody: String, var date: String){
}

class PostMinified(var username: String, var body: String,var isLiked: Boolean,var likes: Int, var postId: String,
                   var date: String, var comments:Int){
}