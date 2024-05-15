var pin = Pin(NodeMCU.D2);
var val = 1;
setInterval(function(){
  digitalWrite(pin,val);
  val = 1 - val;
},500);
