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
        },
        email: {
            type: String,
            lowercase: true
        },
        isVerfied: {
            type: Boolean,
            default: false
        }
    },
    gLink: {
        type: Boolean,
        default: false
    },
    google: {
        id: {
            type: Number,
        },
        gender: {
            type: String
        }
    },
    token: {
        type: String
    }
});

module.exports = mongoose.model("user", UserSchema, "user");