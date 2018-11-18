'use strict';

var mongoose = require('mongoose'),
    Voucher = require('../models/voucherModel');

exports.getAllVouchers = (req, res) => {
    var query = {customerId: req.params.customerId}

    Voucher.find(query, (err, list) => {
        if (err) res.status(500).send(err);
        res.status(200).send(list);
    });
};

exports.getVoucher = (req, res) => {
    Voucher.findById(req.body.voucherId, (err, voucher) => {
        if (err) res.status(500).send(err);
        res.status(200).send(voucher);
    });
};

exports.getAllUnusedVouchers = (req, res) => {
    var query = {
        customerId: req.params.customerId,
        isUsed: false
    }

    Voucher.find(query, (err, list) => {
        if (err) res.status(500).send(err);
        res.status(200).send(list.json());
    });
};

exports.createVoucher = (req, res) => {
    var voucher = new Voucher({
        customerId: req.body.customerId,
        type: req.body.type
    });

    voucher.save((err, data) => {
        if (err) res.status(500).send(err);
        res.status(200).send(data);
    });
};