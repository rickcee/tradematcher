/**
 * 
 */
package net.rickcee.tradematcher.test;

import java.util.ArrayList;
import java.util.Stack;

import lombok.extern.slf4j.Slf4j;
import net.rickcee.tradematcher.Automatcher;
import net.rickcee.tradematcher.IMatchable;
import net.rickcee.tradematcher.data.TradePopulation;
import net.rickcee.tradematcher.model.Trade;
import net.rickcee.tradematcher.util.BlockTradeFinder;

/**
 * @author rickcee
 *
 */
@Slf4j
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

				long allocSum = 0L;
				for(IMatchable trade : result) {
					allocSum += trade.getQuantity();
				}
				log.info(" ====> MATCH Result for Block [" + blockTrade.getId() + "/" + blockTrade.getBlockAccountId()
						+ "/" + blockTrade.getQuantity() + "]: (" + result.size() + ") - " + allocSum);
				log.debug("Allocation Result: " + result);
				//allocationsForBlock.removeAll(result);
				engine.removeAllocTradeFromCache(result);
			}
		}
	}

}
