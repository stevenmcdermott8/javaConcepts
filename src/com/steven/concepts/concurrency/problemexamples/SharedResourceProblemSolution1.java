package com.steven.concepts.concurrency.problemexamples;

/**
 * stevmc created on 2/24/20
 */
public class SharedResourceProblemSolution1 {

	// solution here is to add the synchronized key word to the increment and decrement operations, forcing java
	// to lock to resource until the current thread accessing it is finished its operation before another thread
	// can access the resource

	public static void main(String[] args) throws InterruptedException {

		InventoryCounter inventoryCounter = new InventoryCounter();
		IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
		DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

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

		public synchronized void increment() {
			// this is actually three operations
			// 1. get current value of items
			// 2. increment current value by 1
			// 3. store the result into items variable

			// EXAMPLE:
			// if items == 0
			// currentValue <-- 0
			// newValue <-- currentValue + 1 = 1
			// items <-- ewValue = 1
			items++;
		}

		public synchronized void decrement() {
			items--;
		}

		public int getItems() {
			return items;
		}

	}
}
