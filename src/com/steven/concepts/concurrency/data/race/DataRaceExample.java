package com.steven.concepts.concurrency.data.race;

/**
 * stevmc created on 2/25/20
 */
public class DataRaceExample {

	public static void main(String[] args) {
		SharedClass sharedClass = new SharedClass();

		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < Integer.MAX_VALUE; i++) {
					sharedClass.increment();
				}
			}
		});

		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < Integer.MAX_VALUE; i++) {
					sharedClass.checkForDataRace();
				}
			}
		});

		thread1.start();
		;
		thread2.start();
		;
	}

	private static class SharedClass {
		// adding volatile keyword ensures that all instructions before and after a volatile variable will be executed
		// as they are written in the code and not changed by the compiler or CPU.
		private volatile int x = 0;
		private volatile int y = 0;

		public void increment() {
			x++;
			y++;
		}

		public void checkForDataRace() {
			// original assumption is that x will always be greater than y because its operation come first,
			// but the CPU may execute the instructions in any order to optimize performance and utilization.
			if (y > x) {
				System.out.println("y > x - Data race detected");
			}
		}
	}
}
