package com.steven.concepts.concurrency.race.condition.atomic.operations;

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
		// use code above to see differing behavior
		// the threads started below threads are not atomic operations
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

		public void decrement() {
			items--;
		}

		public int getItems() {
			return items;
		}

	}
}
