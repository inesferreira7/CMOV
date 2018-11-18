'use strict';

var mongoose = require('mongoose'),
    Order = require('../models/orderModel');

exports.getAllOrders = (req, res) => {
    var query = {customerId: req.params.customerId}

    Order.find(query, (err, list) => {
        if (err) res.status(500).send(err);
        res.status(200).send(list);
    });
};

exports.getOrder = (req, res) => {
    Order.findById(req.params.orderId, (err, order) => {
        if (err) res.status(500).send(err);
        res.status(200).send(order);
    });
};

exports.createOrder = (req, res) => {
    var order = new Order({
        customerId: req.body.customerId,
        products: req.body.products,
        vouchers: req.body.vouchers,
        totalPrice: req.body.totalPrice
    });

    order.save((err) => {
        if (err) res.status(500).send(err);
        res.status(200).send();
    });
};