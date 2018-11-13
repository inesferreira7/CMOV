'use strict';

var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var PerformanceSchema = new Schema({
    name: {
        type: String,
        required: true
    },
    date: {
        type: Date,
        default: Date.now,
    },
    ticketPrice: {
        type: Number,
        required: true
    }
});

module.exports = mongoose.model('Performance', PerformanceSchema);