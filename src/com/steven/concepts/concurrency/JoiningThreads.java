package com.steven.concepts.concurrency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * stevmc created on 2/22/20
 */
public class JoiningThreads {

	public static void main(String[] args) throws InterruptedException {
		List<Long> numbers = Arrays.asList(1000000000L, 3435L, 35435L, 2324L, 4656L, 23L, 2435L, 5566L);
		// We want to calculate the !0, !3435, !35435, !2324, !4656, !23, !2435, !5566

		List<FactorialCalculationThread> threads = new ArrayList<>();

		for (long number : numbers) {
			threads.add(new FactorialCalculationThread(number));
		}

		for (Thread thread : threads) {
			thread.setDaemon(true);
			thread.start();
			thread.join(2000);
		}

		// optional join after start loop
		//		for (Thread thread : threads) {
		//			thread.join();
		//		}

		for (int i = 0; i < numbers.size(); i++) {
			FactorialCalculationThread thread = threads.get(i);
			if (thread.isFinished()) {
				System.out.println("Factorial of " + numbers.get(i) + " is " + thread.getResult());
			} else {
				System.out.println("The calculation for " + numbers.get(i) + " is still in progress");
			}
		}
	}
}
