var express     = require('express'),
    router      = express.Router(),
    bodyParser  = require("body-parser"),
    mongoose    = require("mongoose");

const app = express();
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());
const db = require("./models");
var myRoutes = require('./routes');

router.use(function(req, res, next) {
  next();
});

router.route('/register')
.get(function(req, res) {
    db.collections.User.findById(req.user._id)
    .then((data)=>{
        db.collections.Post.create({
            userId: data.id,
            username: data.username,
            post: req.body.post_body,
            displayName: data.displayName || ""
        })
        .then((result)=>{
            res.redirect(`/post/${result.id}`);
        })
    })
    .catch((err)=>{})
});


module.exports = router;