'use strict';

module.exports = function(app){
    var performance = require('../controllers/performanceController');

    //Performance Routes
    app.get('/performances', performance.getAllPerformances);
    app.get('/performance/:performanceId', performance.getPerformance);
    app.post('/performance', performance.createPerformance);
    app.put('/performance/:performanceId', performance.updatePerformance);
    app.delete('/performance/:performanceId', performance.deletePerformance);
}