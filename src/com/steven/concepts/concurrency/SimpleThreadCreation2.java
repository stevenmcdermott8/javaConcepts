package com.steven.concepts.concurrency;

/**
 * stevmc created on 2/21/20
 */
public class SimpleThreadCreation2 {

	public static void main(String[] args) throws InterruptedException {
		Thread thread = new NewThread();

		thread.start();

	}

	private static class NewThread extends Thread {
		@Override
		public void run() {
			System.out.println("Hello from thread " + this.getName());
		}
	}
}
