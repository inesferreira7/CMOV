'use strict';

var mongoose = require('mongoose'),
    Customer = require('../models/customerModel');

/*
 * Retrieves all customers.
 * If an error is encountered, sends a response with a 500 status code and the error description.
 * If no error is encountered, sends a response with a 200 status code.
 */
exports.getAllCustomers = (req, res) => {
  Customer.find({}, (err) => {
    if (err) res.status(500).send(err);
    res.status(200).send();
  });
};

/*
 * Creates a new app customer.
 * If an error is encountered, sends a response with a 500 status code and the error description.
 * If no error is encountered, sends a response with a 200 status code and the UUID of the customer. 
 */
exports.registerCustomer = (req, res) => {
    var customer = new Customer({
        name: req.body.name,
        nif: req.body.nif,
        credit_card: req.body.credit_card
    });

    // On save, sends the UUID of the customer back to the app.
    customer.save((err, data) => {
        if (err) res.status(500).send(err);
        res.status(200).send(data._id);
    });
};

/* Retrieves a customer with a certain ID.
 * If an error is encountered, sends a response with a 500 status code and the error description.
 * If no error is encountered, sends a response with a 200 status code and the found customer.
 */
exports.getCustomer = (req, res) => {
    Customer.findById(req.params.customerId, (err, customer) => {
        if (err) res.status(500).send(err);
        res.status(200).send(customer);
    });
};

/* Updates a customer with a certain ID.
 * If an error is encountered, sends a response with a 500 status code and the error description.
 * If no error is encountered, sends a response with a 200 status code.
 */
exports.updateCustomer = (req, res) => {
    console.log(req.body);
    Customer.findOneAndUpdate({_id: req.params.customerId}, req.body, {returnNewDocument: false}, (err) => {
        if (err) res.status(500).send(err);
        res.status(200).send();
    });
}

/* Deletes a customer with a certain 
 * If an error is encountered, sends a response with a 500 status code and the error description.
 * If no error is encountered, sends a response with a 200 status code.
 */
exports.deleteCustomer = (req, res) => {
    Customer.remove({ _id: req.params.customerId }, (err) => {
        if (err) res.status(500).send(err);
        res.status(200).send();
    })
}