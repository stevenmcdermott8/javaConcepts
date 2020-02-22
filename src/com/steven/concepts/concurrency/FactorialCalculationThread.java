package com.steven.concepts.concurrency;

import java.math.BigInteger;

/**
 * stevmc created on 2/22/20
 */
public class FactorialCalculationThread extends Thread {

	private long inputNumber;
	private BigInteger result = BigInteger.ZERO;
	private boolean isFinished;

	public FactorialCalculationThread(long inputNumber) {
		this.inputNumber = inputNumber;
	}

	@Override
	public void run() {
		this.result = factorial(inputNumber);
		this.isFinished = true;
	}

	public BigInteger factorial(long inputNumber) {
		BigInteger tempResult = BigInteger.ONE;

		for (long i = inputNumber; i > 0; i--) {
			tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
		}

		return tempResult;
	}

	public BigInteger getResult() {
		return result;
	}

	public boolean isFinished() {
		return isFinished;
	}

}

