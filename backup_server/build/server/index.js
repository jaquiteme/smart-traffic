const express = require('express');
const mongoose = require('mongoose');
const app = express();
const bodyParser = require('body-parser');
const fs = require('fs');
const cors = require('cors')
const API_END_POINT = "/central/api/store"

// credentials
const user = 'root';
const password = 'root';
const authMechanism = 'SCRAM-SHA-1';
const databaseAddr = "10.5.0.20"
// Database Name
const dbName = 'road_system';
const options = {
    user:"root",
    pass:"root",
    keepAlive: true,
    keepAliveInitialDelay: 300000,
    useNewUrlParser: true,
    useUnifiedTopology: true
};
// Connection URL
const url = `mongodb://${databaseAddr}:27017/${dbName}?authSource=admin`;
//CORS
app.use(cors())

app.use(bodyParser.urlencoded({
    extended: true
}));

app.use(bodyParser.json());

try {
    mongoose.connect(url, options);
} catch (error) {
    console.log("NOT ABLE TO CONNECT TO DATABASE");
}

// Import routes
let apiRoutes = require("./api-routing")

// Setup server port
var port = process.env.PORT || 8080;

// Use Api routes in the App
app.use('/api', apiRoutes)
//
app.listen(port, function () {
    console.log("Running Backup Server API on port " + port);
});
