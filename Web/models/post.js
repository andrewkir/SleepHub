var mongoose = require("mongoose");

var PostSchema = new mongoose.Schema({
    userId: String,
    username: String,
    displayName: String,
    post: String,
    date: {type: Date, default: Date.now},
    likes: [String],
    comments: [
        {
            commenterId: String,
            commentBody: String,
            date: {type: Date, default: Date.now}
        }
    ],
    viewers: [String],
    views: {type: Number, default: 0},
});

module.exports = mongoose.model("post", PostSchema, "post");