package com.andrewkir.sleepproject.Utilities

class Post(userId: String, username: String, displayName: String,
           body: String, postTitle: String, date: String, likes: ArrayList<String>,
           comments: ArrayList<Comment>, viewers: ArrayList<String>, views: Int, isLiked: Boolean) {
    var userId = userId
    var username = username
    var displayName = displayName
    var body = body
    var postTitle = postTitle
    var date = date
    var likes = likes
    var comments = comments
    var viewers = viewers
    var views = views
    var isLiked = isLiked

}

class Comment(commenterId: String, commentBody: String, date: String){
    var commenterId = commenterId
    var commentBody = commentBody
    var date = date
}