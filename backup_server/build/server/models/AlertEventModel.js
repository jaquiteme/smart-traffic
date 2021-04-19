const mongoose = require('mongoose');
//[SCHEMA]
var Position = mongoose.Schema({
    latitude: Number,
    longitude: Number,
})

var DENM = mongoose.Schema({
    'station-id': String,
    'cause-code': Number,
    'sub-cause-code': Number,
    'station-type': Number,
    position: Position
})

var AlertEvent = mongoose.Schema({
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
        type: [DENM]
    }
})

var AlertEvent = module.exports = mongoose.model('alert_event', AlertEvent);

module.exports.get = function (callback, limit) {
    AlertEvent.find(callback).limit(limit);
}