'use strict';

module.exports = function(app){
    var customer = require('../controllers/customerController');

    //customer Routes
    app.get('/customers', customer.listAllCustomers);
    app.post('/customers', customer.createCustomer);

    app.get('/customers/:customerId', customer.getCustomer);
    app.put('/customers/:customerId',customer.updateCustomer)
    app.delete('/customers/:customerId', customer.deleteCustomer);
}