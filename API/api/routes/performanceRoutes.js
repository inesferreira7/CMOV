'use strict';

module.exports = function(app){
    var performance = require('../controllers/performanceController');

    //Performance Routes
    app.get('/performances', performance.getAllPerformances);
    app.post('/performance', performance.createPerformance);
    app.get('/performance/:performanceId', performance.getPerformance);
    app.put('/performance/:performanceId', performance.updatePerformance);
    app.delete('/performance/:performanceId', performance.deletePerformance);
}