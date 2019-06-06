/**
 * 
 */
package net.rickcee.tradematcher.test;

import java.util.ArrayList;
import java.util.Stack;

import net.rickcee.tradematcher.Automatcher;
import net.rickcee.tradematcher.IMatchable;
import net.rickcee.tradematcher.data.TradePopulation;
import net.rickcee.tradematcher.model.Trade;
import net.rickcee.tradematcher.util.BlockTradeFinder;

/**
 * @author rickcee
 *
 */
public class RunAutomatcher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Automatcher engine = new Automatcher();
		for (IMatchable trade : TradePopulation.ALLOC_TRADES) {
			engine.addAllocTradeToCache(trade);
		}

		BlockTradeFinder btf;
		for (Trade blockTrade : TradePopulation.BLOCK_TRADES) {
			btf = new BlockTradeFinder(blockTrade.getQuantity());
			ArrayList<IMatchable> allocationsForBlock = engine.getAllocCacheByKey().get(blockTrade.getMatchKey());
			if (allocationsForBlock != null) {
				Stack<? extends IMatchable> result = btf.findAllocationsForBlock(allocationsForBlock);
				System.out.println("Result for Block [" + blockTrade.getBlockAccountId() + "/" + blockTrade.getQuantity() + "]: " + result);
				allocationsForBlock.removeAll(result);
			}
		}
	}

}
