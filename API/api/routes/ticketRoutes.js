'use strict';

module.exports = function(app){
    var ticket = require('../controllers/ticketController');

    // Ticket Routes
    app.get('/tickets/customer/:customerId', ticket.getCustomerTickets);
    app.post('/tickets/validate', ticket.validateTicket);
    app.post('/tickets/buy', ticket.buyTickets);
    app.delete('/ticket/:tickedId', ticket.deleteTicket);
}