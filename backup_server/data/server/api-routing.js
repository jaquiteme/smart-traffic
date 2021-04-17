// Initialize express router
let router = require('express').Router();

// Set default API response
router.get('/', function (req, res) {
    res.json({
        status: '200',
        message: 'Welcome to NodeJS, Road Sytem Storage Api',
    });
});

var RoadMessageController = require('./controllers/RoadMessageController');
var AlertEventController = require('./controllers/AlertEventController');
// Road messages routes
router.route('/road_messages')
    .get(RoadMessageController.index)
    .post(RoadMessageController.new)

router.route('/road_messages/cars')
    .get(RoadMessageController.cars)

router.route('/road_messages/gateways')
    .get(RoadMessageController.gateways)
// router.route('/road_message/:id')
//     .get(RoadMessageController.find_between_dates)

// Road messages routes
router.route('/alert_events')
    .get(AlertEventController.index)
    .post(AlertEventController.new)

    // Road messages routes
router.route('/alert_events/stats')
.get(AlertEventController.stat_btw_events_by_date)

// Export API routes
module.exports = router;