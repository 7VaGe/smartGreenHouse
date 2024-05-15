package seiot.consegna02;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

class SmartCMView extends JFrame  {

	private JButton refill;
	private JProgressBar sugarLevel;
	private JLabel info;

	long lastWarnTime;

	public SmartCMView(){
		super(".:: Smart Coffee Machine ::.");
		setSize(400,200);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.add(Box.createRigidArea(new Dimension(0,20)));

		JPanel infoLine = new JPanel();
		infoLine.setLayout(new BoxLayout(infoLine, BoxLayout.LINE_AXIS));
		info = new JLabel("----");
		info.setPreferredSize(new Dimension(200,15));
		infoLine.add(info);
		mainPanel.add(infoLine);

		mainPanel.add(Box.createRigidArea(new Dimension(0,20)));

		JPanel sugarLine = new JPanel();
		sugarLine.setLayout(new BoxLayout(sugarLine, BoxLayout.LINE_AXIS));
		// sugarLine.add(new JLabel("Sugar level: "));
		sugarLevel = new JProgressBar(0, 100);
		sugarLevel.setValue(0);
		sugarLevel.setStringPainted(true);
		sugarLevel.setPreferredSize(new Dimension(200,25));
		sugarLine.add(Box.createRigidArea(new Dimension(20,0)));
		sugarLine.add(sugarLevel);
		sugarLine.add(Box.createRigidArea(new Dimension(20,0)));
	    mainPanel.add(sugarLine);

		mainPanel.add(Box.createRigidArea(new Dimension(0,20)));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));	    
		refill = new JButton("Refill");
		refill.setEnabled(false);
		buttonPanel.add(refill);

		mainPanel.add(buttonPanel);

		this.getContentPane().add(mainPanel);

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				System.exit(-1);
			}
		});
	}

	public void registerController(Controller contr){
		refill.addActionListener(contr);
	}

	public void updateSugarLevel(int level){		
		SwingUtilities.invokeLater(() -> {
			sugarLevel.setValue(level);
		});
	}

	public void setInfo(String msg){
		SwingUtilities.invokeLater(() -> {
			info.setText(msg);
		});
	}
	
	public void startMaintenance() {
		SwingUtilities.invokeLater(() -> {
			refill.setEnabled(true);
		});
	}
	
	public void refilled(int qty){
		SwingUtilities.invokeLater(() -> {
			info.setText("Coffee refilled: " + qty);
			refill.setEnabled(false);
		});
	}
}
