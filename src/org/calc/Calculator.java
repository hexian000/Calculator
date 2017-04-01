package org.calc;

import java.security.InvalidParameterException;
import java.util.Stack;

public class Calculator {
	private Stack<Double> numStack;
	private Stack<Character> opStack;
	private String display, expression, lastExpression;
	private boolean displayDirty, dotUsed, opChange;
	private char op;
	private double num;

	public Calculator() {
		numStack = new Stack<Double>();
		opStack = new Stack<Character>();
		pressClear();
	}

	private int getPriority(char op) {
		switch (op) {
		case '+':
		case '-':
			return 1;
		case '*':
		case '/':
			return 2;
		case '^':
			return 3;
		}
		return -1;
	}

	private double compute() {
		while (!opStack.empty())
			numStack.push(compute(numStack.pop(), opStack.pop(), numStack.pop()));
		return numStack.pop();
	}

	private double compute(char op) {
		double num = numStack.peek();
		while (!opStack.empty()) {
			if (getPriority(opStack.peek()) >= getPriority(op))
				numStack.push(num = compute(numStack.pop(), opStack.pop(), numStack.pop()));
			else
				break;
		}
		return num;
	}

	private double compute(double op1, char op, double op2) {
		switch (op) {
		case '+':
			return op1 + op2;
		case '-':
			return op1 - op2;
		case '*':
			return op1 * op2;
		case '/':
			return op1 / op2;
		default:
			throw new InvalidParameterException();
		}
	}

	public String pressClear() {
		numStack.clear();
		numStack.push(0.0);
		opStack.clear();
		display = "0";
		displayDirty = false;
		expression = "";
		dotUsed = false;
		op = 0;
		num = 0.0;
		opChange = false;
		return display;
	}

	public String pressNumber(char num) {
		if (num < '0' || num > '9')
			throw new InvalidParameterException();
		if (displayDirty) {
			if (!opStack.empty()) {
				op = opStack.pop();
				expression += " " + op;
				compute(op);
				opStack.push(op);
			}
			display = "0";
			displayDirty = false;
		}
		if (display.equals("0"))
			display = String.valueOf(num);
		else
			display += num;
		opChange = false;
		return display;
	}

	public String pressDot() {
		if (dotUsed)
			return display;
		if (displayDirty) {
			display = "0";
			displayDirty = false;
		}
		display += '.';
		dotUsed = true;
		opChange = false;
		return display;
	}

	public String pressOperator(char op) {
		switch (op) {
		case '+':
		case '-':
		case '*':
		case '/':
			this.op = op;
			break;
		default:
			throw new InvalidParameterException();
		}
		double num = Double.parseDouble(display);
		this.num = num;
		if (opChange) {
			opStack.pop();
			numStack.pop();
		} else {
			if (opStack.empty())
				expression = String.valueOf(num);
			else
				expression += " " + num;
		}
		numStack.push(num);
		opStack.push(op);
		displayDirty = true;
		dotUsed = false;
		opChange = true;
		return display;
	}

	public String pressEval() {
		double num = Double.parseDouble(display);
		if (!displayDirty)
			this.num = num;
		if (opStack.empty()) {
			if (op == 0)
				expression = String.valueOf(num);
			else {
				expression = String.valueOf(num) + " " + op + " " + String.valueOf(this.num);
				opStack.push(op);
				numStack.push(this.num);
			}
		} else {
			if (opChange)
				expression += " " + op;
			expression += " " + num;
		}
		numStack.push(num);
		dotUsed = false;
		opChange = false;
		display = String.valueOf(compute());
		displayDirty = true;
		lastExpression = expression + " = " + display;
		return display;
	}

	public String getLastExpression() {
		return lastExpression;
	}
}
