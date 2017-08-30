package wza.slx.com.xlxapplication.test;

import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

public class PolishT2 {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		String line = "";
		// sc.close();
		while (sc.hasNextLine()) {
			line = sc.nextLine();
			double result = calculator(line);
			System.out.println("final result = " + result);
		}

	}

	static String operator = "+-*/%^()";

	/**
	 *    -1-(0-1+1)   -->    0-1-(0-1+1)
	 */
	public static String pretreatment(String str) {
		StringBuilder sb = new StringBuilder(str.trim());
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if (operator.indexOf(c) >= 0 && operator.indexOf(c) < (operator.length() - 2)) {
				if (i == 0) {
					sb.insert(0, '0');
				} else if (sb.charAt(i - 1) == '(') {
					sb.insert(i, '0');
				}
			}
		}

		return sb.toString();
	}

	/**
	 *  op1 > op2 -1,    op1 < op2 1
	 *
	 */
	public static int compare(char op1, char op2) {
		if ('(' == op1) {
			return 1;
		}

		if ('^' == op1) {
			if (op2 == '^') {
				return 0;
			}
			return -1;
		} else if ("+-".indexOf(op1) >= 0) {
			if ("+-".indexOf(op2) >= 0) {
				return 0;
			}
			return 1;
		} else {
			if ("+-".indexOf(op2) >= 0) {
				return -1;
			} else if ('^' == op2) {
				return 1;
			}
			return 0;
		}
	}

	public static double calculator(String str) {

		String preStr = pretreatment(str);

		LinkedBlockingQueue<String> polish = new LinkedBlockingQueue<>();

		StringBuffer sb = new StringBuffer();

		Stack<Character> stack = new Stack<Character>();

		for (int i = 0; i < preStr.length(); i++) {

			char c = preStr.charAt(i);
			if (operator.indexOf(c) >= 0) {
				if (sb.length() > 0) {
					System.out.println(" num = " + sb.toString() + " c = " + c);
					polish.offer(sb.toString());
					sb = new StringBuffer();
				}
				switch (c) {

				case '(':
					stack.push(c);
					break;
				case ')':
					while (stack.size() > 0) {
						char op = stack.pop();
						if (op != '(') {
							polish.offer(String.valueOf(op));
						} else {
							break;
						}

					}
					break;
				default:
					if (stack.size() == 0) {
						stack.push(c);
					} else {
						while (stack.size() > 0) {
							char op = stack.lastElement();
							int compare = compare(op, c);
							if (compare <= 0) {
								polish.offer(String.valueOf(stack.pop()));
							} else {
								stack.push(c);
								break;
							}
						}
					}

					break;
				}
			} else {
				sb.append(c);
			}

		}
		// for end
		if (sb.length() > 0) {
			polish.offer(sb.toString());
		}
		while (stack.size() > 0) {
			polish.offer(String.valueOf(stack.pop()));
		}
		System.out.println("----  polish.size = " + polish.size() + " polish = " + polish.toString());
		for (String s : polish) {
			System.out.println(s);
		}

		return calPolishQueue(polish);
	}

	public static double calPolishQueue(LinkedBlockingQueue<String> polish) {
		Stack<Double> stack = new Stack<Double>();
		while (true) {

			String str = polish.poll();
			if (str == null) {
				break;
			}
			if (operator.indexOf(str) >= 0) {
				double num2 = stack.pop();
				double num1 = stack.pop();
				double resulttemp = 0;
				switch (str.charAt(0)) {
				case '+':
					resulttemp = num1 + num2;
					break;
				case '-':
					resulttemp = num1 - num2;
					break;
				case '*':
					resulttemp = num1 * num2;
					break;
				case '/':
					if (num2 == 0) {
						throw new IllegalArgumentException(" chu 0 ");
					}
					resulttemp = num1 + num2;
					break;
				case '%':
					resulttemp = num1 % num2;
					break;
				case '^':
					resulttemp = Math.pow(num1, num2);
					break;
				default:
					throw new IllegalArgumentException("unknow operator");
				}
				System.out.println(" temp result = " + resulttemp);
				stack.push(resulttemp);

			} else {
				stack.push(Double.valueOf(str));
			}
		
			System.out.println(" stack.size = " + stack.size() + " stack = " + stack.toString() + "  polish.size = "
					+ polish.size() + " polish = " + polish.toString());

		}
		return stack.pop();
	}

}
