'use strict';

var mongoose = require('mongoose'),
    Order = require('../models/orderModel');

exports.getAllOrders = (req, res) => {
    var query = {customerId: req.params.customerId}

    Order.find(query, (err, list) => {
        if (err) res.status(500).send(err);
        res.status(200).send(list.json());
    });
};

exports.getOrder = (req, res) => {
    Order.findById(req.params.orderId, (err, order) => {
        if (err) res.status(500).send(err);
        res.status(200).send(order);
    });
};

exports.createOrder = (req, res) => {
    var order = {
        customerId: req.params.customerId,
        products: req.params.products,
        vouchers: req.params.vouchers,
        totalPrice: req.params.totalPrice
    }

    order.save((err, data) => {
        if (err) res.status(500).send(err);
        res.status(200).send();
    });
};