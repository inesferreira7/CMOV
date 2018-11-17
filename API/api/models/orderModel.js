'use strict';

var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var OrderSchema = new Schema({
    customerId: {
        type: Schema.Types.ObjectId,
        required: true
    },
    products: [ 
        {
            type: String,
            enum: ['Coffee', 'Soda', 'Sandwich', "Popcorn"],
            required: true
        }
    ],
    vouchers: [
        {
            type: Schema.Types.ObjectId,
            validate: [
                arrayLimit,
                '{PATH} exceeds the limit of 2 vouchers'
            ]
        }
    ],
    totalPrice: {
        type: Number,
        required: true
    }
});

function arrayLimit(val) {
    return val.length <= 2;
}

module.exports = mongoose.model('Order', OrderSchema);