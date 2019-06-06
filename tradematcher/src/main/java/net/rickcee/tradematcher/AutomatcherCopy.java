/**
 * 
 */
package net.rickcee.tradematcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.rickcee.tradematcher.event.TradeEvent;
import net.rickcee.tradematcher.util.BlockTradeFinder;

/**
 * @author rickcee
 *
 */
@Getter
//@Component
@Slf4j
public class AutomatcherCopy implements ApplicationListener<TradeEvent>{

	@Autowired
	ApplicationEventPublisher evtPublisher;
	
	private ConcurrentMap<String, ArrayList<IMatchable>> allocCacheByKey = new ConcurrentHashMap<>();
	private ConcurrentMap<String, ArrayList<IMatchable>> blockCacheByKey = new ConcurrentHashMap<>();
	//private ConcurrentMap<String, IMatchable> unmatchedBlockTrades = new ConcurrentHashMap<>();
	private Set<IMatchable> matchedBlockTrades = new HashSet<>();
	
	public synchronized void addAllocTradeToCache(IMatchable trade) {
		ArrayList<IMatchable> trades = allocCacheByKey.get(trade.getMatchKey());
		if (trades == null) {
			trades = new ArrayList<IMatchable>();
			allocCacheByKey.put(trade.getMatchKey(), trades);
		}
		log.debug("Adding to alloc cache: [" + trade.getMatchKey() + "]");
		
		// Remove if exists (in case the new version's details changed)
		if (trades.contains(trade)) {
			trades.remove(trade);
		}
		trades.add(trade);
	}
	
	public synchronized void addBlockTradeToCache(IMatchable trade) {
		log.debug("Adding to block cache: [" + trade.getMatchKey() + "]");
		ArrayList<IMatchable> trades = blockCacheByKey.get(trade.getMatchKey());
		if (trades == null) {
			trades = new ArrayList<IMatchable>();
			
			blockCacheByKey.put(trade.getMatchKey(), trades);
		}
		synchronized (trades) {
			trades.add(trade);
		}
	}
	
	protected synchronized IMatchable checkForMatch(IMatchable blockTrade) {
		BlockTradeFinder btf;
		btf = new BlockTradeFinder(blockTrade.getQuantity());
		synchronized (blockTrade) {
		ArrayList<IMatchable> allocationsForBlock = allocCacheByKey.get(blockTrade.getMatchKey());

		if (allocationsForBlock != null) {
			
//			synchronized (allocationsForBlock) {
//				log.info("allocationsForBlock: " + allocationsForBlock);
				Stack<? extends IMatchable> result = btf.findAllocationsForBlock(allocationsForBlock);
				if(result.size() > 0) {
					// Here's where the breakdown needs to happen
					log.info("Breaking down Block Trade: " + blockTrade);
					for (IMatchable alloc : result) {
						log.info(" +-------------------> " + alloc);
					}

					// Once the breakdown is done, we remove the allocs from cache
					allocationsForBlock.removeAll(result);
					// If no allocations left, remove entry from cache.
					if (allocationsForBlock.size() == 0) {
						allocCacheByKey.remove(blockTrade.getMatchKey());
					}
					
						log.info("Removing from Unmatched: " + blockTrade.getUID());
						blockCacheByKey.get(blockTrade.getMatchKey()).remove(blockTrade);
						log.info("Adding to Matched: " + blockTrade.getUID());
						matchedBlockTrades.add(blockTrade);
					}
					
					// Return the matched block trade to safely remove it from the collection
					return blockTrade;
				}

//			}
		
		}
		log.info("No Match Found for Block [" + blockTrade.getUID() + "][" + blockTrade.getMatchKey() + "]");
		return null;
	}

	@Override
	public void onApplicationEvent(TradeEvent event) {
		if ("RCNET_BLOCK_FEED".equals(event.getSource())) {
			addBlockTradeToCache(event.getTrade());
			// Try to find Allocs for the Block
			checkForMatch(event.getTrade());
		} else {
			addAllocTradeToCache(event.getTrade());
			findPotentialBlockForAlloc(event);
		}
	}

	private synchronized void findPotentialBlockForAlloc(TradeEvent event) {
		// Try to get a Pairing Block for the received Alloc
		ArrayList<IMatchable> blockTrades = blockCacheByKey.get(event.getTrade().getMatchKey());
		if (blockTrades != null && blockTrades.size() > 0) {
			synchronized (blockTrades) {
				IMatchable[] copy = new IMatchable[blockTrades.size()];
				blockTrades.toArray(copy);
				for (IMatchable block : copy) {
					IMatchable matchedBlockTrade = checkForMatch(block);
					// Remove block trade
//					if (matchedBlockTrade != null) {
//						log.info("Removing from Unmatched: " + matchedBlockTrade.getUID());
//						blockTrades.remove(matchedBlockTrade);
//						log.info("Adding to Matched: " + matchedBlockTrade.getUID());
//						matchedBlockTrades.add(matchedBlockTrade);
//					}
				}
				// If no blocks left, remove entry from cache.
				if (blockTrades.size() == 0) {
					blockCacheByKey.remove(event.getTrade().getMatchKey());
				}
			}
//			synchronized (blockTrades) {
//				IMatchable matchedBlockTrade;
//				if (blockTrades.size() > 0) {
//					Iterator<IMatchable> itr = blockTrades.iterator();
//					while(itr.hasNext()) {
//						matchedBlockTrade = checkForMatch(itr.next());
//						// Remove block trade
//						if (matchedBlockTrade != null) {
//							itr.remove();
//							matchedBlockTrades.add(matchedBlockTrade);
//						}
//					}
//				}
//				// If no blocks left, remove entry from cache.
//				if(blockTrades.size() == 0) {
//					blockCacheByKey.remove(event.getTrade().getMatchKey());
//				}
//			}
		} else {
			log.info("No Potential Block Match for Allocation [" + event.getTrade().getUID() + "]["
					+ event.getTrade().getMatchKey() + "]");
		}
	}

	
}
