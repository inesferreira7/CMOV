'use strict';

var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var TicketSchema = new Schema({
    performance: {
        type: Schema.Types.ObjectId,
        required: true
    },
    customer: {
        type: Schema.Types.ObjectId,
        required: true
    },
    isUsed: {
        type: Boolean,
        default: false
    }
});

module.exports = mongoose.model('Ticket', TicketSchema);