'use strict';

module.exports = function(app){
    var customer = require('../controllers/customerController');

    //customer Routes
    app.post('/customer/register', customer.registerCustomer);
    app.get('/customers', customer.getAllCustomers);
    app.get('/customer/:customerId', customer.getCustomer);
    app.put('/customer/:customerId',customer.updateCustomer)
    app.delete('/customer/:customerId', customer.deleteCustomer);
}