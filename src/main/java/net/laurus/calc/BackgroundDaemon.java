package net.laurus.calc;

import javax.swing.JComponent;

public class BackgroundDaemon extends Thread {

	final JComponent mComponent;

	public BackgroundDaemon(JComponent aComponent) {
		this.mComponent = aComponent;
		this.setDaemon(true);
		start();
	}

	@Override
	public void run() {

		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {}
		this.mComponent.repaint();

	}



}
