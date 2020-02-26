package com.steven.concepts.concurrency.race.condition.atomic.operations;

/**
 * stevmc created on 2/24/20
 */
public class SharedResourceProblemSolution2 {

	// solution here is to add the synchronized keyword to only a section of code, protection the logic wrapped within
	// but not making the entire method synchronized
	// allows for keeping the section of code needing thread safety/safe execution to be kept to a minimum
	// also, different objects can be used for the locks, this will allow different blocks of code to be executed by
	// different threads and be safe, keeping threads from being blocked when they dont need to be

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
		Object lock = new Object();

		public void increment() {
			// this is actually three operations
			// 1. get current value of items
			// 2. increment current value by 1
			// 3. store the result into items variable

			// EXAMPLE:
			// if items == 0
			// currentValue <-- 0
			// newValue <-- currentValue + 1 = 1
			// items <-- ewValue = 1
			synchronized (this.lock) {
				items++;
			}
		}

		public void decrement() {
			synchronized (this.lock) {
				items--;
			}
		}

		public int getItems() {
			return items;
		}

	}
}
