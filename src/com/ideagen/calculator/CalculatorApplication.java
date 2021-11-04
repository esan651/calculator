package com.ideagen.calculator;

import com.ideagen.calculator.service.CalculatorService;

public class CalculatorApplication {

	public static void main(String[] args) {
		try {
			CalculatorService.startCalculation();
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
