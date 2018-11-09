'use strict';

var mongoose = require('mongoose'),
    Ticket = require('../models/ticketModel'),
    Performance = require('../models/performanceModel');

// Creates a new ticket and decreases number of performance available tickets
exports.buyTickets = (req, res) => {
    var ticket = new Ticket({
        performance: req.body.performance,
        customer: req.body.customer,
        isUsed: false
    });

    if (req.body.quantity != 0 && req.body.quantity != null) {
        console.log(req.body.quantity);
        ticket.save()
        .then(data => {
            for(var n = 0; n < req.body.quantity; n++) {
                res.send(data);
            }
        }).then(data => {
            Performance.findOneAndUpdate({ _id : ticket.performance }, {$inc: {nTickets: -1 * ticket.quantity}}, {new: true}, (err, performance) => {
                if (err) res.send(err);
                res.json({message: 'Transaction complete'});
            });
            res.send(data);
        })
        .catch(err => {
            res.send(err);
        });
    } else {
        res.send({message: 'Transaction failed: You need to buy at least 1 ticket'});
    }
};

// Retrieves customer tickets
exports.getCustomerTickets = (req, res) => {
    var query = {customer: req.params.customerId}
    
    Ticket.find(query, (err, tickets) => {
        if (err) res.send(err);
        res.send(tickets);
    });
}

// Deletes a ticket
exports.deleteTicket = (req, res) => {
    Ticket.remove({ _id: req.params.ticketId }, (err, ticket) => {
        if (err) res.send(err);
        res.send({message: 'Ticket successfully deleted'});
    })
}

exports.validateTicket = (req, res) => {
    var query = {
        _id: req.params.ticketId,
        customer: req.params.customerId,
        isUsed: false
    };

    Ticket.findOneAndUpdate(query, {$set: {isUsed: true}}, {returnNewDocument: true}, (err, result) => {
        if (err) res.send(err);
        if (result != null) res.send({message: 'The ticket is now valid'});
        else res.send({message: 'The ticket is already validated'});
    });
}