var express     = require('express'),
    router      = express.Router(),
    mongoose    = require("mongoose"),
    randomstring    = require("randomstring");

const db = require("./models");
var myRoutes = require('./routes');

router.use(function(req, res, next) {
  next();
});

router.route('/register').post(function(req, res) {
    db.collections.User.create({
        username: req.body.username,
        ["local.password"]: req.body.password,
        displayName: req.body.name,
        androidToken: randomstring.generate(20)
    })
    .then((data)=>{
        response = {
            statusCode: 200,
            androidToken: data.androidToken
        }
        res.send(JSON.stringify(response));
    })
    .catch((err)=>{})
});


module.exports = router;