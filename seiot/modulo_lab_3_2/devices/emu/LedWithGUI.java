package seiot.modulo_lab_3_2.devices.emu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import seiot.modulo_lab_3_2.devices.*;
import seiot.modulo_lab_3_2.devices.emu.Button.ButtonFrame;


public class LedWithGUI implements Light {
	private int pinNum;
	private String id;
	private LedFrame gui;

	public LedWithGUI(int pinNum, String id){
		this.pinNum = pinNum;
		this.id = id;
		try {
			SwingUtilities.invokeAndWait(()-> {
				try {
					gui = new LedFrame(pinNum, id);
					gui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}

	public LedWithGUI(int pinNum){
		this(pinNum,"green");
	}	
	
	@Override
	public synchronized void switchOn() throws IOException {
		gui.setText("ON");
	}

	@Override
	public synchronized void switchOff() throws IOException {
		gui.setText("OFF");
	}

	class LedFrame extends JFrame  {
		  JLabel state;
		  public LedFrame(int pin, String id){
		    super("Led "+id+" on "+pin);
		    setSize(200,50);
		    state = new JLabel("");
		    getContentPane().add(state);
		    addWindowListener(new WindowAdapter(){
		      public void windowClosing(WindowEvent ev){
		        System.exit(-1);
		      }
		    });
		  }

		public void setText(String text) {			
			SwingUtilities.invokeLater(()->{
				state.setText(text);
			});
		}
	}	
}
