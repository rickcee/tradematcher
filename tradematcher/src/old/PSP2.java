package tradematcher;

import java.util.Stack;

public class PSP2 {
	/** Set a value for target sum */
	public static final int TARGET_SUM = 7;

	private static Stack<Integer> stack = new Stack<Integer>();

	/** Store the sum of current elements stored in stack */
	private static int sumInStack = 0;

	public static void populateSubset(int[] data, int fromIndex, int endIndex) {
		System.out.println("sumInStack: " + sumInStack);
		/*
		 * Check if sum of elements stored in Stack is equal to the expected target sum.
		 * 
		 * If so, call print method to print the candidate satisfied result.
		 */
		if (sumInStack == TARGET_SUM) {
			print(stack);
			return;
		}

		for (int currentIndex = fromIndex; currentIndex < endIndex; currentIndex++) {
			System.out.println("processing: " + data[currentIndex]);

			if (sumInStack + data[currentIndex] <= TARGET_SUM) {
				stack.push(data[currentIndex]);
				sumInStack += data[currentIndex];

				/*
				 * Make the currentIndex +1, and then use recursion to proceed further.
				 */
				populateSubset(data, currentIndex + 1, endIndex);
				sumInStack -= (Integer) stack.pop();
			} else {
				System.out.println(data[currentIndex] + " too big. skipping.");
			}
		}
	}

	/**
	 * Print satisfied result. i.e. 15 = 4+6+5
	 */

	private static void print(Stack<Integer> stack) {
		StringBuilder sb = new StringBuilder();
		sb.append(TARGET_SUM).append(" = ");
		for (Integer i : stack) {
			sb.append(i).append("+");
		}
		System.out.println(" == RESULT: " + sb.deleteCharAt(sb.length() - 1).toString());
	}

	//private static final int[] DATA = { 1, 3, 4, 5, 6, 2, 7, 8, 9, 10, 11, 13, 14, 15 };
	private static final int[] DATA = { 1, 1, 3, 4, 5, 6, 2, 7, 8, 9, 10, 11, 13, 14, 15 };

	public static void main(String[] args) {
		populateSubset(DATA, 0, DATA.length);
	}
}
