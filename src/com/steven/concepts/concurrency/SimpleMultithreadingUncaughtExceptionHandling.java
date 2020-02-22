package com.steven.concepts.concurrency;

/**
 * stevmc created on 2/21/20
 */
public class SimpleMultithreadingUncaughtExceptionHandling {

	public static void main(String[] args) throws InterruptedException {

		// java 7 and down
		Thread java7Thread = new Thread(new Runnable() {
			@Override
			public void run() {
				throw new RuntimeException("Intentional Exception");
			}
		});

		java7Thread.setName("Misbehaving Java 7 Thread");
		java7Thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println(
						"A critical error happened in the thread " + t.getName() + " the error is " + e.getMessage());
			}
		});

		// java 8+
		Thread java8Thread = new Thread(() -> {
			throw new RuntimeException("Intentional Exception");
		});

		java8Thread.setName("Misbehaving Java 8 Thread");
		java8Thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println(
						"A critical error happened in the thread " + t.getName() + " the error is " + e.getMessage());
			}
		});

		java7Thread.start();
		java8Thread.start();

	}
}
