package com.steven.concepts;

import java.util.*;
import java.util.Map.Entry;

/**
 * stevmc created on 2/18/20
 */
public class SearchArraysAndListsConcepts {

	private static Integer dataSetSize = 1_000_000;
	private static Integer itemToFind = 900_000;

	public static void main(String[] args) {
		Map<String, Long> totals = new HashMap<>();

		totals.put("searchArrayListWithForLoop", searchArrayListWithForEachLoop());

		System.out.println("\n\n");
		totals.put("searchArrayListWithForLoop", searchArrayListWithForLoop());

		System.out.println("\n\n");
		totals.put("searchArrayListLamdaForEach", searchArrayListLamdaForEach());

		System.out.println("\n\n");
		totals.put("searchArrayListStreamLamdaForEach", searchArrayListStreamLamdaForEach());

		System.out.println("\n\n");
		totals.put("searchArrayListWithLambdaFilter", searchArrayListWithLambdaFilter());

		System.out.println("\n\n");
		totals.put("searchArrayListBinary", searchArrayListBinary());

		System.out.println("\n\n");
		totals.put("searchArrayWithForLoop", searchArrayWithForLoop());

		System.out.println("\n\n");
		totals.put("searchArrayWithForEachLoop", searchArrayWithForEachLoop());

		System.out.println("\n\n");
		totals.put("searchArrayStreamLamdaForEach", searchArrayStreamLamdaForEach());

		System.out.println("\n\n");
		totals.put("searchArrayWithLambdaFilter", searchArrayWithLambdaFilter());

		System.out.println("\n\n");
		totals.put("searchArrayBinary", searchArrayBinary());

		System.out.println("\n\n");
		totals.put("searchLinkedListBinary", searchLinkedListBinary());

		System.out.println("\n\n");
		totals.put("searchLinkedListWithLambdaFilter", searchLinkedListWithLambdaFilter());

		System.out.println("\n\n");
		totals.put("searchLinkedListStreamLamdaForEach", searchLinkedListStreamLamdaForEach());

		System.out.println("\n\n");
		totals.put("searchLinkedListLamdaForEach", searchLinkedListLamdaForEach());

		System.out.println("\n\n");
		totals.put("searchLinkedListWithForEachLoop", searchLinkedListWithForEachLoop());

		// this is so slow its not even worth it, run at your own impatience.
		//System.out.println("\n\n");
		//totals.put("searchLinkedListWithForLoop", searchLinkedListWithForLoop());

		System.out.println("\n\n");
		System.out.println("-------Sorted RESULTS Ascending------");
		totals.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue()).forEach(System.out::println);

		Entry<String, Long> min = getFastestOperation(totals);

		System.out.println("\n\n");
		System.out.println("-------RESULTS------");
		System.out.println("The fastest search was: " + min.getKey() + " with a value of " + min.getValue());
	}

	private static Entry<String, Long> getFastestOperation(Map<String, Long> totals) {
		Entry<String, Long> min = null;
		for (Entry<String, Long> entry : totals.entrySet()) {
			if (min == null || min.getValue() > entry.getValue()) {
				min = entry;
			}
		}

		return min;
	}

	// ARRAY LIST SEARCHES
	private static List<Integer> getTestArrayList() {
		List<Integer> arrayList = new ArrayList<>();

		//populate the list with 10k items
		for (int i = 0; i < dataSetSize; i++) {
			// add int and autobox to Integer
			arrayList.add(i);
		}

		return arrayList;
	}

	private static long searchArrayListWithForLoop() {
		List<Integer> arrayList = getTestArrayList();
		System.out.println("Running ArrayList Search with For Loop search for item... ");
		long start = System.nanoTime();
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i).equals(itemToFind)) {
				System.out.println("Found item...");
				break;
			}
		}
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds for ArrayList by For Loop: " + total);
		return total;
	}

	private static long searchArrayListWithForEachLoop() {
		List<Integer> arrayList = getTestArrayList();

		System.out.println("Running ArrayList Search with For Each Loop search for item... ");
		long start = System.nanoTime();
		for (Integer item : arrayList) {
			if (item.equals(itemToFind)) {
				System.out.println("Found item...");
				break;
			}
		}
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds for ArrayList by For Each Loop: " + total);
		return total;
	}

	private static long searchArrayListLamdaForEach() {
		List<Integer> arrayList = getTestArrayList();

		System.out.println("Running ArrayList Search with Lambda For Each Loop search for item... ");
		long start = System.nanoTime();

		arrayList.forEach(item -> {
			if (item.equals(itemToFind)) {
				System.out.println("Found item...");
				return;
			}
		});
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds by Lambda For Each Loop: " + total);
		return total;
	}

	private static long searchArrayListStreamLamdaForEach() {
		List<Integer> arrayList = new ArrayList<>();

		//populate the list with 10k items
		for (int i = 0; i < dataSetSize; i++) {
			// add int and autobox to Integer
			arrayList.add(i);
		}

		System.out.println("Running ArrayList Search with Lambda For Each Loop search for item... ");
		long start = System.nanoTime();

		arrayList.stream().forEach(item -> {
			if (item.equals(itemToFind)) {
				System.out.println("Found item...");
				return;
			}
		});
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds by Lambda For Each Loop: " + total);
		return total;
	}

	private static long searchArrayListWithLambdaFilter() {
		List<Integer> arrayList = getTestArrayList();

		System.out.println("Running ArrayList Search with Lambda Filter for item... ");
		long start = System.nanoTime();
		arrayList.stream().filter(item -> item.equals(itemToFind)).findAny();
		// making the assumption we found the item, logging because all others log, need to have a fair test
		System.out.println("Found item...");
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds by Lambda Filter: " + total);
		return total;
	}

	private static long searchArrayListBinary() {
		List<Integer> arrayList = getTestArrayList();

		System.out.println("Running ArrayList Search with Binary Search for item... ");
		long start = System.nanoTime();
		// We know arraylist is sorted already, has to be sorted for binary search to work
		int index = Collections.binarySearch(arrayList, itemToFind);
		// making the assumption we found the item, logging because all others log, need to have a fair test
		System.out.println("Found item...");
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds by Binary Search: " + total);
		return total;
	}

	// ARRAY SEARCHES
	private static Integer[] getTestArray() {
		Integer[] array = new Integer[dataSetSize];

		//populate the list with 10k items
		for (int i = 0; i < dataSetSize; i++) {
			// add int and autobox to Integer
			array[i] = i;
		}
		return array;
	}

	private static long searchArrayWithForLoop() {
		Integer[] array = getTestArray();

		System.out.println("Running Array Search with For Loop search for item... ");
		long start = System.nanoTime();
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(itemToFind)) {
				System.out.println("Found item...");
				break;
			}
		}
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds for Array by For Loop: " + total);
		return total;
	}

	private static long searchArrayWithForEachLoop() {
		Integer[] array = getTestArray();

		System.out.println("Running Array Search with For Each Loop search for item... ");
		long start = System.nanoTime();
		for (Integer item : array) {
			if (item.equals(itemToFind)) {
				System.out.println("Found item...");
				break;
			}
		}
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds for Array by For Each Loop: " + total);
		return total;
	}

	private static long searchArrayStreamLamdaForEach() {
		Integer[] array = getTestArray();

		System.out.println("Running Array Search with Lambda Stream For Each Loop search for item... ");
		long start = System.nanoTime();

		Arrays.stream(array).forEach(item -> {
			if (item.equals(itemToFind)) {
				System.out.println("Found item...");
				return;
			}
		});
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds for Array by Lambda Stream For Each Loop: " + total);
		return total;
	}

	private static long searchArrayWithLambdaFilter() {
		Integer[] array = getTestArray();

		System.out.println("Running Array Search with Lambda Filter for item... ");
		long start = System.nanoTime();
		Arrays.stream(array).filter(item -> item.equals(itemToFind)).findAny();
		// making the assumption we found the item, logging because all others log, need to have a fair test
		System.out.println("Found item...");
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds for Array by Lambda Filter: " + total);
		return total;
	}

	private static long searchArrayBinary() {
		Integer[] array = getTestArray();

		System.out.println("Running Array Search with Binary Search for item... ");
		long start = System.nanoTime();
		// We know array is sorted already, has to be sorted for binary search to work
		int index = Arrays.binarySearch(array, itemToFind);
		// making the assumption we found the item, logging because all others log, need to have a fair test
		System.out.println("Found item...");
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds for Array by Binary Search: " + total);
		return total;
	}

	// LINKED LIST SEARCHES
	private static LinkedList<Integer> getTestLinkedList() {
		LinkedList<Integer> arrayList = new LinkedList<>();

		//populate the list with 10k items
		for (int i = 0; i < dataSetSize; i++) {
			// add int and autobox to Integer
			arrayList.add(i);
		}

		return arrayList;
	}

	private static long searchLinkedListWithForLoop() {
		LinkedList<Integer> linkedList = getTestLinkedList();

		System.out.println("Running Linked List with For Loop search for item... ");
		long start = System.nanoTime();
		for (int i = 0; i < linkedList.size(); i++) {
			System.out.println(linkedList.get(i));
			if (linkedList.get(i).equals(itemToFind)) {
				System.out.println("Found item...");
				break;
			}
		}
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds for LinkedList by For Loop: " + total);
		return total;
	}

	private static long searchLinkedListWithForEachLoop() {
		LinkedList<Integer> linkedList = getTestLinkedList();

		System.out.println("Running Linked List Search with For Each Loop search for item... ");
		long start = System.nanoTime();
		for (Integer item : linkedList) {
			if (item.equals(itemToFind)) {
				System.out.println("Found item...");
				break;
			}
		}
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds for Linked List by For Each Loop: " + total);
		return total;
	}

	private static long searchLinkedListLamdaForEach() {
		LinkedList<Integer> linkedList = getTestLinkedList();

		System.out.println("Running Linked List Search with Lambda For Each Loop search for item... ");
		long start = System.nanoTime();
		linkedList.forEach(item -> {
			if (item.equals(itemToFind)) {
				System.out.println("Found item...");
				return;
			}
		});
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds for Linked List by Lambda For Each Loop: " + total);
		return total;
	}

	private static long searchLinkedListStreamLamdaForEach() {
		LinkedList<Integer> linkedList = getTestLinkedList();

		System.out.println("Running Linked List Search with Lambda Stream For Each Loop search for item... ");
		long start = System.nanoTime();

		linkedList.stream().forEach(item -> {
			if (item.equals(itemToFind)) {
				System.out.println("Found item...");
				return;
			}
		});
		long end = System.nanoTime();
		long total = end - start;
		System.out
				.println(" Total search time in nanoseconds for Linked List by Lambda Stream For Each Loop: " + total);
		return total;
	}

	private static long searchLinkedListWithLambdaFilter() {
		LinkedList<Integer> linkedList = getTestLinkedList();

		System.out.println("Running Array Search with Lambda Filter for item... ");
		long start = System.nanoTime();
		linkedList.stream().filter(item -> item.equals(itemToFind)).findAny();
		// making the assumption we found the item, logging because all others log, need to have a fair test
		System.out.println("Found item...");
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds for Array by Lambda Filter: " + total);
		return total;
	}

	private static long searchLinkedListBinary() {
		LinkedList<Integer> linkedList = getTestLinkedList();

		System.out.println("Running Array Search with Binary Search for item... ");
		long start = System.nanoTime();
		// We know array is sorted already, has to be sorted for binary search to work
		int index = Collections.binarySearch(linkedList, itemToFind);
		// making the assumption we found the item, logging because all others log, need to have a fair test
		System.out.println("Found item...");
		long end = System.nanoTime();
		long total = end - start;
		System.out.println(" Total search time in nanoseconds for Array by Binary Search: " + total);
		return total;
	}

}


