var crypto          = require("crypto"),
    express         = require('express'),
    mongoose        = require("mongoose"),
    randomstring    = require("randomstring");

const config = require("./root/config.json");
const router = express.Router();

const db = require("./models");
var myRoutes = require('./routes');

router.use(function(req, res, next) {
  next();
});

router.route('/register').post(function(req, res) {
    db.collections.User.findOne({
        username: req.body.username
    })
    .then((data)=>{
        if(data){
            return res.sendStatus(403);
        } else {
            db.collections.User.create({
                username: req.body.username,
                // ["local.password"]: hash(req.body.password),
                ["local.password"]: req.body.password,
                displayName: req.body.name,
                androidToken: randomstring.generate(20)
            })
            .then((user)=>{
                response = {
                    name: user.displayName,
                    username: user.username,
                    gLink: user.gLink,
                    google: user.google ? user.google : 0,
                    statusCode: 200,
                    androidToken: user.androidToken
                }
                res.send(JSON.stringify(response));
            })
            .catch((err)=>{})
        }
    });
});
router.route('/login').post(function(req, res) {
    db.collections.User.findOne({
        username: req.body.username
    })
    .then((data)=>{
        if(!data){
            return res.sendStatus(403);
        } else {
            if(data.local.password == req.body.password){
                response = {
                    name: data.displayName,
                    username: data.username,
                    gLink: data.gLink,
                    google: data.google ? data.google : 0,
                    statusCode: 200,
                    androidToken: data.androidToken
                }
                return res.send(JSON.stringify(response));
            } else {
                return res.sendStatus(401);
            }
        }
        
    });
});
router.route('/getPosts').post(function(req, res) {
    db.collections.User.findOne({
        androidToken: req.body.androidToken
    })
    .then((data)=>{
        db.collections.Post.find()
        .then((postData)=>{
            androidArray = [];
            if(req.body.amount.toString() == "0"){
                postData = postData.reverse();
            } else {
                postData = postData.reverse().slice(0, req.body.amount);

            }
            postData.forEach(el => {
                var temp = {};
                var date = new Date(el.date);
                temp.likes = el.likes.length;
                temp.views = el.views;
                temp.id = el._id;
                temp.body = el.body;
                temp.name = el.displayName;
                temp.username = el.username;
                temp.date = `${date.getDate()}-${date.getMonth()}-${date.getUTCFullYear()}`;
                temp.comments = el.comments.length;
                temp.isLiked = el.likes.indexOf(data._id) > -1;
                androidArray.push(temp);
            });
            // res.send("JSON.stringify(androidArray)");
            res.send(JSON.stringify(androidArray));
        })
        .catch((err)=>{
            res.send(err);
        })
    })
    .catch((err)=>{
        res.send(JSON.stringify({
            error: "wrong token"
        }))
    })
});

router.route('/postInfo').post(function(req, res) {
    db.collections.User.findOne({
        androidToken: req.body.androidToken
    })
    .then((data)=>{
        db.collections.Post.findOne({
            _id: req.body.id
        })
        .then((postData)=>{
            var temp = {};
            
            temp.likes = postData.likes.length;
            var date = postData.date;
            temp.views = postData.views;
            temp.body = postData.body;
            temp.name = postData.displayName;
            temp.username = postData.username;
            temp.isMine = data._id == postData.userId ? true : false;
            temp.date = `${date.getHours()}:${date.getMinutes()} ${date.getDate()}-${date.getMonth()}-${date.getUTCFullYear()}`;
            temp.commentsLen = postData.comments.length;
            temp.isLiked = postData.likes.indexOf(data._id) > -1;
            res.send(JSON.stringify(temp));
        })
        .catch((err)=>{
            res.send(err);
        })
    })
    .catch((err)=>{
        res.send(JSON.stringify({
            error: "wrong token"
        }))
    })
});

router.route('/profile').post(function(req, res) {
    db.collections.User.findOne({
        androidToken: req.body.androidToken
    })
    .then((data)=>{
        db.collections.User.findOne({
            _id: req.body.id
        })
        .then((user)=>{
            var temp = {};
            temp.rating = user.rating;
            temp.name = user.displayName;
            temp.username = user.username;
            temp.isMe = data._id == req.body.id ? true : false;
            if(data._id == req.body.id){
                temp.gLink = user.gLink;
            }
            res.send(JSON.stringify(temp));
        })
        .catch((err)=>{
            res.send(err);
        })
    })
    .catch((err)=>{
        res.send(JSON.stringify({
            error: "wrong token"
        }))
    })
});


function hash(password){
    return crypto.createHmac('sha256', config.HASHSECRET).update(password).digest('hex');
}

module.exports = router;