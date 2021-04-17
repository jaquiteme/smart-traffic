const mongoose = require('mongoose');
//[SCHEMA]

var Position = mongoose.Schema({
    latitude: Number,
    longitude: Number,
})

var CAM = mongoose.Schema({
    'station-id': String,
    heading: Number,
    'station-type': Number,
    position: Position
})

var RoadMessage = mongoose.Schema({
    id: {
        type: String
    },
    'gateway-id': {
        type: String
    },
    created_at: {
        type: Number
    },
    tag: {
        type: Number
    },
    content: {
        type: CAM
    }
})

var RoadMessage = module.exports = mongoose.model('road_message', RoadMessage);

module.exports.get = function (callback, limit) {
    RoadMessage.find(callback).limit(limit);
}