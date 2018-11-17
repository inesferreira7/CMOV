'use strict';

module.exports = function(app){
    var voucher = require('../controllers/voucherController');

    //Voucher Routes
    app.get('/vouchers', voucher.getAllVouchers);
    app.get('/voucher', voucher.getVoucher);
    app.get('/voucher', voucher.getAllUnusedVouchers);
    app.post('/voucher', voucher.createVoucher);
}