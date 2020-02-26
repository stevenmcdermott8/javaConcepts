package com.steven.concepts.concurrency.race.condition.atomic.operations;

import java.util.Random;

/**
 * stevmc created on 2/25/20
 */
public class MetricsExample {

	public static void main(String[] args) {
		Metrics metrics = new Metrics();

		BusinessLogic businessLogicThread1 = new BusinessLogic(metrics);
		BusinessLogic businessLogicThread2 = new BusinessLogic(metrics);

		MetricsPrinter metricsPrinterThread = new MetricsPrinter(metrics);

		businessLogicThread1.start();
		businessLogicThread2.start();
		metricsPrinterThread.start();
	}

	private static class MetricsPrinter extends Thread {
		private Metrics metrics;

		public MetricsPrinter(Metrics metrics) {
			this.metrics = metrics;
		}

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {

				}

				double currentAverage = metrics.getAverage();

				System.out.println("Current average is " + currentAverage);
			}
		}
	}

	private static class BusinessLogic extends Thread {
		private MetricsExample.Metrics metrics;
		private Random random = new Random();

		public BusinessLogic(MetricsExample.Metrics metrics) {
			this.metrics = metrics;
		}

		@Override
		public void run() {
			while (true) {
				long start = System.currentTimeMillis();
				try {
					Thread.sleep(random.nextInt(10));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				long end = System.currentTimeMillis();

				metrics.addSample(end - start);
			}
		}
	}

	private static class Metrics {
		private long count = 0;

		// volatile guarantees that since this is a double, the write to average in addSample and the read inside
		// get average are atomic.  double and long are not atomic in nature like other primitives.
		private volatile double average = 0.0;

		public synchronized void addSample(long sample) {
			double currentSum = average * count;
			count++;
			average = (currentSum + sample) / count;
		}

		// get average is not synchronized, so it can be performed 100% in parallel to other threads, not slowing
		// down threads calling it.
		public double getAverage() {
			return average;
		}
	}
}
