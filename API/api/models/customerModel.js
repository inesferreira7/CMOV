'use strict';

var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var CustomerSchema = new Schema({
    name: {
        type: String
    },
    nif: {
        type: Number
    },
    credit_card: {
        card_type:{
            type: String
        },
        card_number: {
            type: Number
        },
        validity: {
            type: String
        }
    }
});

module.exports = mongoose.model('Customer', CustomerSchema);