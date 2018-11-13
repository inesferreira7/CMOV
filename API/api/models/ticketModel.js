'use strict';

var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var TicketSchema = new Schema({
    performanceName: {
        type: String,
        required: true
    },
    performanceDate: {
        type: String,
        required: true
    },
    performanceId: {
        type: Schema.Types.ObjectId,
        required: true
    },
    customerId: {
        type: Schema.Types.ObjectId,
        required: true
    },
    seat: {
        type: String,
        required: true
    },
    isUsed: {
        type: Boolean,
        default: false
    }
});

module.exports = mongoose.model('Ticket', TicketSchema);