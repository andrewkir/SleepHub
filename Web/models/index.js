var mongoose = require("mongoose");
const config = require("../root/config.json");

mongoose.connect(process.env.DATABASEURL || "mongodb://localhost/sleep" || config.DATABASEURL, {useNewUrlParser: true});
mongoose.set('useFindAndModify', false);
mongoose.Promise = Promise;

module.exports.collections = {
    User: require("./user"),
    Post: require("./post")
}