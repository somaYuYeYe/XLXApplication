package wza.slx.com.xlxapplication.test;

import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

public class PolishT {
	public static void main(String[] args) {

		System.out.println("请输入一个计算表达式（中缀式）");
		Scanner sc = new Scanner(System.in);
		String line = "";
		// sc.close();
		// System.out.println("输入为：" + line);
		while (sc.hasNextLine()) {
			line = sc.nextLine();
			double result = PolishT.calculator(line);
			System.out.println("final result = " + result);
		}

	}

	static String operator = "+-*/%^()";

	/**
	 * 预处理，正负号前加0，-1-（-1+1） --> 0-1-(0-1+1)
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
	 * 比较优先级，相等 0， op1 > op2 -1, op1 < op2 1
	 *
	 * 急待优化
	 */
	public static int compare(char op1, char op2) {
		if ('(' == op1) { // 遇到括号 就直接入栈 所以 op2 大
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
		} else { // if("*/%".indexOf(op1) >=0) 没必要 再判断是否为 */% 了
			if ("+-".indexOf(op2) >= 0) {
				return -1;
			} else if ('^' == op2) {
				return 1;
			}
			return 0;
		}
	}

	public static double calculator(String str) {

		// 预处理
		String preStr = pretreatment(str);

		// 用于保存 逆波兰式 的队列
		LinkedBlockingQueue<String> polish = new LinkedBlockingQueue<>();

		// 字符 转 数字
		StringBuffer sb = new StringBuffer();

		// 存操作符
		Stack<Character> stack = new Stack<Character>();

		for (int i = 0; i < preStr.length(); i++) {

			char c = preStr.charAt(i);
			// 如果 找到 操作符
			if (operator.indexOf(c) >= 0) {
				if (sb.length() > 0) { // 如果有数字 就压栈
					System.out.println(" 转换 num = " + sb.toString() + " c = " + c);
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
		// for 结束后 ， 如果还有数字 存到波兰表达式中
		if (sb.length() > 0) {
			polish.offer(sb.toString());
		}
		// 如果还有操作符， 存到波兰表达式中
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
							throw new IllegalArgumentException("出错，除数为0");
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
						throw new IllegalArgumentException("出错，不识别的操作符");
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
