package com.andrewkir.sleepproject

class Post {
    var userId = ""
    var username = ""
    var displayName = ""
    var body = ""
    var postTitle = ""
    var date = ""
    var likes = ArrayList<String>()
    var comments = ArrayList<Comment>()
    var viewers = ArrayList<String>()
    var views = Int
}

class Comment(commenterId: String, commentBody: String, date: String){
    var commenterId = commenterId
    var commentBody = commentBody
    var date = date
}