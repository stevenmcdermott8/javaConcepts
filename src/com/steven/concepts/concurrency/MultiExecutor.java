package com.steven.concepts.concurrency;

import java.util.ArrayList;
import java.util.List;

/**
 * stevmc created on 2/22/20
 */
public class MultiExecutor {

	private List<Runnable> tasks;

	/*
	 * @param tasks to executed concurrently
	 */
	public MultiExecutor(List<Runnable> tasks) {
		this.tasks = tasks;
	}

	/**
	 * Starts and executes all the tasks concurrently
	 */
	public void executeAll() {
		List<Thread> threads = new ArrayList<>(tasks.size());

		for (Runnable task : tasks) {
			Thread thread = new Thread(task);
			threads.add(thread);
		}

		for (Thread thread : threads) {
			thread.start();
		}
	}
}