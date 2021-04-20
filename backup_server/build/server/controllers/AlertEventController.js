var AlertEvent = require('../models/AlertEventModel');

// Find 
exports.index = async function (req, res) {
    let response = {}
    if (typeof req.query.lt_date !== 'undefined') {
        let lt_date = parseInt(req.query.lt_date)
        let gt_date = parseInt(req.query.gt_date)
        response = await AlertEvent.find({ created_at: { $gte: gt_date, $lte: lt_date } });
    } else {
        response = await AlertEvent.find({});
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

// Find by date
exports.find_between_dates = async function (req, res) {
    try {
        let gt_date = parseInt(req.query.gt_date)
        let lt_date = parseInt(req.query.lt_date)
        const response = await AlertEvent.find({ created_at: { $gt: gt_date, $lt: lt_date } });
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

// Get accident and jams stat by date 
exports.stat_btw_events_by_date = async function (req, res) {
    try {
        var gt_date = parseInt(req.query.gt_date)
        var lt_date = parseInt(req.query.lt_date)
        const response = await AlertEvent.aggregate([
            {
                $match: {
                    created_at: { $gte: gt_date, $lte: lt_date }
                },
            },
            {
                $group: {
                    _id: {
                        created_at: { $dateToString: { format: "%Y-%m-%d %H:%M", date: { $convert: { input: "$created_at", to: "date" } } } },
                        tag: "$tag"
                    },
                    count: { $sum: 1 }
                }
            }, {
                $group: {
                    _id: { tag: "$_id.tag" },
                    stats: { $addToSet: { created_at: "$_id.created_at", count: "$count" } }
                }
            }
        ]);
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

    // model Alert
    var alert_event = new AlertEvent();
    alert_event['gateway-id'] = req.body['gateway-id'];
    alert_event.created_at = req.body.created_at;
    alert_event.tag = req.body.tag;
    alert_event.content = req.body.content.denm;

    try {
        const response = await alert_event.save();
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