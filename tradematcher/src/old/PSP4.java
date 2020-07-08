package tradematcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class PSP4 {
	/** Set a value for target sum */
	public static final int TARGET_SUM = 3;

	private static Stack<TestTrade> stack = new Stack<TestTrade>();

	/** Store the sum of current elements stored in stack */
	private static int sumInStack = 0;

	public static boolean populateSubset(ArrayList<TestTrade> data, int fromIndex, int endIndex) {
		System.out.println(stack + " || sumInStack: " + sumInStack);
		/*
		 * Check if sum of elements stored in Stack is equal to the expected target sum.
		 * 
		 * If so, call print method to print the candidate satisfied result.
		 */
		if (sumInStack == TARGET_SUM) {
			print(stack);
			return true;
		}

		for (int currentIndex = fromIndex; currentIndex < endIndex; currentIndex++) {
			//System.out.println("processing: " + data[currentIndex]);

			if (sumInStack + data.get(currentIndex).getQuantity() <= TARGET_SUM) {
				stack.push(data.get(currentIndex));
				sumInStack += data.get(currentIndex).getQuantity();

				/*
				 * Make the currentIndex +1, and then use recursion to proceed further.
				 */
				boolean result = populateSubset(data, currentIndex + 1, endIndex);
				if(result) {
					return result;
				}
				sumInStack -= ((TestTrade) stack.pop()).quantity;
			} else {
				System.out.println(data.get(currentIndex) + " too big. skipping.");
				//return false;
			}
		}
		return false;
	}

	/**
	 * Print satisfied result. i.e. 15 = 4+6+5
	 */

	private static void print(Stack<TestTrade> stack) {
		StringBuilder sb = new StringBuilder();
		sb.append(TARGET_SUM).append(" = ");
		for (TestTrade i : stack) {
			sb.append(i.quantity).append("+");
		}
		System.out.println(" == RESULT: " + sb.deleteCharAt(sb.length() - 1).toString());
	}

	public static void main(String[] args) {
		//private static final int[] DATA = { 1, 1, 3, 4, 5, 6, 2, 7, 8, 9, 10, 11, 13, 14, 15 };
		//TestTrade[] DATA = { new TestTrade(1, 1L), new TestTrade(2, 1L), new TestTrade(3, 3L), new TestTrade(4, 4L), new TestTrade(5, 5L), new TestTrade(6, 6L), new TestTrade(7, 2L) };
		TestTrade[] DATA = { new TestTrade(2, 1L), new TestTrade(3, 3L), new TestTrade(4, 4L), new TestTrade(5, 5L), new TestTrade(6, 6L), new TestTrade(7, 2L), new TestTrade(8, 3L) };
		ArrayList<TestTrade> DATALIST = new ArrayList<>();
		DATALIST.addAll(Arrays.asList(DATA));
		System.out.println("DATALIST: " + DATALIST);
		populateSubset(DATALIST, 0, DATALIST.size());
		System.out.println("RESULT: " + stack);
		
		DATALIST.removeAll(stack);
		stack.removeAllElements();
		sumInStack = 0;
		System.out.println("DATALIST: " + DATALIST);
		populateSubset(DATALIST, 0, DATALIST.size());
		System.out.println("RESULT: " + stack);
		
		DATALIST.removeAll(stack);
		stack.removeAllElements();
		sumInStack = 0;
		System.out.println("DATALIST: " + DATALIST);
		populateSubset(DATALIST, 0, DATALIST.size());
		System.out.println("RESULT: " + stack);
	}

	
}
