'use strict';

module.exports = function(app){
    var customer = require('../controllers/customerController');

    //customer Routes
    app.get('/customers', customer.getAllCustomers);
    app.post('/customer', customer.createCustomer);
    app.get('/customer/:customerId', customer.getCustomer);
    app.put('/customer/:customerId',customer.updateCustomer)
    app.delete('/customer/:customerId', customer.deleteCustomer);
}