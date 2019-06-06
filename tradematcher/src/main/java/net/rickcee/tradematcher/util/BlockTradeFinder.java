package net.rickcee.tradematcher.util;

import java.util.ArrayList;
import java.util.Stack;

import lombok.extern.slf4j.Slf4j;
import net.rickcee.tradematcher.IMatchable;

@Slf4j
public class BlockTradeFinder {
	/** Set a value for target sum */
	private long desiredQty = 0;

	private Stack<IMatchable> stack = new Stack<IMatchable>();

	/** Store the sum of current elements stored in stack */
	private int sumInStack = 0;

	public BlockTradeFinder(long desiredQty) {
		super();
		this.desiredQty = desiredQty;
	}

	public boolean findDesiredQty(ArrayList<? extends IMatchable> data, int fromIndex, int endIndex) {
		//System.out.println(stack + " || sumInStack: " + sumInStack);
		/*	If the SUM is the one we expect, return */
		if (sumInStack == desiredQty) {
			return true;
		}

		for (int currentIndex = fromIndex; currentIndex < endIndex; currentIndex++) {
			//log.debug("processing: " + data.get(currentIndex));

			IMatchable trade = data.get(currentIndex);
			if (sumInStack + trade.getQuantity() <= desiredQty) {
				stack.push(data.get(currentIndex));
				log.debug("PUSH: " + stack);
				sumInStack += data.get(currentIndex).getQuantity();

				/*
				 * Make the currentIndex +1, and then use recursion to proceed further.
				 */
				boolean result = findDesiredQty(data, currentIndex + 1, endIndex);
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
	
	public Stack<? extends IMatchable> findAllocationsForBlock(ArrayList<? extends IMatchable> data) {
		long addedQty;
		addedQty = data.stream().mapToLong(i -> i.getQuantity()).sum();
		if(desiredQty <= addedQty) {
			log.debug("Desired Qty: " + desiredQty + " / Added Qty: " + addedQty + ". Processing.");
			
			data.sort(null);
			
			findDesiredQty(data, 0, data.size());
		} else {
			log.debug("Desired Qty: " + desiredQty + " / Added Qty: " + addedQty + ". Skipping.");
		}
		return stack;
	}
	
}
