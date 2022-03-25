package net.laurus.calc.gui;

import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class InternalSplitPane extends JSplitPane {

	private static final long serialVersionUID = -4809354188541814941L;

	public InternalSplitPane() {
		super();
		disableResize();
	}

	public void disableResize() {
		BasicSplitPaneUI bleh = (BasicSplitPaneUI) this.getUI();
		bleh.getDivider().setEnabled(false);
		bleh.getDivider().setVisible(false);
	}
}