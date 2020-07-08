/**
 * 
 */
package net.rickcee.tradematcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.rickcee.tradematcher.model.RCTrade;

/**
 * @author rickcee
 *
 */
@Component
@Slf4j
@Getter
public class TradeMatcher {

	public static final Double TOLERANCE = 0.05;
	
	public Map<String, List<RCTrade>> unmatchedInternalTrades = new HashMap<>();
	public Set<RCTrade> matchedInternalTrades = new HashSet<>();
	
	public Map<String, Map<Double, List<RCTrade>>> matchingMap = new HashMap<>();
	
	public synchronized void addTradeToMap(RCTrade trade) {
		log.debug("Adding Trade: " + trade);
		String matchKey = trade.getMatchKey();
		Map<Double, List<RCTrade>> priceMap = matchingMap.get(matchKey);
		List<RCTrade> trades;
		if (priceMap != null) {
			trades = priceMap.get(trade.getPrice());
			if (trades == null) {
				trades = new ArrayList<RCTrade>();
			}
		} else {
			priceMap = new HashMap<Double, List<RCTrade>>();
			trades = new ArrayList<RCTrade>();
			priceMap.put(trade.getPrice(), trades);
			matchingMap.put(matchKey, priceMap);
		}
		if (trades.contains(trade)) {
			trades.remove(trade);
		}
		trades.add(trade);
	}
	
	public synchronized void removeTradesFromMap(Collection<RCTrade> trades) {
		String matchKey;
		for (RCTrade trade : trades) {
			matchKey = trade.getMatchKey();
			Map<Double, List<RCTrade>> l1Matches = matchingMap.get(matchKey);
			if (l1Matches != null) {
				List<RCTrade> l2Matches = l1Matches.get(trade.getPrice());
				boolean removed = l2Matches.remove(trade);
				if (removed) {
					log.debug("Removed from MatchingMap: " + trade);
					if(l2Matches.size() == 0) {
						l1Matches.remove(trade.getPrice());
						if(l1Matches.values().size() == 0) {
							matchingMap.remove(matchKey);
						}
					}
				}
			}
		}
	}
	
	public synchronized RCTrade findMatchingTrade(RCTrade blockTrade) {
		String matchKey = blockTrade.getMatchKey();
		Map<Double, List<RCTrade>> l1Matched = matchingMap.get(matchKey);
		Boolean bdResult = false;
		if (l1Matched != null) {
			List<RCTrade> l2Matched = l1Matched.get(blockTrade.getPrice());
			if (l2Matched != null && l2Matched.size() > 0) {
				bdResult = processPotentialMatches(blockTrade, l2Matched);
				if(bdResult) {
					return blockTrade;
				}
			} else {
				// No exact MATCH, check other values using PRICE tolerance
				log.debug(" - No EXACT price match found, checking other trades using specified tolerance [" + TOLERANCE
						+ "]...");
				for(Double price : l1Matched.keySet()) {
					if(Math.abs(price - blockTrade.getPrice()) < TOLERANCE) {
						log.info("Matching Trade Found using TOLERANCE, block Price [" + blockTrade.getPrice()
								+ "], alloc Price [" + price + "]");
						l2Matched = l1Matched.get(price);
						bdResult = processPotentialMatches(blockTrade, l2Matched);
						if(bdResult) {
							// If matches found, break the cycle otherwise keep looking...
							break;
						}
					}
				}
				if(!bdResult) {
					log.info("No L2 matching for Trade " + blockTrade);
				}
			}
		} else {
			log.info("No L1 matching for Trade " + blockTrade);
		}
		return null;
	}

	private synchronized boolean processPotentialMatches(RCTrade blockTrade, List<RCTrade> l2Matched) {
		log.info("Potential Matches: " + l2Matched);
		BlockTradeFinder finder = new BlockTradeFinder(blockTrade.getQuantity());
		Collection<RCTrade> result = finder.findAllocationsForBlock(l2Matched);
		if(result.size() > 0) {
			log.info("Exact Matched Found (" + result.size() + "): " + result);
			
			boolean bdResult = breakdownTrade(blockTrade, result);
			if(bdResult) {
				log.info("Removing allocated entries (" + result.size() + "), adding matched block ["
						+ blockTrade.getUuid() + "]");
				removeTradesFromMap(result);
				matchedInternalTrades.add(blockTrade);
				// Not here
				//unmatchedInternalTrades.get(blockTrade.getMatchKey()).remove(blockTrade);
			}
			return bdResult;
		}
		return false;
	}
	
	public synchronized void processInternalTradeMessage(RCTrade internalTrade) {
		List<RCTrade> trades = unmatchedInternalTrades.get(internalTrade.getMatchKey());
		if(trades == null) {
			trades = new ArrayList<RCTrade>();
			unmatchedInternalTrades.put(internalTrade.getMatchKey(),  trades);
		}
		trades.add(internalTrade);
		RCTrade bdTrade = findMatchingTrade(internalTrade);
		if (bdTrade != null) {
			unmatchedInternalTrades.get(bdTrade.getMatchKey()).remove(bdTrade);
		}
	}
	
	public synchronized void processExternalTradeMessage(RCTrade externalTrade) {
		addTradeToMap(externalTrade);
		String matchingKey = externalTrade.getMatchKey();
		if (unmatchedInternalTrades.containsKey(matchingKey)) {
			List<RCTrade> potentialBlockTrades = unmatchedInternalTrades.get(matchingKey);
			if (potentialBlockTrades != null && potentialBlockTrades.size() > 0) {
				RCTrade bdTrade = null;
				for(Iterator<RCTrade> iterator = potentialBlockTrades.iterator(); iterator.hasNext(); ) {
					bdTrade = findMatchingTrade(iterator.next());
					if (bdTrade != null) {
//						unmatchedInternalTrades.get(bdTrade.getMatchKey()).remove(bdTrade);
						break;
					}
				}
				if (bdTrade != null) {
					unmatchedInternalTrades.get(bdTrade.getMatchKey()).remove(bdTrade);
				}
//				potentialBlockTrades.forEach(blockTrade -> {
//					findMatchingTrade(blockTrade);
//				});
			}
		}
	}
	
	public synchronized boolean breakdownTrade(RCTrade internalTrade, Collection<RCTrade> result) {
		log.info("================================================================");
		log.info(" = Parent Trade: " + internalTrade);
		log.info("================================================================");
		result.forEach(trade -> {
			log.info(" ===> Allocation Trade: " + trade);
		});
		log.info("================================================================");
		return true;
	}
	
	public static void main(String args[]) {
		
	}
	
}
