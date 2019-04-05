var crypto          = require("crypto"),
    express         = require("express"),
    request         = require("request"),
    mongoose        = require("mongoose"),
    passport        = require("passport"),
    bodyParser      = require("body-parser"),
    LocalStrategy   = require("passport-local"),
    mOverride       = require("method-override"),
    GoogleStrategy  = require("passport-google-oauth20"),
    randomstring    = require("randomstring");


const config = require("./root/config.json");
const app = express();
const db = require("./models");
var myRoutes = require('./routes');


app.use(express.static('public'));
app.set("view engine", "ejs");
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());
app.use(require("express-session")({
    secret: config.SESSIONSECRET,
    resave: false,
    saveUninitialized: false
}));
app.use(mOverride("_method"));

app.use('/api', myRoutes);



//passport part
app.use(passport.initialize());
app.use(passport.session());
passport.use(new GoogleStrategy({
    clientID: config.GOOGLECLIENTID,
    clientSecret: config.GOOGLECLIENTSECRET,
    callbackURL: "/return"
  }, function(accessToken, refreshToken, profile, cb) {
      console.log("IM IN");
   
    return cb(null, {
        refreshToken,
        profile
    });
}));
passport.use(new LocalStrategy(
    function(username, password, done) {
        db.collections.User.findOne({ username: username }, function (err, user) {
          if (err) { return done(err); }
          if (!user) { return done(null, false); }
          if (hash(password)!=user.local.password) { return done(null, false); }
          return done(null, user);
        });
    }
));
passport.serializeUser(function(user, cb) {
    cb(null, user);
});
passport.deserializeUser(function(obj, cb) {
    cb(null, obj);
});
//

//middleware
app.use((req, res, next)=>{
    res.locals.currentUserDisplayName = "";
    res.locals.currentUserId = "";
    if(req.user){
        db.collections.User.findById(req.user._id)
        .then((data)=>{
            res.locals.currentUserDisplayName = data.displayName;
            res.locals.currentUserId = data._id;
            return next();
        })
        .catch((err)=>{
            res.send(err);
        })
    } else {
        return next();
    }
});

app.get("/", (req, res)=>{
    db.collections.Post.find()
    .then((postData)=>{
        res.render("home", {postData: postData});
    })
    .catch((err)=>{
        res.send(err);
    })
});
app.post("/toggleLike", isLoggedIn, hasUsernameFetch, (req, res)=>{
    db.collections.Post.findById(req.body.id)
    .then((data)=>{
        var ammountOfLikes = data.likes.length;
        if(data.likes.includes(req.user._id)){
            db.collections.Post.findByIdAndUpdate(req.body.id, {
                $pull: {
                    likes: req.user._id
                }
            })
            .then((result)=>{
                res.send(JSON.stringify({"color":"black", "ammount": --ammountOfLikes}));
            })
        } else {
            db.collections.Post.findByIdAndUpdate(req.body.id, {
                $push: {
                    likes: req.user._id
                }
            })
            .then((result)=>{
                res.send(JSON.stringify({"color":"red", "ammount": ++ammountOfLikes}));
            })
        }
    })
    .catch((err)=>{})
})
app.get("/login", (req, res)=>{
    res.render("login_local");
});
app.post("/login", passport.authenticate("local", {successRedirect: "/"}), (req, res)=>{
    console.log("oh, hello")
});
app.get("/GLink", passport.authenticate("google", {
    scope: ["https://www.googleapis.com/auth/fitness.activity.read", "profile" ],
    accessType: 'offline',
    prompt: 'consent'
}));
app.get("/return", function(req, res, next) {

    passport.authenticate('google', function(err, user, info) {
        db.collections.User.findById(req.user._id, function (err, data) {
            if(data.gLink){
                console.log("already logged in, faggot")
                return res.redirect("/");
            } else {
                db.collections.User.findByIdAndUpdate(data._id, {
                    gLink: true,
                    ["google.id"]: user.profile.id,
                    secret_token: user.refreshToken
                })
                .then((data)=>{
                    if(data){
                        return res.redirect("/");
                    }
                })
            }
        });
    })(req, res, next);
  });
app.get("/info", isLoggedIn, updateToken, (req, res)=>{
    db.collections.User.findById(req.user._id)
    .then((data)=>{
        var options = {
            uri: `https://www.googleapis.com/fitness/v1/users/me/dataset:aggregate?access_token=${data.token}`,
            method: "POST",
            json: {
                "aggregateBy": [{
                    "dataSourceId": "raw:com.google.activity.segment:com.urbandroid.sleep:"
                }],
                "bucketByTime": { "durationMillis": 86400000 },
                "startTimeMillis": new Date().getTime() - 86400000*10,
                "endTimeMillis": new Date().getTime()
            }
        }
        request(options, (err, resp, body)=>{
            res.send(body.bucket);
        });
    });
});
// app.get("/getToken", isLoggedIn, (req, res)=>{
//     db.collections.User.findOne({
//         username: req.user.username
//     })
//     .then((data)=>{
//         var options = {
//             uri: `https://www.googleapis.com/oauth2/v4/token`,
//             method: "POST",
//             json: {
//                 client_id: config.GOOGLECLIENTID,
//                 client_secret: config.GOOGLECLIENTSECRET,
//                 refresh_token: data.secret_token,
//                 grant_type: "refresh_token"
//             }
//         }
//         request(options, (err, resp, body)=>{
//             res.send(body.access_token);
//         });
//     })
   
// })
app.get("/register", (req, res)=>{
    res.render("register");
});
app.post("/register", (req, res)=>{
    db.collections.User.create({
        username: req.body.username,
        ["local.password"]: hash(req.body.password),
        displayName: req.body.name,
        androidToken: randomstring.generate(20)
    })
    .then((data)=>{
        passport.authenticate("local")(req, res, ()=>{
            res.redirect("/");
        });
    })
    .catch((err)=>{})
});
app.get("/logout", (req, res)=>{
    req.logout();
    res.redirect("/");
});
 
app.get("/new", isLoggedIn, hasUsername, (req, res)=>{
    res.render("newPost");
})
app.post("/new", isLoggedIn, hasUsername, (req, res)=>{
    db.collections.User.findById(req.user._id)
    .then((data)=>{
        db.collections.Post.create({
            userId: data.id,
            username: data.username,
            body: req.body.post_body,
            displayName: data.displayName || ""
        })
        .then((result)=>{
            res.redirect(`/post/${result.id}`);
        })
    })
    .catch((err)=>{})
});
app.get("/profile/me", isLoggedIn, (req, res)=>{
    db.collections.User.findById(req.user._id)
    .then((data)=>{
        res.render("myProfile", {userData: data});
    })
});
app.put("/profile/me", isLoggedIn, (req, res)=>{
    db.collections.User.findByIdAndUpdate(req.user._id, {
        username: req.body.username,
        displayName: req.body.display_name
    })
    .then((data)=>{
        db.collections.Post.updateMany({userId: data._id}, {
            $set: {username: req.body.username, displayName: req.body.display_name} 
        })
        .then((updated)=>{
            res.redirect("/profile/me");
        })
    })
});
app.get("/post/:id", isLoggedIn, (req, res)=>{
    db.collections.Post.findByIdAndUpdate(req.params.id, {
        $addToSet: {viewers: req.user._id},
        $inc: {views: 1}
    })
    .then((data)=>{
        res.render("show", {postData: data, myPost: data.userId == req.user._id});
    })
});
app.get("/post/:id/delete", isLoggedIn, isMyPost, (req, res)=>{
    db.collections.Post.findByIdAndDelete(req.params.id, (err, data)=>{
        res.redirect("/");
    });
});
app.get("/post/:id/edit", isLoggedIn, isMyPost, (req, res)=>{
    db.collections.Post.findById(req.params.id)
    .then((data)=>{
        res.render("edit", {postData: data});
    })
});
app.put("/post/:id/edit", isLoggedIn, isMyPost, (req, res)=>{
    db.collections.Post.findByIdAndUpdate(req.params.id, {
        post: req.body.post_body
    })
    .then((data)=>{
        res.redirect(`/post/${req.params.id}`);
    })
});
function isLoggedIn(req, res, next){
    if(req.user){
        next();
    } else {
        return res.redirect("/login");
    }
}
function hasUsername(req, res, next){
    db.collections.User.findById(req.user._id)
    .then((data)=>{
        if(data.username) return next();
        res.redirect("/profile/me");
    })
    .catch(()=>{})
}
function isMyPost(req, res, next){
    console.log(req.params.id);
    db.collections.Post.findById(req.params.id, (err, data)=>{
        console.log(data);
        if(data.userId == req.user._id){
            next();
        } else {
            res.redirect("/");
        }
    });
}
function hasUsernameFetch(req, res, next){
    db.collections.User.findById(req.user._id)
    .then((user)=>{
        if(user.username){
            next()
        } else {
            return res.send(JSON.stringify({"hasUsername":"nope"}));
        }
    });
}
function hash(password){
    return crypto.createHmac('sha256', config.HASHSECRET).update(password).digest('hex');
}
function updateToken(req, res, next){
    db.collections.User.findById(req.user._id)
    .then((data)=>{
        var options = {
            uri: `https://www.googleapis.com/oauth2/v4/token`,
            method: "POST",
            json: {
                client_id: config.GOOGLECLIENTID,
                client_secret: config.GOOGLECLIENTSECRET,
                refresh_token: data.secret_token,
                grant_type: "refresh_token"
            }
        }
        request(options, (err, resp, body)=>{
            db.collections.User.findByIdAndUpdate(data._id, {
                token: body.access_token
            }, (err, data)=>{
                next()
            })
        });
    })
}

app.get("/qqq", (req, res)=>{
    res.render("shit");
});

app.listen(3000, "0.0.0.0");