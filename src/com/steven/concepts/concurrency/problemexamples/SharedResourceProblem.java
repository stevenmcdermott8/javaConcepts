package com.steven.concepts.concurrency.problemexamples;

/**
 * stevmc created on 2/24/20
 */
public class SharedResourceProblem {

	public static void main(String[] args) throws InterruptedException {

		InventoryCounter inventoryCounter = new InventoryCounter();
		IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
		DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

		//		// run sequentially, this is safe but not really giving us anything extra we cannot do with one thread anyway
		//		incrementingThread.start();
		//		incrementingThread.join();

		//		decrementingThread.start();
		//		decrementingThread.join();
		//
		//		System.out.println("Inventory is " + inventoryCounter.getItems());

		// expecting IllegalThreadStateException if above code is not commented out,
		// otherwise, unpredictable value from threads sharing resource.
		incrementingThread.start();
		decrementingThread.start();

		incrementingThread.join();
		decrementingThread.join();

		System.out.println("Inventory is " + inventoryCounter.getItems());

	}

	private static class IncrementingThread extends Thread {
		private InventoryCounter inventoryCounter;

		public IncrementingThread(InventoryCounter inventoryCounter) {
			this.inventoryCounter = inventoryCounter;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10_000; i++) {
				inventoryCounter.increment();
			}
		}
	}

	private static class DecrementingThread extends Thread {

		private InventoryCounter inventoryCounter;

		public DecrementingThread(InventoryCounter inventoryCounter) {
			this.inventoryCounter = inventoryCounter;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10_000; i++) {
				inventoryCounter.decrement();
			}
		}
	}

	private static class InventoryCounter {
		private int items = 0;

		public void increment() {
			items++;
		}

		public void decrement() {
			items--;
		}

		public int getItems() {
			return items;
		}

	}
}
