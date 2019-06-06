/**
 * 
 */
package tradematcher;

import java.util.ArrayList;
import java.util.Stack;

import net.rickcee.tradematcher.IMatchable;
import net.rickcee.tradematcher.util.BlockTradeFinder;

/**
 * @author rickcee
 *
 */
public class BTFTest {
	static ArrayList<IMatchable> ALLOCS = new ArrayList<>();
	
	static {
		ALLOCS.add(new TestTrade(1, 1500000L));
		ALLOCS.add(new TestTrade(2, 1000000L));
		ALLOCS.add(new TestTrade(3, 3000000L));
		ALLOCS.add(new TestTrade(4, 4000000L));
		ALLOCS.add(new TestTrade(5, 5000000L));
		ALLOCS.add(new TestTrade(6, 6000000L));
		ALLOCS.add(new TestTrade(7, 2000000L));
		ALLOCS.add(new TestTrade(8, 3000000L));
		ALLOCS.add(new TestTrade(9, 1500000L));
		ALLOCS.add(new TestTrade(10, 5000000L));
	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		BlockTradeFinder bft = new BlockTradeFinder(8000000);
		Stack<? extends IMatchable> result = bft.findAllocationsForBlock(ALLOCS);
		System.out.println("1st Result: " + result);
		
		ALLOCS.removeAll(result);
		bft = new BlockTradeFinder(8000000);
		result = bft.findAllocationsForBlock(ALLOCS);
		System.out.println("2nd Result: " + result);
		
		ALLOCS.removeAll(result);
		bft = new BlockTradeFinder(8000000);
		result = bft.findAllocationsForBlock(ALLOCS);
		System.out.println("3rd Result: " + result);
		
		ALLOCS.removeAll(result);
		bft = new BlockTradeFinder(8000000);
		result = bft.findAllocationsForBlock(ALLOCS);
		System.out.println("4th Result: " + result);
		
		ALLOCS.removeAll(result);
		bft = new BlockTradeFinder(8000000);
		result = bft.findAllocationsForBlock(ALLOCS);
		System.out.println("5th Result: " + result);

	}

}
