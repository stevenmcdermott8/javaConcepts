package com.steven.concepts.concurrency;

import java.math.BigInteger;

/**
 * stevmc created on 2/22/20
 */
public class InterruptingThreadsLongComputation {

	public static void main(String[] args) {
		Thread thread = new Thread(new LongComputationTask(new BigInteger("2000000"), new BigInteger("10000000")));
		thread.start();
		// Interrupt and handle thread being stopped inside the thread code itself
		thread.interrupt();

		Thread thread2 = new Thread(new LongComputationTask(new BigInteger("2000000"), new BigInteger("10000000")));
		// because this is a daemon thread, interrupt will allow the application to exit when this is interrupted.
		thread2.setDaemon(true);
		thread2.start();
		thread2.interrupt();
	}

	private static class LongComputationTask implements Runnable {

		private BigInteger base;
		private BigInteger power;

		public LongComputationTask(BigInteger base, BigInteger power) {
			this.base = base;
			this.power = power;
		}

		@Override
		public void run() {
			System.out.println(base + "^" + power + " = " + pow(base, power));
		}

		private BigInteger pow(BigInteger base, BigInteger power) {
			BigInteger result = BigInteger.ONE;

			for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
				if (Thread.currentThread().isInterrupted()) {
					System.out.println("Thread interrupted");
					return BigInteger.ZERO;
				}
				result = result.multiply(base);
			}

			return result;
		}

	}

}
