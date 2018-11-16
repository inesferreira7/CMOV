'use strict';

var mongoose = require('mongoose'),
    Performance = require('../models/performanceModel');

/*
 * Retrieves all performances.
 * If an error is encountered, sends a response with a 500 status code and the error description.
 * If no error is encountered, sends a response with a 200 status code and the performance list.
 */
exports.getAllPerformances = (req, res) => {
  Performance.find({}, (err, list) => {
    if (err) res.status(500).send(err);
    res.status(200).send(list);
  });
};

/*
 * Creates a new performance.
 * If an error is encountered, sends a response with a 500 status code and the error description.
 * If no error is encountered, sends a response with a 200 status code. 
 */
exports.createPerformance = (req, res) => {
    var performance = new Performance({
        name: req.body.name,
        date_string: req.body.date_string,
        ticketPrice: req.body.ticketPrice
    });

    performance.save((err) =>{
        if (err) res.status(500).send(err);
        res.status(200).send();
    });
};

/* 
 * Retrieves a performance with a certain ID.
 * If an error is encountered, sends a response with a 500 status code and the error description.
 * If no error is encountered, sends a response with a 200 status code and the found performance.
 */
exports.getPerformance = (req, res) => {
    Performance.findById(req.params.performanceId, (err, performance) => {
        if (err) res.status(500).send(err);
        res.status(200).send(performance);
    });
};

/* 
 * Updates a performance with a certain ID.
 * If an error is encountered, sends a response with a 500 status code and the error description.
 * If no error is encountered, sends a response with a 200 status code.
 */
exports.updatePerformance = (req, res) => {
    Performance.findOneAndUpdate({ _id: req.params.performanceId }, req.body, { new: true }, (err) => {
        if (err) res.status(500).send(err);
        res.status(200).send();
    });
}

/* 
 * Deletes a performance with a certain 
 * If an error is encountered, sends a response with a 500 status code and the error description.
 * If no error is encountered, sends a response with a 200 status code.
 */
exports.deletePerformance = (req, res) => {
    Performance.remove({ _id: req.params.performanceId }, (err) => {
        if (err) res.status(500).send(err);
        res.status(200).send();
    });
};