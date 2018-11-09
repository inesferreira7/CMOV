'use strict';

var mongoose = require('mongoose'),
    Performance = require('../models/performanceModel');

// Lists all app performances
exports.getAllPerformances = (req, res) => {
  Performance.find({}, (err, customer) => {
    if (err) res.send(err);
    res.json(customer);
  });
};

// Creates a new performance
exports.createPerformance = (req, res) => {
    var performance = new Performance({
        name: req.body.name,
        date: req.body.date,
        nTickets: req.body.nTickets,
        ticketPrice: req.body.ticketPrice
    });

    performance.save()
    .then(data => {
        res.send(data);
    })
    .catch(err => {
        res.send(err);
    });
};

// Retrieves a performance with a certain ID
exports.getPerformance = (req, res) => {
    Performance.findById(req.params.performanceId, (err, performance) => {
        if (err) res.send(err);
        res.json(performance);
    });
};

// Updates a performance with a certain ID
exports.updatePerformance = (req, res) => {
    Performance.findOneAndUpdate({ _id: req.params.performanceId }, req.body, { new: true }, (err, performance) => {
        if (err) res.send(err);
        res.json(performance);
    });
}

// Deletes a performance with a certain ID
exports.deletePerformance = (req, res) => {
    Performance.remove({ _id: req.params.performanceId }, (err, performance) => {
        if (err) res.send(err);
        res.jseon({ message: 'Performance successfully deleted' })
    });
};