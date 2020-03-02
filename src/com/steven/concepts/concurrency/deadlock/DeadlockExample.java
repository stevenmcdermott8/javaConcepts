package com.steven.concepts.concurrency.deadlock;

import java.util.Random;

/**
 * stevmc created on 3/1/20
 */
public class DeadlockExample {

	public static void main(String[] args) {
		Intersection intersection = new Intersection();
		Thread trainA = new Thread(new TrainA(intersection));
		Thread trainB = new Thread(new TrainB(intersection));

		trainA.start();
		trainB.start();
	}

	private static class TrainA implements Runnable {
		private final Intersection intersection;
		private Random random = new Random();

		public TrainA(Intersection intersection) {
			this.intersection = intersection;
		}

		@Override
		public void run() {
			while (true) {
				long sleepingTime = random.nextInt(5);
				try {
					Thread.sleep(sleepingTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				intersection.takeRoadA();
			}
		}
	}

	private static class TrainB implements Runnable {
		private final Intersection intersection;
		private Random random = new Random();

		public TrainB(Intersection intersection) {
			this.intersection = intersection;
		}

		@Override
		public void run() {
			while (true) {
				long sleepingTime = random.nextInt(5);
				try {
					Thread.sleep(sleepingTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				intersection.takeRoadB();
			}
		}
	}

	private static class Intersection {
		private Object roadA = new Object();
		private Object roadB = new Object();

		public void takeRoadA() {
			synchronized (roadA) {
				System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

				synchronized (roadB) {
					System.out.println("Train is passing through Road A");
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		// to avoid deadlock to locked resources must be locked in the same order by threads sharing the resources
		// otherwise circular wait happens.
		// to recreate the deadlock, in this method, swap roadA and roadB locked resources.
		public void takeRoadB() {
			synchronized (roadA) {
				System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

				synchronized (roadB) {
					System.out.println("Train is passing through Road B");
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
