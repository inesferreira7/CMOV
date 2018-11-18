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
    const quantity = req.body[0].quantity;

    for (var i = 0; i < quantity; i++) {
        var ticket = new Ticket({
            performanceName: req.body[0].performanceName,
            performanceDate: req.body[0].performanceDate,
            performanceId: req.body[0].performanceId,
            customerId: req.body[0].customerId,
            seat: req.body[0].seat,
            isUsed: false
        });

        tickets.push(ticket);
    }

    Ticket.collection.insertMany(tickets, (err, data) => {
        if(err) res.status(500).send(err);
        res.status(200).send(tickets);
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
    console.log("Begin ticket validation...") 

    var elements = req.body[0].ids.substring(1, req.body[0].ids.length-1);
    var elements_array = elements.split(", ");
    var query = {
        _id: {
            $in: elements_array
        },
        customerId: req.body[0].customerId
    }

    Ticket.find(query, (err, result) => {
        if (err){
            res.status(500).send(err);
        } else{
            for(var i = 0; i < result.length; i++) {
                if (!result[i].isUsed){
                    result[i].isUsed = true;
                    result[i].save(err => {
                        if(err) res.status(500).send(err);
                    });
                } 
            }
            console.log(JSON.stringify(result));
            res.status(200).send(JSON.stringify(result));
        }
    })
}