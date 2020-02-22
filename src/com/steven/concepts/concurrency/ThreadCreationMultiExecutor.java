package com.steven.concepts.concurrency;

import java.util.ArrayList;
import java.util.List;

/**
 * stevmc created on 2/22/20
 */
public class ThreadCreationMultiExecutor {

	public static void main(String[] args) {
		List<Runnable> runnables = new ArrayList<>();
		runnables.add(createRunnableThread());
		runnables.add(createRunnableThread());
		runnables.add(createRunnableThread());
		runnables.add(createRunnableThread());
		runnables.add(createRunnableThread());
		runnables.add(createRunnableThread());
		MultiExecutor multiExecutor = new MultiExecutor(runnables);
		multiExecutor.executeAll();
	}

	private static Runnable createRunnableThread() {
		Runnable runnableThread = new Runnable() {
			@Override
			public void run() {
				System.out.println("We are in thread " + Thread.currentThread().getName());
			}
		};

		return runnableThread;
	}
}
