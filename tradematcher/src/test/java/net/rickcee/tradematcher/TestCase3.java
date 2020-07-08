/**
 * 
 */
package net.rickcee.tradematcher;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import lombok.extern.slf4j.Slf4j;
import net.rickcee.tradematcher.model.RCTrade;
import net.rickcee.tradematcher.util.TradeGenerator;

/**
 * @author rickcee
 *
 */
@TestMethodOrder(OrderAnnotation.class)
@Slf4j
public class TestCase3 {
	private TradeMatcher engine = new TradeMatcher();
	private TradeGenerator tg = new TradeGenerator();
	
	@Test
	@Order(1)
	public void check_Trade_Matching() throws Exception {
		
		RCTrade blockTradeToProcess = TradeGenerator.TRADE_POOL[3];
		double allocPrice = 0.0;
		Set<RCTrade> allocs;
		
		/* Step 1, received 100 pieces of 100000, total 10mm */
		allocs = tg.generateAllocs(blockTradeToProcess, 100, 100);
		for (RCTrade trade : allocs) {
			allocPrice = trade.getPrice();
			engine.processExternalTradeMessage(trade);
		}
		assertEquals(100, engine.getMatchingMap().get(blockTradeToProcess.getMatchKey()).get(allocPrice).size());
		
		/* Step 2, received 400 pieces of 50000, total 20mm */
		allocs = tg.generateAllocs(blockTradeToProcess, 50, 400);
		for (RCTrade trade : allocs) {
			allocPrice = trade.getPrice();
			engine.processExternalTradeMessage(trade);
		}
		assertEquals(500, engine.getMatchingMap().get(blockTradeToProcess.getMatchKey()).get(allocPrice).size());
		
		/* Step 3, push BLOCK trade */
		engine.processInternalTradeMessage(blockTradeToProcess);
		assertEquals(1, engine.unmatchedInternalTrades.get(blockTradeToProcess.getMatchKey()).size());
		assertEquals(blockTradeToProcess, engine.unmatchedInternalTrades.get(blockTradeToProcess.getMatchKey()).get(0));

		/* Step 4, received 1 piece of 20mm */
		allocs = tg.generateAllocs(blockTradeToProcess, 20000, 1);
		for (RCTrade trade : allocs) {
			allocPrice = trade.getPrice();
			engine.processExternalTradeMessage(trade);
		}
		/* Block trade is part of Internal Trade Matching */
		assertTrue(engine.matchedInternalTrades.contains(blockTradeToProcess));
		/* No pending allocations for Block Trade */
		assertFalse(engine.matchingMap.containsKey(blockTradeToProcess.getMatchKey()));
		
		log.info("Matching Map: " + engine.getMatchingMap());

	}

}
