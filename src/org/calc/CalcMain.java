package org.calc;

import java.awt.EventQueue;

import javax.swing.UIManager;

public class CalcMain {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable ignore) {
		}
		EventQueue.invokeLater(() -> {
			try {
				CalcUI frame = new CalcUI(new Calculator());
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
