'use strict';

var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var CustomerSchema = new Schema({
    public_key : {
        type: String,
        required: true
    },
    name: {
        type: String,
        required: true
    },
    nif: {
        type: Number,
        required: true
    },
    credit_card: {
        card_type:{
            type: String,
            required: true
        },
        card_number: {
            type: Number,
            required: true
        },
        validity: {
            type: String,
            required: true
        }
    }
});

module.exports = mongoose.model('Customer', CustomerSchema);