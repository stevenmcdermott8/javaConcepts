package com.steven.concepts.concurrency;

/**
 * stevmc created on 2/21/20
 */
public class SimpleMultithreading {

	public static void main(String[] args) throws InterruptedException {

		// java 7 and down
		Thread java7Thread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("We are in thread " + Thread.currentThread().getName() + " inside java 7 thread");
				System.out.println("Current thread priority " + Thread.currentThread().getPriority());
			}
		});

		java7Thread.setName("java7-WorkerThread");
		java7Thread.setPriority(Thread.MIN_PRIORITY);

		// java 8+
		Thread java8Thread = new Thread(() -> {
			System.out.println("We are in thread " + Thread.currentThread().getName() + " inside java 8  thread");
			System.out.println("Current thread priority " + Thread.currentThread().getPriority());
		});
		java7Thread.setPriority(Thread.MAX_PRIORITY);
		java8Thread.setName("java8-WorkerThread");

		System.out.println("We are in thread " + Thread.currentThread().getName() + " before java 7 thread");
		java7Thread.start();
		System.out.println("We are in thread " + Thread.currentThread().getName() + " after java 7 thread");

		Thread.sleep(1000);
		System.out.println("We are in thread " + Thread.currentThread().getName() + " before java 8 thread");
		java8Thread.start();
		System.out.println("We are in thread " + Thread.currentThread().getName() + " after java 8 thread");

		Thread.sleep(1000);
	}
}
