package com.ideagen.calculator.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorService {

	public static double calculate(String num, String indicator) {

		Pattern pattern = Pattern.compile("-?[0-9.]+|[A-Za-z]+|[-+*/()]|==|<=|>=|&&|[|]{2}");
		Matcher match = pattern.matcher(num);
		List<String> actual = new ArrayList<String>();
		while (match.find()) {
			actual.add(match.group());
		}
		String[] result = actual.toArray(new String[actual.size()]);

		boolean cMultiply = Arrays.stream(result).anyMatch("*"::equals);
		boolean cDivide = Arrays.stream(result).anyMatch("/"::equals);
		boolean cBracket = Arrays.stream(result).anyMatch("("::equals);

		if (cMultiply == true && cDivide == false && cBracket == false) {
			result = processPrecedence(result, "*");
		} else if (cDivide == true && cMultiply == false && cBracket == false) {
			result = processPrecedence(result, "/");
		} else if (cMultiply == true && cDivide == true && cBracket == false) {
			result = processPrecedence(result, "/");
			result = processPrecedence(result, "*");
		} else if (cBracket == true) {
			result = processPrecedence(result, "(");
		}

		double firstNum = Double.parseDouble(result[0]);
		int count = 0;
		double nextNum = 0;
		for (String s : result) {
			switch (s) {
			case "*":
				nextNum = Double.parseDouble(result[count + 1]);
				firstNum *= nextNum;
				break;
			case "/":
				nextNum = Double.parseDouble(result[count + 1]);
				firstNum /= nextNum;
				break;
			case "+":
				nextNum = Double.parseDouble(result[count + 1]);
				firstNum += nextNum;
				break;
			case "-":
				nextNum = Double.parseDouble(result[count + 1]);
				firstNum -= nextNum;
				break;
			}
			count++;
		}
		
		if("user".equals(indicator)) {
			System.out.println("The Answer is: " + firstNum);
		}
		return firstNum;
	}

	public static void startCalculation() {
		String input = "";
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter a String with arithmetic operation separated by a space: ");
		input = sc.nextLine();
		calculate(input, "user");
		sc.close();
	}

	public static String[] processPrecedence(String[] result, String operator) {
		double resultCalc = 0;
		double right = 0;
		double left = 0;
		int index = 0;
		int indexStart = 0;
		int indexEnd = 0;
		List<String> list = new ArrayList<>();
		if (("*").equals(operator)) {
			index = Arrays.asList(result).indexOf("*");
			left = Double.parseDouble(result[index - 1]);
			right = Double.parseDouble(result[index + 1]);
			resultCalc = left * right;
		} else if (("/").equals(operator)) {
			index = Arrays.asList(result).indexOf("/");
			left = Double.parseDouble(result[index - 1]);
			right = Double.parseDouble(result[index + 1]);
			resultCalc = left / right;
		} else if (("(").equals(operator)) {
			indexStart = Arrays.asList(result).indexOf("(");
			indexEnd = Arrays.asList(result).indexOf(")");
			String[] newArray = Arrays.copyOfRange(result, indexStart + 1, indexEnd);
			resultCalc = calculate(String.join(",", newArray), "system");
		}

		if (("(").equals(operator)) {
			list = new ArrayList<>(Arrays.asList(result));
			int indexSize = indexEnd - indexStart;
			for (int i = 0; i < indexSize + 1; i++) {
				list.remove(indexEnd - i);
			}
			list.add(indexStart, String.valueOf(resultCalc));
		} else {
			list = new ArrayList<>(Arrays.asList(result));
			list.remove(index + 1);
			list.remove(index);
			list.remove(index - 1);
			if (index == 0) {
				list.add(index, String.valueOf(resultCalc));
			} else {
				list.add(index - 1, String.valueOf(resultCalc));
			}
		}
		return list.toArray(new String[list.size()]);
	}
}
