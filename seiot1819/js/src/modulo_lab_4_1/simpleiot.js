function sendData(content) {
  var options = {
    host: '496ccad1.ngrok.io',
    port: '80',
    path:'/api/data',
    method:'POST',
    headers: {
      "Content-Type":"application/json",
      "Content-Length":content.length
    }
  };
  console.log("sending " + content + "...");
  require("http").request(options, function(res)  {
    res.on('close', function(data) {
      console.log("OK.");
    });
    /*
    res.on('data', function(data) { 
      console.log("data: "+data);
    });*/
  }).end(content);
};

function doJob(){
  setInterval(function(){
    var value = analogRead(A0)/1023.0;
    sendData('{ "value": ' + value + ', "place": "campus" }');
  },2000);
}

var wifi = require("Wifi");

wifi.connect("littlebarfly", {password:"seiot1819"}, function(err){
  if (err==null){
    console.log("connected: ",wifi.getIP());
    doJob();
  } else {
    console.log("error - not connected.");
  }
});

