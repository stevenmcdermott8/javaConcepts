package com.steven.concepts.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * stevmc created on 2/21/20
 */
public class SimpleThreadCreation3 {

	public static final int MAX_PASSWORD = 9999;

	public static void main(String[] args) throws InterruptedException {

		Random random = new Random();
		Vault vault = new Vault(random.nextInt(MAX_PASSWORD));
		List<Thread> threads = new ArrayList<>();
		// hacker one starts at 0 goes up while guessing incrementally at the password
		threads.add(new AscendingHackerThread(vault));

		// hacker two starts at MAX_PASSWORD and goes down while guessing incrementally at the password
		threads.add(new DescendingHackerThread(vault));

		// police counts down 10 seconds, if he finishes then hackers are caught.
		threads.add(new PoliceThread());

		for (Thread thread : threads) {
			thread.start();
		}
	}

	private static class Vault {
		private int password;

		public Vault(int password) {
			this.password = password;
		}

		public boolean isCorrectPassword(int guess) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
			}
			return this.password == guess;
		}
	}

	private static abstract class HackerThread extends Thread {
		protected Vault vault;

		public HackerThread(Vault vault) {
			this.vault = vault;
			this.setName(this.getClass().getSimpleName());
			this.setPriority(Thread.MAX_PRIORITY);
		}

		@Override
		public synchronized void start() {
			System.out.println("Starting thread " + this.getName());
			super.start();
		}
	}

	private static class AscendingHackerThread extends HackerThread {

		public AscendingHackerThread(Vault vault) {
			super(vault);
		}

		@Override
		public void run() {
			for (int guess = 0; guess < MAX_PASSWORD; guess++) {
				if (vault.isCorrectPassword(guess)) {
					System.out.println(this.getName() + " guessed the password as " + guess);
					System.exit(0);
				}
			}
			super.run();
		}
	}

	private static class DescendingHackerThread extends HackerThread {

		public DescendingHackerThread(Vault vault) {
			super(vault);
		}

		@Override
		public void run() {
			for (int guess = MAX_PASSWORD; guess >= MAX_PASSWORD; guess--) {
				if (vault.isCorrectPassword(guess)) {
					System.out.println(this.getName() + " guessed the password as " + guess);
					System.exit(0);
				}
			}
			super.run();
		}
	}

	private static class PoliceThread extends Thread {
		@Override
		public void run() {
			for (int i = 10; i > 0; i--) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				System.out.println(i);
			}

			System.out.println("Game over for you hackers");
			System.exit(0);
		}
	}

}
