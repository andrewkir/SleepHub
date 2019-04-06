var crypto          = require("crypto"),
    express         = require('express'),
    request         = require("request"),
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

router.route('/toggleLike').post(function(req, res) {
    db.collections.User.findOne({
        androidToken: req.body.androidToken
    })
    .then((data)=>{
        db.collections.Post.findOne({
            _id: req.body.id
        })
        .then((post)=>{
                var temp = {};
                var amountOfLikes = post.likes.length;
                if(post.likes.indexOf(data._id) > -1){
                    db.collections.Post.findByIdAndUpdate(post._id, {
                        $pull: {
                            likes: data._id
                        }
                    })
                    .then((result)=>{
                        res.send(JSON.stringify({"isLiked": false, "amount": --amountOfLikes}));
                    })
                } else {
                    db.collections.Post.findByIdAndUpdate(post._id, {
                        $push: {
                            likes: data._id
                        }
                    })
                    .then((result)=>{
                        res.send(JSON.stringify({"isLiked": true, "amount": ++amountOfLikes}));
                    })
                }
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

router.route('/add').post(function(req, res) {
    db.collections.User.findOne({
        androidToken: req.body.androidToken
    })
    .then((data)=>{
        db.collections.Post.create({
            body: req.body.body,
            userId: data._id,
            name: data.displayName
        })
        .then((postData)=>{
            return res.send(JSON.stringify({
                id: postData._id
            }))
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

router.route('/delete').post(function(req, res) {
    db.collections.User.findOne({
        androidToken: req.body.androidToken
    })
    .then((data)=>{
        db.collections.Post.findOne({
            _id: req.body.id
        })
        .then((postData)=>{
            if(data._id == postData.userId){
                db.collections.Post.findByIdAndDelete(postData._id)
                .then((result)=>{
                    res.sendStatus(200);
                })
            } else {
                res.sendStatus(403);
            }
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

router.route('/update').post(function(req, res) {
    db.collections.User.findOne({
        androidToken: req.body.androidToken
    })
    .then((data)=>{
        db.collections.Post.findOne({
            _id: req.body.id
        })
        .then((postData)=>{
            if(data._id == postData.userId){
                db.collections.Post.findByIdAndUpdate(postData._id, {
                    body: req.body.body
                })
                .then((result)=>{
                    res.send(JSON.stringify({
                        body: req.body.body
                    }));
                })
            } else {
                res.send(JSON.stringify({
                    error: "you can edit only your own posts"
                }));
            }
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

router.route('/addComment').post(function(req, res) {
    db.collections.User.findOne({
        androidToken: req.body.androidToken
    })
    .then((data)=>{
        db.collections.Post.findByIdAndUpdate(req.body.id, {
            $push: {
                comments: {
                    username: data.username,
                    userId: data._id,
                    body: req.body.body
                }
            }
        })
        .then((result)=>{
            db.collections.Post.findById(req.body.id)
            .then((postData)=>{
                console.log(postData.comments);
                var posts = [];
                postData.comments.forEach(el => {
                    var temp = {};
                    temp.likes = el.likes.length;
                    temp.id = el._id;
                    temp.body = el.body;
                    temp.username = el.username;
                    temp.date = new Date(el.date).getTime();
                    temp.isLiked = el.likes.indexOf(data._id) > -1;
                    temp.isMine = el.userId == data._id;
                    posts.push(temp);
                });
                return res.send(JSON.stringify(posts))
            })
            
            
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

router.route('/deleteComment').post(function(req, res) {
    db.collections.User.findOne({
        androidToken: req.body.androidToken
    })
    .then((data)=>{
        db.collections.Post.findById(req.body.post_id)
        .then((result)=>{
            result.comments.forEach((el, index, object) => {
                if(el.id == req.body.comment_id){
                    object.splice(index, 1);
                }
            });
            db.collections.Post.findByIdAndUpdate(req.body.post_id, {
                comments: result.comments
            })
            .then((response)=>{
                db.collections.Post.findById(req.body.post_id)
                .then((shit)=>{
                    var posts = [];
                    shit.comments.forEach(el => {
                        var temp = {};
                        temp.likes = el.likes.length;
                        temp.id = el._id;
                        temp.body = el.body;
                        temp.username = el.username;
                        temp.date = new Date(el.date).getTime();
                        temp.isLiked = el.likes.indexOf(data._id) > -1;
                        temp.isMine = el.userId == data._id;
                        posts.push(temp);
                    });
                    return res.send(JSON.stringify(posts))
                })
                
            })
                
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


router.route('/sleepData').post(function(req, res) {
    db.collections.User.findOne({
        androidToken: req.body.androidToken
    })
    .then((data)=>{
        if(!data.gLink){
            return res.send(JSON.stringify({
                error: "not authorized in google"
            }))
        }
        db.collections.User.findById(data._id)
        .then((response)=>{
            var options = {
                uri: `https://www.googleapis.com/oauth2/v4/token`,
                method: "POST",
                json: {
                    client_id: config.GOOGLECLIENTID,
                    client_secret: config.GOOGLECLIENTSECRET,
                    refresh_token: response.secret_token,
                    grant_type: "refresh_token"
                }
            }
            request(options, (err, resp, body)=>{
                var options = {
                    uri: `https://www.googleapis.com/fitness/v1/users/me/dataset:aggregate?access_token=${body.access_token}`,
                    method: "POST",
                    json: {
                        "aggregateBy": [{
                            "dataSourceId": "raw:com.google.activity.segment:com.urbandroid.sleep:"
                        }],
                        "bucketByTime": { "durationMillis": 86400000 },
                        "startTimeMillis": new Date().getTime() - 864000000,
                        "endTimeMillis": new Date().getTime()
                    }
                }
                request(options, (err, resp, body)=>{
                    var bucketLength = body.bucket.length;

                    res.send(body.bucket);
                });
            });
        })
        // db.collections.Post.findOne({
        //     _id: req.body.id
        // })
        // .then((postData)=>{
        //     if(data._id == postData.userId){
        //         db.collections.Post.findByIdAndUpdate(postData._id, {
        //             body: req.body.body
        //         })
        //         .then((result)=>{
        //             res.sendStatus(200);
        //         })
        //     } else {
        //         res.sendStatus(403);
        //     }
        // })
        // .catch((err)=>{
        //     res.send(err);
        // })
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