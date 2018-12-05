var express = require('express');
var router = express.Router();

var mongoose = require('mongoose')

//=========DATABASE CONNECT=========
mongoose.connect(
  'mongodb://ductrung:cudin5897@ds237713.mlab.com:37713/iot',
  function (err) {
    if (err) throw err
    console.log('Sucessfully connected to data')
  }
)
//----------------------------------





//==========MODEL==================
const App = mongoose.model('App', {
  temporary: {
    type: Number,
    min: 0,
    max: 100
  },
  humidity: {
    type: Number,
    min: 0,
    max: 100
  },
  mcu_id: {
    type: Number
  },
  timestamp: Date
});


const Condition = mongoose.model('Condition', {
  temp_condition: {
    type: Number,
    min: 0,
    max: 100
  },
  humi_condition: {
    type: Number,
    min: 0,
    max: 100
  },
  mcu_id: {
    type: Number
  },
  mode: {
    type: String,
    enum: ["on", "off"],
    default: "off"
  },
  timestamp: Date
});
//---------------------------------




//=========API=====================
/* GET home page. */
router.get('/', function (req, res, next) {
  res.render('index', { title: 'Express' });
});

/* Send data of tempo and humidity of mcu*/
router.get('/send', function (req, res, next) {
  const temporary = req.query.temporary;
  const humidity = req.query.humidity;
  const mcu = req.query.mcu;

  const timestamp = new Date();
  const newData = new App({
    temporary: temporary,
    humidity: humidity,
    mcu_id: mcu,
    timestamp: timestamp
  });
  var error = newData.validateSync();
  newData.save();
  error != undefined
    ? res.send('Error when send data')
    : res.send(newData);
});


/* get latest data of 1 mcu */
router.get('/getone', async function (req, res, next) {
  const mcu = req.query.mcu;
  let data = await App.findOne({
    mcu_id: mcu
  }, {}, { sort: { 'timestamp': -1 } })
  res.send(data);
});

/* get all data of 1 mcu */
router.get('/getall', async function (req, res, next) {
  const mcu = req.query.mcu;
  let data = await App.find({
    mcu_id: mcu
  }, {}, { sort: { 'timestamp': -1 } })
  res.send(data);
});

/* Set condition of mcu */
router.get('/condition', function (req, res, next) {
  const temporary = req.query.temporary;
  const humidity = req.query.humidity;
  const mcu = req.query.mcu;
  const timestamp = new Date();
  const newData = new Condition({
    temp_condition: temporary,
    humi_condition: humidity,
    timestamp: timestamp,
    mcu_id: mcu
  });
  var error = newData.validateSync();
  newData.save();
  error != undefined
    ? res.send('Error when send data')
    : res.send(newData);
});

/* get condition data of 1 mcu */
router.get('/mcu/:mcu', async function (req, res, next) {
  const mcu = req.params.mcu;
  let data = await Condition.findOne({
    mcu_id: mcu
  })
  res.send(data);
});


/* change condition data of 1 mcu */
router.get('/updatemcu/:mcu', async function (req, res, next) {
  const mcu = req.params.mcu;
  const temporary = req.query.temporary;
  const humidity = req.query.humidity;
  const mode = req.query.mode;

  let data = await Condition.findOne({
    mcu_id: mcu
  })

  temporary?data.temp_condition = temporary:null;
  humidity? data.humi_condition = humidity:null;
  mode?data.mode = mode:null;
  data.save();
  res.send(data);
});

/* Get all data. */
router.get('/getall', async function (req, res, next) {
  let data = await App.find();
  res.send(data);
});

//===============================

module.exports = router;
