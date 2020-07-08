/**
 * 
 */
package net.rickcee.tradematcher;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
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
public class TestCase2 {
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
	@Order(1)
	public void check_trade_matching_with_lower_tolerance() throws Exception {
		/* Trade 1 price is 99.105 */
		Set<RCTrade> allocs = tg.generateAllocs(TradeGenerator.TRADE_POOL[0], 100, 10);
		for (RCTrade trade : allocs) {
			/* Modify Allocation Price to test tolerance */
			trade.setPrice(trade.getPrice() - TradeMatcher.TOLERANCE);
			engine.processExternalTradeMessage(trade);
		}
		assertTrue(engine.getMatchedInternalTrades().contains(TradeGenerator.TRADE_POOL[0]));
		assertNull(engine.getMatchingMap().get(TradeGenerator.TRADE_POOL[0].getMatchKey()));
	}
	
	@Test
	@Order(2)
	public void check_trade_matching_with_greater_tolerance() throws Exception {
		/* Trade 1 price is 99.105 */
		Set<RCTrade> allocs = tg.generateAllocs(TradeGenerator.TRADE_POOL[0], 100, 10);
		for (RCTrade trade : allocs) {
			/* Modify Allocation Price to test tolerance */
			trade.setPrice(trade.getPrice() + TradeMatcher.TOLERANCE);
			engine.processExternalTradeMessage(trade);
		}
		assertTrue(engine.getMatchedInternalTrades().contains(TradeGenerator.TRADE_POOL[0]));
		assertNull(engine.getMatchingMap().get(TradeGenerator.TRADE_POOL[0].getMatchKey()));
	}
	
	@Test
	@Order(3)
	public void check_trade_matching_fails_with_lower_tolerance() throws Exception {
		/* Trade 1 price is 99.105 */
		Set<RCTrade> allocs = tg.generateAllocs(TradeGenerator.TRADE_POOL[0], 100, 10);
		for (RCTrade trade : allocs) {
			/* Modify Allocation Price to test tolerance, should fail */
			trade.setPrice(trade.getPrice() - (TradeMatcher.TOLERANCE + 0.01));
			engine.processExternalTradeMessage(trade);
		}
		assertFalse(engine.getMatchedInternalTrades().contains(TradeGenerator.TRADE_POOL[0]));
		assertNotNull(engine.getMatchingMap().get(TradeGenerator.TRADE_POOL[0].getMatchKey()));
	}
	
	@Test
	@Order(4)
	public void check_trade_matching_fails_with_greater_tolerance() throws Exception {
		/* Trade 1 price is 99.105 */
		Set<RCTrade> allocs = tg.generateAllocs(TradeGenerator.TRADE_POOL[0], 100, 10);
		for (RCTrade trade : allocs) {
			/* Modify Allocation Price to test tolerance, should fail */
			trade.setPrice(trade.getPrice() + (TradeMatcher.TOLERANCE + 0.01));
			engine.processExternalTradeMessage(trade);
		}
		assertFalse(engine.getMatchedInternalTrades().contains(TradeGenerator.TRADE_POOL[0]));
		assertNotNull(engine.getMatchingMap().get(TradeGenerator.TRADE_POOL[0].getMatchKey()));
	}
}
