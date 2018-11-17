'use strict';

var mongoose = require('mongoose'),
    Ticket = require('../models/ticketModel'),
    Performance = require('../models/performanceModel');

/*
 * Creates a new ticket and decreases number of performance available tickets
 * If an error is encountered, sends a response with a 500 status code and the error description.
 * If no error is encountered, decreases number of performance available tickets then sends a response with a 200 status code.
 */
exports.buyTickets = (req, res) => {
    var tickets = new Array();
    const quantity = req.body.quantity;

    for (var i = 0; i < quantity; i++) {
        var ticket = new Ticket({
            performanceName: req.body.performanceName,
            performanceDate: req.body.performanceDate,
            performanceId: req.body.performanceId,
            customerId: req.body.customerId,
            seat: req.body.seat,
            isUsed: false
        });

        tickets.push(ticket);
    }

    Ticket.collection.insertMany(tickets, (err, data) => {
        if(err) res.status(500).send(err);
        res.status(200).send({message: "done"});
    });
};

/* 
 * Retrieves tickets from a customer with a certain ID.
 * If an error is encountered, sends a response with a 500 status code and the error description.
 * If no error is encountered, sends a response with a 200 status code and the list of the tickets.
 */
exports.getCustomerTickets = (req, res) => {
    var query = {customerId: req.params.customerId}
    
    Ticket.find(query, (err, tickets) => {
        if (err) res.status(500).send(err);
        res.status(200).send(tickets);
    });
}

/* 
 * Deletes a ticket with a certain ID.
 * If an error is encountered, sends a response with a 500 status code and the error description.
 * If no error is encountered, sends a response with a 200 status code.
 */
exports.deleteTicket = (req, res) => {
    Ticket.remove({ _id: req.params.ticketId }, (err) => {
        if (err) res.status(500).send(err);
        res.status(200).send();
    })
}

/* 
 * Validates a ticket from a customer.
 * If an error is encountered, sends a response with a 500 status code and the error description.
 * If no error is encountered, sends a response with a 200 status code and updates the used status of the ticket.
 */
exports.validateTicket = (req, res) => {
    var query = {
        _id: req.params.ticketId,
        customerId: req.params.customerId,
        isUsed: false
    };

    Ticket.findOneAndUpdate(query, {$set: {isUsed: true}}, {returnNewDocument: true}, (err, result) => {
        if (err) res.status(500).send(err);
        if (result != null) res.status(200).send({message: 'The ticket is now valid'});
        else res.status(304).send({message: 'The ticket is already validated'});
    });
}