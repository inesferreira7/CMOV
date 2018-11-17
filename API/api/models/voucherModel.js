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
        required: true,
        default: false
    },
    description: {
        type: String,
        required: true
    }
});

module.exports = mongoose.model('Voucher', VoucherSchema);