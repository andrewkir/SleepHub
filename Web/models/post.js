var mongoose = require("mongoose");

var PostSchema = new mongoose.Schema({
    userId: String,
    username: String,
    displayName: String,
    body: String,
    date: {type: Date, default: Date.now},
    likes: [String],
    comments: [
        {
            username: String,
            userId: String,
            body: String,
            date: {type: Date, default: Date.now},
            likes: [String]
        }
    ],
    viewers: [String],
    views: {type: Number, default: 0},
});

module.exports = mongoose.model("post", PostSchema, "post");