/**
 * 
 */
package net.rickcee.tradematcher;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.context.annotation.Description;

import lombok.extern.slf4j.Slf4j;
import net.rickcee.tradematcher.model.RCTrade;
import net.rickcee.tradematcher.util.TradeGenerator;

/**
 * @author rickcee
 *
 */
@TestMethodOrder(OrderAnnotation.class)
@Slf4j
public class TestCase1 {
	private TradeMatcher engine = new TradeMatcher();
	private TradeGenerator tg = new TradeGenerator();

	/**
	 * Constructor will add BLOCK trades.
	 */
	@BeforeEach
	public void init() {
		log.info(" - Init -");
		for (RCTrade trade : TradeGenerator.TRADE_POOL) {
			engine.processInternalTradeMessage(trade);
		}
	}
	
	@Test
	@Description("Multiple trade matching scenario.")
	@Order(1)
	public void check_Trade_Matching() throws Exception {
		/* Trade 1, should MATCH */
		Set<RCTrade> allocs = tg.generateAllocs(TradeGenerator.TRADE_POOL[0], 100, 10);
		for (RCTrade trade : allocs) {
			engine.processExternalTradeMessage(trade);
		}
		assertTrue(engine.getMatchedInternalTrades().contains(TradeGenerator.TRADE_POOL[0]));
		assertNull(engine.getMatchingMap().get(TradeGenerator.TRADE_POOL[0].getMatchKey()));

		/* Trade 2, should NOT MATCH */
		allocs = tg.generateAllocs(TradeGenerator.TRADE_POOL[1], 400, 1);
		allocs.addAll(tg.generateAllocs(TradeGenerator.TRADE_POOL[1], 200, 1));
		for (RCTrade trade : allocs) {
			engine.processExternalTradeMessage(trade);
		}
		assertTrue(!engine.getMatchedInternalTrades().contains(TradeGenerator.TRADE_POOL[1]));
		assertNotNull(engine.getMatchingMap().get(TradeGenerator.TRADE_POOL[1].getMatchKey()));

		/* Trade 3, should MATCH */
		allocs = tg.generateAllocs(TradeGenerator.TRADE_POOL[2], 5, 30);
		allocs.addAll(tg.generateAllocs(TradeGenerator.TRADE_POOL[2], 10, 10));
		for (RCTrade trade : allocs) {
			engine.processExternalTradeMessage(trade);
		}
		assertTrue(engine.getMatchedInternalTrades().contains(TradeGenerator.TRADE_POOL[2]));
		assertNull(engine.getMatchingMap().get(TradeGenerator.TRADE_POOL[2].getMatchKey()));

		/* Trade 2, should MATCH NOW */
		allocs = tg.generateAllocs(TradeGenerator.TRADE_POOL[1], 25, 4);
		for (RCTrade trade : allocs) {
			engine.processExternalTradeMessage(trade);
		}
		/* Should be only 1 trade left unmatched */
		assertEquals(1, engine.getMatchingMap().values().size());
		assertTrue(engine.getMatchedInternalTrades().contains(TradeGenerator.TRADE_POOL[1]));
		assertTrue(engine.getMatchingMap().keySet().contains(TradeGenerator.TRADE_POOL[1].getMatchKey()));
	}


}
