package net.rickcee.tradematcher;

import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import lombok.extern.slf4j.Slf4j;
import net.rickcee.tradematcher.model.RCTrade;

@Slf4j
public class BlockTradeFinder {
	/** Set a value for target sum */
	private long desiredQty = 0;

	private Stack<RCTrade> stack = new Stack<RCTrade>();

	/** Store the sum of current elements stored in stack */
	private int sumInStack = 0;

	public BlockTradeFinder(long desiredQty) {
		super();
		this.desiredQty = desiredQty;
	}

	private boolean findDesiredQty(List<RCTrade> potentialMatches, int fromIndex, int endIndex) {
		//System.out.println(stack + " || sumInStack: " + sumInStack);
		/*	If the SUM is the one we expect, return */
		if (sumInStack == desiredQty) {
			return true;
		}

		for (int currentIndex = fromIndex; currentIndex < endIndex; currentIndex++) {
			//log.debug("processing: " + data.get(currentIndex));

			RCTrade trade = potentialMatches.get(currentIndex);
			if (sumInStack + trade.getQuantity() <= desiredQty) {
				stack.push(potentialMatches.get(currentIndex));
				log.debug("PUSH: " + stack);
				sumInStack += potentialMatches.get(currentIndex).getQuantity();

				/*
				 * Make the currentIndex +1, and then use recursion to proceed further.
				 */
				boolean result = findDesiredQty(potentialMatches, currentIndex + 1, endIndex);
				if(result) {
					return result;
				}
				sumInStack -= stack.pop().getQuantity();
				log.debug("POP: " + stack);
			} else {
				//System.out.println(data.get(currentIndex) + " too big. skipping.");
			}
		}
		return false;
	}
	
	public Stack<RCTrade> findAllocationsForBlock(List<RCTrade> potentialMatches) {
		long addedQty;
		addedQty = potentialMatches.stream().mapToLong(i -> i.getQuantity()).sum();
		if(desiredQty <= addedQty) {
			log.info(" BLOCK Potential Matches: " + potentialMatches.size() + " - Desired Qty: " + desiredQty + " / Total Qty: " + addedQty + ", Searching...");
			
			potentialMatches.sort(new Comparator<RCTrade>() {

				@Override
				public int compare(RCTrade o1, RCTrade o2) {
					return o2.getQuantity().compareTo(o1.getQuantity());
				}
			});
			
			findDesiredQty(potentialMatches, 0, potentialMatches.size());
		} else {
			log.info(" BLOCK Potential Matches: " + potentialMatches.size() + " - Desired Qty: " + desiredQty + " / Total Qty: " + addedQty + ", Quantity is insufficient, skipping...");
		}
		return stack;
	}
	
}
