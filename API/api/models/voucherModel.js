'use strict';

var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var VoucherSchema = new Schema({
    customerId: {
        type: Schema.Types.ObjectId,
        required: true
    },
    isUsed: {
        type: Boolean,
        default: false
    },
    type: {
        type: String,
        enum: ['Coffee', 'Soda', 'Sandwich', "Popcorn", "5%"],
        required: true
    }
});

module.exports = mongoose.model('Voucher', VoucherSchema);