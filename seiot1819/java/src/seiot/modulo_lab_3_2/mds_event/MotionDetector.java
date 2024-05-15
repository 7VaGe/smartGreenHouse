package seiot.modulo_lab_3_2.mds_event;

import seiot.modulo_lab_3_2.common.*;
import seiot.modulo_lab_3_2.devices.*;

public class MotionDetector extends BasicEventLoopController {

	private enum State {IDLE, DETECTED, ALARM, BLINK0, BLINK1};
	private State state;
	private ObservableMotionDetectorSensor movSensor;
	private Light led;
	private ObservableButton resetButton;
	private AlarmCounter alarmCounter;
	private ObservableTimer timer;
	
	private static final int DT_DETECTED = 1000; 
	private static final int DT_ALARM = 2000; 
	private static final int BLINK_PERIOD = 50; 
	
	public MotionDetector(Light led, ObservableMotionDetectorSensor movSensor, ObservableButton resetButton, AlarmCounter alarmCounter){
		this.led = led;
		this.alarmCounter = alarmCounter;
		this.movSensor = movSensor;
		this.resetButton = resetButton;
		this.led = led;
		state = State.IDLE;
		timer = new ObservableTimer();
		timer.addObserver(this);
		movSensor.addObserver(this);
		resetButton.addObserver(this);
	}
	
	@Override
	protected void processEvent(Event ev) {
		try {
			switch (state){
		    case IDLE:
		      if (ev instanceof MotionDetected){
		        state = State.DETECTED; 
		        timer.scheduleTick(DT_DETECTED);
		        log("DETECTED");
		      }
		      break;
		    case DETECTED:
		      if (ev instanceof MotionNoMoreDetected){
		        state = State.IDLE; 
		        log("Back to IDLE");
			  } else if (ev instanceof Tick){
		          state = State.ALARM;
		          led.switchOn();
		          log("ALARM");
		          alarmCounter.incNumAlarms();
		          timer.scheduleTick(DT_ALARM);
			  }
		      break;
		    case ALARM:
		      if (ev instanceof MotionNoMoreDetected){
		        state = State.IDLE; 
		        log("Back to IDLE");
		      } else {
		        if (ev instanceof Tick){
		          state = State.BLINK0;
		          led.switchOff();
			      timer.start(BLINK_PERIOD);
		        }
		      }
		      break;
		    case BLINK0:
		      if (ev instanceof Tick){
		        led.switchOn();
		        state = State.BLINK1;
		      } else if (ev instanceof ButtonPressed){
		        state = State.IDLE;
		        timer.stop();
		      }
		      break;
		    case BLINK1:
		      if (ev instanceof Tick){
		        led.switchOff();
		        state = State.BLINK0;
		      } else if (ev instanceof ButtonReleased){
		        led.switchOff();
		        state = State.IDLE;
		        timer.stop();
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
