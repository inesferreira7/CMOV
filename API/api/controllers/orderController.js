'use strict';

var mongoose = require('mongoose'),
    Order = require('../models/orderModel'),
    Voucher = require('../models/voucherModel');

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

exports.validateOrder = (req, res) => {
    var query = {
        _id: req.body.id,
        customerId: req.body.customerId,
        validated: req.body.validated
    }

    var voucherIds = new Array();

    for(var i = 0; i < req.body.vouchers.length; i++){
        voucherIds.push(req.body.vouchers[i].voucherId)
    }

    Order.find(query, (err, result) => {
        if (err) res.status(500).send(err);
        else {
            var queryV = {
                _id: {
                    $in: voucherIds
                }
            }
            Voucher.find(queryV, (err, resultV) => {
                if (err) res.status(500).send(err);
                else{
                    var percentage = false;

                    for (var i = 0; i < resultV.length; i++) {
                        if (!resultV[i].isUsed){
                            resultV[i].isUsed = true;

                            if(resultV[i].type == 'Coffee'){
                                result[0].totalPrice -= 1;
                            } else if (resultV[i].type == 'Popcorn'){
                                result[0].totalPrice -= 2;
                            } else if (resultV[i].type == '5%'){
                                percentage = true;
                            }

                            resultV[i].save(err => {
                                if (err) res.status(500).send(err);
                            });  
                        }
                    }
                    if(percentage) result[0].totalPrice -= (result[0].totalPrice * 0.05);

                    result[0].validated = true;
                    result[0].save(err => {
                        if (err) res.status(500).send(err);
                    });

                    res.status(200).send();
                }
            });
        }
    });
}