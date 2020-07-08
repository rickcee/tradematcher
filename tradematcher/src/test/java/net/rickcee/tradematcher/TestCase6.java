/**
 * 
 */
package net.rickcee.tradematcher;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
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
public class TestCase6 {
	private TradeMatcher engine = new TradeMatcher();
	private TradeGenerator tg = new TradeGenerator();
	
	@Test
	@DisplayName("Multiple Block Trade w/ Same Details - Sequencial, Allocs First")
	@Order(1)
	public void check_Trade_Matching() throws Exception {
		
		/* 3 trade of 500K each w/ same matching details */
		RCTrade blockTrade1 = TradeGenerator.TRADE_POOL[1];
		RCTrade blockTrade2 = TradeGenerator.TRADE_POOL[4];
		RCTrade blockTrade3 = TradeGenerator.TRADE_POOL[5];
		RCTrade blockTrade4 = TradeGenerator.TRADE_POOL[6];

		Set<RCTrade> allocs;
		
		/* Step 1, received 10 pieces of 50000, total 500k */
		allocs = tg.generateAllocs(blockTrade1, 50, 10);
		for (RCTrade trade : allocs) {
			engine.processExternalTradeMessage(trade);
		}
		
		/* Step 2, received 100 pieces of 500, total 500k */
		allocs = tg.generateAllocs(blockTrade2, 5, 100);
		for (RCTrade trade : allocs) {
			engine.processExternalTradeMessage(trade);
		}
		
		/* Step 3, received 20 pieces of 25k, total 500k */
		allocs = tg.generateAllocs(blockTrade3, 25, 20);
		for (RCTrade trade : allocs) {
			engine.processExternalTradeMessage(trade);
		}

		/* Step 4, send Block Trades */
		engine.processInternalTradeMessage(blockTrade4);
		assertEquals(1, engine.matchedInternalTrades.size());
		engine.processInternalTradeMessage(blockTrade1);
		assertEquals(2, engine.matchedInternalTrades.size());
		engine.processInternalTradeMessage(blockTrade2);
		assertEquals(3, engine.matchedInternalTrades.size());
		engine.processInternalTradeMessage(blockTrade3);
		assertEquals(3, engine.matchedInternalTrades.size());
		
		/* Step 5, received 5 pieces of 50k, total 250k */
		allocs = tg.generateAllocs(blockTrade3, 50, 5);
		for (RCTrade trade : allocs) {
			engine.processExternalTradeMessage(trade);
		}
		assertEquals(4, engine.matchedInternalTrades.size());
		
		/* No pending allocations for Block Trade */
		assertFalse(engine.matchingMap.containsKey(blockTrade1.getMatchKey()));
		
		log.info("Matching Map: " + engine.getMatchingMap());

	}

}
