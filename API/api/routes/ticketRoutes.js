'use strict';

module.exports = function(app){
    var ticket = require('../controllers/ticketController');

    //Ticket Routes
    app.post('/tickets/buy', ticket.buyTickets);
    app.get('/tickets/customer/:customerId', ticket.getCustomerTickets);
    app.delete('/ticket/:tickedId', ticket.deleteTicket);
    app.get('/validate/ticket/:ticketId/customer/:customerId', ticket.validateTicket);
}