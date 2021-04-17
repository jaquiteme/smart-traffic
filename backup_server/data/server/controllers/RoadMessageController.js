var RoadMessage = require('../models/RoadMessageModel');

// Find 
exports.index = async function (req, res) {
    let reponse = {}
    if (typeof req.query.lt_date !== 'undefined') {
        let lt_date = req.query.lt_date
        let gt_date = req.query.gt_date
        response = await RoadMessage.find({ created_at: { $gte: gt_date, $lte: lt_date } });
    } else {
        response = await RoadMessage.find({});
    }
    try {
        res.status(200).json({
            status: 200,
            message: "retrieving data ok",
            data: response == null ? [] : response
        });
    } catch (error) {
        res.status(500).json({
            status: 500,
            message: error
        });
    }
}

//Find Distinct CARS
exports.cars = async function (req, res) {
    try {
        const response = await RoadMessage.distinct("content.station-id")
        res.status(200).json({
            status: 200,
            message: "retrieving data ok",
            data: response == null ? [] : response
        });
    } catch (error) {
        res.status(500).json({
            status: 500,
            message: error
        });
    }
}

//Find Distinct gateways
exports.gateways = async function (req, res) {
    try {
        const response = await RoadMessage.distinct("gateway-id")
        res.status(200).json({
            status: 200,
            message: "retrieving data ok",
            data: response == null ? [] : response
        });
    } catch (error) {
        res.status(500).json({
            status: 500,
            message: error
        });
    }
}

// CREATE
exports.new = async function (req, res) {

    // Create User model Object
    var road_message = new RoadMessage();
    road_message['gateway-id'] = req.body['gateway-id'];
    road_message.created_at = req.body.created_at;
    road_message.tag = req.body.tag;
    road_message.content = req.body.content.cam;

    try {
        // save the user and check for errors
        console.log(req);
        const response = await road_message.save();
        res.status(200).json({
            status: 200,
            message: "New data added successfully!!",
            data: response
        });
    } catch (error) {
        res.status(500).json({
            status: 500,
            message: error
        });
    }
};