package org.calc;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.UIManager;

public class CalcUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4046019222805098694L;
	private JPanel contentPane;
	private JTextField txtDisplay;

	/**
	 * Create the frame.
	 */
	public CalcUI(Calculator calc) {
		setTitle("Calculator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(4, 4));
		setContentPane(contentPane);

		txtDisplay = new JTextField();
		txtDisplay.setText("0");
		txtDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDisplay.setEditable(false);
		contentPane.add(txtDisplay, BorderLayout.NORTH);
		txtDisplay.setColumns(10);

		JPanel pnlNumber = new JPanel();
		contentPane.add(pnlNumber, BorderLayout.WEST);
		pnlNumber.setLayout(new GridLayout(4, 4, 4, 4));

		JButton btn1 = new JButton("1");
		btn1.addActionListener((e) -> txtDisplay.setText(calc.pressNumber('1')));
		pnlNumber.add(btn1);

		JButton btn2 = new JButton("2");
		btn2.addActionListener((e) -> txtDisplay.setText(calc.pressNumber('2')));
		pnlNumber.add(btn2);

		JButton btn3 = new JButton("3");
		btn3.addActionListener((e) -> txtDisplay.setText(calc.pressNumber('3')));
		pnlNumber.add(btn3);

		JButton btnAdd = new JButton("+");
		btnAdd.addActionListener((e) -> txtDisplay.setText(calc.pressOperator('+')));
		pnlNumber.add(btnAdd);

		JButton btn4 = new JButton("4");
		btn4.addActionListener((e) -> txtDisplay.setText(calc.pressNumber('4')));
		pnlNumber.add(btn4);

		JButton btn5 = new JButton("5");
		btn5.addActionListener((e) -> txtDisplay.setText(calc.pressNumber('5')));
		pnlNumber.add(btn5);

		JButton btn6 = new JButton("6");
		btn6.addActionListener((e) -> txtDisplay.setText(calc.pressNumber('6')));
		pnlNumber.add(btn6);

		JButton btnSub = new JButton("-");
		btnSub.addActionListener((e) -> txtDisplay.setText(calc.pressOperator('-')));
		pnlNumber.add(btnSub);

		JButton btn7 = new JButton("7");
		btn7.addActionListener((e) -> txtDisplay.setText(calc.pressNumber('7')));
		pnlNumber.add(btn7);

		JButton btn8 = new JButton("8");
		btn8.addActionListener((e) -> txtDisplay.setText(calc.pressNumber('8')));
		pnlNumber.add(btn8);

		JButton btn9 = new JButton("9");
		btn9.addActionListener((e) -> txtDisplay.setText(calc.pressNumber('9')));
		pnlNumber.add(btn9);

		JButton btnMul = new JButton("*");
		btnMul.addActionListener((e) -> txtDisplay.setText(calc.pressOperator('*')));
		pnlNumber.add(btnMul);

		JButton btnDot = new JButton(".");
		btnDot.addActionListener((e) -> txtDisplay.setText(calc.pressDot()));
		pnlNumber.add(btnDot);

		JButton btn0 = new JButton("0");
		btn0.addActionListener((e) -> txtDisplay.setText(calc.pressNumber('0')));
		pnlNumber.add(btn0);

		JButton btnEval = new JButton("=");
		pnlNumber.add(btnEval);

		JButton btnDiv = new JButton("/");
		btnDiv.addActionListener((e) -> txtDisplay.setText(calc.pressOperator('/')));
		pnlNumber.add(btnDiv);

		JPanel pnlHistory = new JPanel();
		contentPane.add(pnlHistory, BorderLayout.CENTER);
		pnlHistory.setLayout(new BorderLayout(0, 0));

		JPanel pnlControl = new JPanel();
		pnlHistory.add(pnlControl, BorderLayout.SOUTH);

		JButton btnSave = new JButton("\u4FDD\u5B58");
		pnlControl.add(btnSave);

		JButton btnCopy = new JButton("\u590D\u5236");
		pnlControl.add(btnCopy);

		JButton btnClear = new JButton("\u6E05\u9664");
		pnlControl.add(btnClear);

		JLabel lblLastExpression = new JLabel(" ");
		lblLastExpression.setFont(UIManager.getFont("Menu.font"));
		lblLastExpression.setOpaque(true);
		lblLastExpression.setForeground(Color.WHITE);
		lblLastExpression.setBackground(Color.BLACK);
		lblLastExpression.setHorizontalAlignment(SwingConstants.CENTER);
		pnlHistory.add(lblLastExpression, BorderLayout.NORTH);

		JTextArea txtExpressions = new JTextArea();
		txtExpressions.setLineWrap(true);
		txtExpressions.setEditable(false);
		pnlHistory.add(txtExpressions, BorderLayout.CENTER);

		btnEval.addActionListener((e) -> {
			txtDisplay.setText(calc.pressEval());
			String exp = calc.getLastExpression();
			lblLastExpression.setText(exp);
			txtExpressions.append(exp + System.lineSeparator());
		});

		btnSave.addActionListener((e) -> {
			JFileChooser saveDlg = new JFileChooser();
			saveDlg.setDialogType(JFileChooser.SAVE_DIALOG);
			saveDlg.setAcceptAllFileFilterUsed(false);
			saveDlg.addChoosableFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(".txt");
				}

				@Override
				public String getDescription() {
					return "文本文件(UTF-8)";
				}
			});
			if (saveDlg.showDialog(this, "保存计算过程") == JFileChooser.APPROVE_OPTION) {
				PrintStream ps = null;
				try {
					ps = new PrintStream(saveDlg.getSelectedFile(), "UTF-8");
					ps.print('\uFEFF');
					ps.println(DateFormat.getDateTimeInstance().format(new Date()));
					ps.print(txtExpressions.getText());
				} catch (FileNotFoundException | UnsupportedEncodingException e1) {
					JOptionPane.showMessageDialog(this, e1.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
				} finally {
					if (ps != null)
						ps.close();
				}
			}
		});

		btnClear.addActionListener((e) -> {
			lblLastExpression.setText(" ");
			txtExpressions.setText("");
			txtDisplay.setText("0");
			calc.pressClear();
		});

		btnCopy.addActionListener((e) -> Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(new StringSelection(txtExpressions.getText()), null));
	}

}
