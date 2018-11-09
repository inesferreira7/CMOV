'use strict';

var mongoose = require('mongoose'),
    Customer = require('../models/customerModel');

// Lists all app customers
exports.getAllCustomers = (req, res) => {
  Customer.find({}, (err, customer) => {
    if (err) res.send(err);
    res.json(customer);
  });
};

// Creates a new app customer
exports.registerCustomer = (req, res) => {
    var customer = new Customer({
        name: req.body.name,
        nif: req.body.nif,
        credit_card: req.body.credit_card
    });

    customer.save()
    .then(data => {
        res.send(data);
    })
    .catch(err => {
        res.send(err);
    });
};

// Retrieves a customer with a certain ID
exports.getCustomer = (req, res) => {
    Customer.findById(req.params.customerId, (err, customer) => {
        if (err) res.send(err);
        res.json(customer);
    });
};

// Updates a customer with a certain ID
exports.updateCustomer = (req, res) => {
    Customer.findOneAndUpdate({_id: req.params.customerId}, req.body, {new: true}, (err , customer) => {
        if (err) res.send(err);
        res.json(customer);
    });
}

// Deletes a customer with a certain ID
exports.deleteCustomer = (req, res) => {
    Customer.remove({ _id: req.params.customerId }, (err, customer) => {
        if (err) res.send(err);
        res.json({message: 'Customer successfully deleted'});
    })
}