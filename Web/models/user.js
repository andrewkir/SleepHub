var mongoose = require("mongoose");

var UserSchema = new mongoose.Schema({
    username: {
        type: String,
        required: true
    },
    displayName: {
        type: String,
        required: false
    },
    rating: {
        type: Number,
        required: true,
        default: 0
    },
    registrationDate: {
        type: Date,
        required: true,
        default: Date.now()
    },
    local: {
        password: {
            type: String
        }
    },
    gLink: {
        type: Boolean,
        default: false
    },
    google: {
        id: {
            type: Number,
        }
    },
    secret_token: {
        type: String
    },
    token:{
        type: String
    },
    androidToken: {
        type: String
    }
});

module.exports = mongoose.model("user", UserSchema, "user");