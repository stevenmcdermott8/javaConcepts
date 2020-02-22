package com.steven.concepts.concurrency;

/**
 * stevmc created on 2/22/20
 */
public class InterruptingThreadsLongRunning {

	public static void main(String[] args) {
		Thread thread = new Thread(new BlockingTask());
		thread.start();
		thread.interrupt();
	}

	private static class BlockingTask implements Runnable {
		@Override
		public void run() {
			// do things

			try {
				Thread.sleep(500000000);
			} catch (InterruptedException e) {
				System.out.println("Exiting blocking thread");
			}
		}
	}
}
