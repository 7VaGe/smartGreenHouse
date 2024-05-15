package seiot.modulo_lab_3_2.devices.emu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import seiot.modulo_lab_3_2.common.*;
import seiot.modulo_lab_3_2.devices.*;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ObservablePotentiometer extends seiot.modulo_lab_3_2.devices.ObservablePotentiometer {

	private int pinNum;
	private PotFrame potFrame;
	private String id;
	private int value;
	
	public ObservablePotentiometer(int pinNum, String id, int value){
		this.pinNum = pinNum;
		this.id = id;
		this.value = value;
		try {
			potFrame = new PotFrame(pinNum, id, value);
			potFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void notifyNewValue(int value){
		synchronized (this){
			this.value = value;
		}
		notifyEvent(new PotValueChanged(this,value));		
	}
	
	public int getValue(){
		synchronized (this){
			return value;
		}
	}
	
	class PotFrame extends JFrame implements ChangeListener {
	
		public PotFrame(int pin, String id, int value){
		    super("Potentiometer "+id+" on pin "+pin);
		    setSize(300,100);
		    JSlider slider = new JSlider(JSlider.HORIZONTAL,0, 100, value);
		    slider.addChangeListener(this);

		    slider.setMajorTickSpacing(10);
		    slider.setMinorTickSpacing(1);
		    slider.setPaintTicks(true);
		    slider.setPaintLabels(true);
		   
		    getContentPane().add(slider);
		    addWindowListener(new WindowAdapter(){
		      public void windowClosing(WindowEvent ev){
		        System.exit(-1);
		      }
		    });
		  }

		  public void stateChanged(ChangeEvent e) {
			    JSlider source = (JSlider)e.getSource();
			    notifyNewValue((int)source.getValue());
		  }
	}

}
