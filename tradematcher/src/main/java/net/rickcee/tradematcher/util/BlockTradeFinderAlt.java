package net.rickcee.tradematcher.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import lombok.extern.slf4j.Slf4j;
import net.rickcee.tradematcher.IMatchable;

@Slf4j
public class BlockTradeFinderAlt {
	/** Set a value for target sum */
	private long desiredQty = 0;
	private boolean[][] dp;
	private int factor = 1000;

	private Stack<IMatchable> stack = new Stack<IMatchable>();
	private ArrayList<IMatchable> result;

	/** Store the sum of current elements stored in stack */
	private int sumInStack = 0;

	public BlockTradeFinderAlt(long desiredQty) {
		super();
		this.desiredQty = desiredQty;
	}

//	public boolean findDesiredQty(ArrayList<? extends IMatchable> data, int fromIndex, int endIndex) {
//		//System.out.println(stack + " || sumInStack: " + sumInStack);
//		/*	If the SUM is the one we expect, return */
//		if (sumInStack == desiredQty) {
//			return true;
//		}
//
//		for (int currentIndex = fromIndex; currentIndex < endIndex; currentIndex++) {
//			//log.debug("processing: " + data.get(currentIndex));
//
//			IMatchable trade = data.get(currentIndex);
//			if (sumInStack + trade.getQuantity() <= desiredQty) {
//				stack.push(data.get(currentIndex));
//				log.debug("PUSH: " + stack);
//				sumInStack += data.get(currentIndex).getQuantity();
//
//				/*
//				 * Make the currentIndex +1, and then use recursion to proceed further.
//				 */
//				boolean result = findDesiredQty(data, currentIndex + 1, endIndex);
//				if(result) {
//					return result;
//				}
//				sumInStack -= stack.pop().getQuantity();
//				log.debug("POP: " + stack);
//			} else {
//				//System.out.println(data.get(currentIndex) + " too big. skipping.");
//			}
//		}
//		return false;
//	}
	
	private void printSubsetsRec(IMatchable arr[], int i, int sum, ArrayList<IMatchable> p) {
		// If we reached end and sum is non-zero. We print
		// p[] only if arr[0] is equal to sun OR dp[0][sum]
		// is true.
		if (i == 0 && sum != 0 && dp[0][sum]) {
			//System.out.println("Adding (1): " + arr[i]);
			p.add(arr[i]);
			//System.out.println("Display: " + Arrays.toString(arr));
//			display(p);
//			p.clear();
			result = p;
			return;
		}

		// If sum becomes 0
		if (i == 0 && sum == 0) {
//			display(p);
//			p.clear();
			result = p;
			return;
		}

		// If given sum can be achieved after ignoring
		// current element.
		if (dp[i - 1][sum]) {
			// Create a new vector to store path
			ArrayList<IMatchable> b = new ArrayList<>();
			//System.out.println("Adding (2): " + arr[i]);
			b.addAll(p);
			printSubsetsRec(arr, i - 1, sum, b);
		}

		// If given sum can be achieved after considering
		// current element.
		int qty = arr[i].getQuantity().intValue()/factor;
		if (sum >= qty && dp[i - 1][sum - qty]) {
			//System.out.println("Adding (3): " + arr[i]);
			p.add(arr[i]);
			printSubsetsRec(arr, i - 1, sum - qty, p);
		}
	}
	
	private void printAllSubsets(IMatchable arr[], int n, int sum) {
		if (n == 0 || sum < 0)
			return;

		// Sum 0 can always be achieved with 0 elements
		dp = new boolean[n][sum + 1];
		for (int i = 0; i < n; ++i) {
			dp[i][0] = true;
		}

		// Sum arr[0] can be achieved with single element
		if (arr[0].getQuantity().intValue()/factor <= sum)
			dp[0][arr[0].getQuantity().intValue()/factor] = true;

		// Fill rest of the entries in dp[][]
		for (int i = 1; i < n; ++i)
			for (int j = 0; j < sum + 1; ++j)
				dp[i][j] = (arr[i].getQuantity().intValue()/factor <= j) ? (dp[i - 1][j] || dp[i - 1][j - arr[i].getQuantity().intValue()/factor]) : dp[i - 1][j];
		
		//System.out.println("DP: " + Arrays.deepToString(dp));
		System.out.print(".");
		if (dp[n - 1][sum] == false) {
			System.out.println("There are no subsets with" + " sum " + sum);
			return;
		}

		// Now recursively traverse dp[][] to find all
		// paths from dp[n-1][sum]
		ArrayList<IMatchable> p = new ArrayList<>();
		printSubsetsRec(arr, n - 1, sum, p);
	}
	
	public ArrayList<? extends IMatchable> findAllocationsForBlock(ArrayList<? extends IMatchable> data) {
		long addedQty;
		addedQty = data.stream().mapToLong(i -> i.getQuantity()).sum();
		if(desiredQty <= addedQty) {
			log.debug("Desired Qty: " + desiredQty + " / Added Qty: " + addedQty + ". Processing.");
			
			data.sort(null);
			
			printAllSubsets(data.toArray(new IMatchable[data.size()]), data.size(), (int) desiredQty / factor);
			//findDesiredQty(data, 0, data.size());
		} else {
			log.debug("Desired Qty: " + desiredQty + " / Added Qty: " + addedQty + ". Skipping.");
		}
		return result;
	}
	
}
