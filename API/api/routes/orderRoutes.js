'use strict';

module.exports = function(app){
    var order = require('../controllers/orderController');

    //Order Routes
    app.get('/orders', order.getAllOrders);
    app.get('/order', order.getOrder);
    app.post('/order', order.createOrder);
}