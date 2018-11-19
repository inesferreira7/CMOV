'use strict';

module.exports = function(app){
    var order = require('../controllers/orderController');

    //Order Routes
    app.get('/orders/customer/:customerId', order.getAllOrders);
    app.get('/order', order.getOrder);
    app.post('/order', order.createOrder);
    app.post('/order/validate', order.validateOrder);
}