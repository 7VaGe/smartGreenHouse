package seiot.modulo_lab_3_2.mds;

import seiot.modulo_lab_3_2.common.*;
import seiot.modulo_lab_3_2.devices.*;

public class MotionDetector extends BasicFSMController {

	private enum State {IDLE, DETECTED, ALARM, BLINK0, BLINK1};
	private State state;
	private MotionDetectorSensor movSensor;
	private Light led;
	private Button resetButton;
	private AlarmCounter alarmCounter;
	
	private int countDetected = 0;
	private int countAlarms = 0;
	private static final int DT_DETECTED = 20; 
	private static final int DT_ALARM = 20; 
	
	public MotionDetector(Light led, MotionDetectorSensor movSensor, Button resetButton, AlarmCounter alarmCounter){
		super(50);
		countDetected = 0;
		countAlarms = 0;
		this.led = led;
		this.alarmCounter = alarmCounter;
		this.movSensor = movSensor;
		this.resetButton = resetButton;
		this.led = led;
		state = State.IDLE;
	}
	
	@Override
	protected void tick() {
		try { 	 
			switch (state){
		    case IDLE:
		      if (movSensor.detected()){
		        countDetected = 0;
		        state = State.DETECTED; 
		        log("DETECTED");
		      }
		      break;
		    case DETECTED:
		      if (!movSensor.detected()){
		        state = State.IDLE; 
		        log("Back to IDLE");
			  } else {
		        if (countDetected < DT_DETECTED){
		          countDetected++;
		        } else {
		          state = State.ALARM;
		          led.switchOn();
		          log("ALARM");
		          countAlarms = 0;
		          alarmCounter.incNumAlarms();
		        }
		      }
		      break;
		    case ALARM:
		      if (!movSensor.detected()){
		        state = State.IDLE; 
		        log("Back to IDLE");
		      } else {
		        if (countAlarms < DT_ALARM){
		          countAlarms++;
		        } else {
		          state = State.BLINK0;
		          led.switchOff();
		        }
		      }
		      break;
		    case BLINK0:
		      if (!resetButton.isPressed()){
		        led.switchOn();
		        state = State.BLINK1;
		      } else {
		        state = State.IDLE;
		      }
		      break;
		    case BLINK1:
		      if (!resetButton.isPressed()){
		        led.switchOff();
		        state = State.BLINK0;
		      } else {
		        led.switchOff();
		        state = State.IDLE;
		      }
		      break;
		  }
		} catch (Exception ex){
			ex.printStackTrace();
		}		
	}

	private void log(String msg){
		System.out.println("[MotionDetector] "+msg);
	}
	
}
