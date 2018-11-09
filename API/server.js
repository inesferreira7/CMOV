var express = require('express'),
  app = express(),
  port = process.env.PORT || 3000,
  mongoose = require('mongoose'),
  Customer = require('./api/models/customerModel'),
  bodyParser = require('body-parser');


// mongoose instance connection url connection
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost/cmovDB', {useNewUrlParser: true });

app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());

require('./api/routes/customerRoutes')(app);
require('./api/routes/performanceRoutes')(app);
require('./api/routes/ticketRoutes')(app);

app.listen(port);

console.log('CMOV RESTful API server started on: ' + port);