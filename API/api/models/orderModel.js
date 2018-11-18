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
            _id: false,
            product: { 
                _id: false,
                type: String,
                enum: ['Coffee', 'Soda', 'Sandwich', "Popcorn"],
                required: true
            },
            quantity: {
                _id: false,
                type: Number,
                required: true
            }
        }
    ],
    vouchers: [
        {
            voucherId: {
                type: Schema.Types.ObjectId,
                validate: [
                    arrayLimit,
                    '{PATH} exceeds the limit of 2 vouchers'
                ]
            }
        }
    ],
    totalPrice: {
        type: Number,
        required: true
    },
    validated: {
        type: Boolean,
        default: false
    }
});

function arrayLimit(val) {
    return val.length <= 2;
}

module.exports = mongoose.model('Order', OrderSchema);