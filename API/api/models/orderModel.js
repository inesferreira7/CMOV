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
                type: Schema.Types.ObjectId
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
module.exports = mongoose.model('Order', OrderSchema);