/**
 * 
 */
package net.rickcee.tradematcher.util;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.rickcee.tradematcher.model.RCTrade;

/**
 * @author rickcee
 *
 */
@Slf4j
public class TradeGenerator {
	private ObjectMapper mapper = new ObjectMapper();
	private Random random = new Random();
	
	private static final Date tradeDate = new Date();
	private static final Date settleDate = java.sql.Date.valueOf(LocalDate.now().plusDays(2));
	
	static {
	}
	
	public static final RCTrade[] TRADE_POOL = {
			/* IMPORTANT !!!! - DO NOT MODIFY THESE TRADES, ORDER MATTERS FOR TEST CASES */
			new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC1, RCConstants.BUY, tradeDate, settleDate, RCConstants.EURO, 99.105, 105.00, 1000000L, "XS2201877300", "MAIN_ACCT1", null, true),
			new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC2, RCConstants.SELL, tradeDate, settleDate, RCConstants.EURO, 102.33, 1050.00, 500000L, "DE000DFL6PS2", "MAIN_ACCT_2", null, true),
			new RCTrade(UUID.randomUUID().toString(), RCConstants.LE2, RCConstants.SRC1, RCConstants.BUY, tradeDate, settleDate, RCConstants.USD, 99.998, 1250.29, 250000L, "US7562071065", "MAIN_ACCT_3", null, true),
			new RCTrade(UUID.randomUUID().toString(), RCConstants.LE2, RCConstants.SRC2, RCConstants.BUY, tradeDate, settleDate, RCConstants.USD, 99.156, 23690.79, 50000000L, "US032511BN64", "MAIN_ACCT_4", null, true),
			new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC2, RCConstants.SELL, tradeDate, settleDate, RCConstants.EURO, 102.33, 1050.00, 500000L, "DE000DFL6PS2", "MAIN_ACCT_2", null, true),
			new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC2, RCConstants.SELL, tradeDate, settleDate, RCConstants.EURO, 102.33, 1050.00, 500000L, "DE000DFL6PS2", "MAIN_ACCT_2", null, true),
			new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC2, RCConstants.SELL, tradeDate, settleDate, RCConstants.EURO, 102.33, 1050.00, 250000L, "DE000DFL6PS2", "MAIN_ACCT_2", null, true),
	};
	

	public Set<RCTrade> generateAllocs(RCTrade trade, int... allocs) throws Exception {
		Set<RCTrade> allocTrades = new HashSet<>();
		String jsonAlloc;
		RCTrade allocTrade;
		Long totalAllocSum = 0L;
		Long allocQty = 0L;
		for(long qty : allocs) {
			jsonAlloc = mapper.writeValueAsString(trade);
			allocQty = qty * 1000L;
			totalAllocSum += allocQty;
			allocTrade = mapper.readValue(jsonAlloc, RCTrade.class);
			allocTrade.setUuid(UUID.randomUUID().toString());
			allocTrade.setQuantity(allocQty);
			allocTrade.setAccountId("ACCT_" + random.nextInt(10));;
			allocTrades.add(allocTrade);
		}
		
		log.info("Generated (" + allocTrades.size() + ") allocations, total qty: " + totalAllocSum);
		return allocTrades;
	}

	public Set<RCTrade> generateAllocs(RCTrade trade, long allocQty, int numberAllocs) throws Exception {
		Set<RCTrade> allocTrades = new HashSet<>();
		String jsonAlloc;
		RCTrade allocTrade;
		Long totalAllocSum = 0L;
		Long allocQtyCalc = 0L;
		for(int i=0; i<numberAllocs; i++) {
			jsonAlloc = mapper.writeValueAsString(trade);
			allocQtyCalc = allocQty * 1000L;
			totalAllocSum += allocQtyCalc;
			allocTrade = mapper.readValue(jsonAlloc, RCTrade.class);
			allocTrade.setUuid(UUID.randomUUID().toString());
			allocTrade.setQuantity(allocQtyCalc);
			allocTrade.setAccountId("ACCT_" + random.nextInt(10));
			allocTrade.setBlock(false);
			allocTrades.add(allocTrade);
		}
		
		log.info("Generated (" + allocTrades.size() + ") allocations, total qty: " + totalAllocSum);
		return allocTrades;
	}
	
	public static void main(String args[]) throws Exception {
		Date tradeDate = new Date();
		Date settleDate = java.sql.Date.valueOf(LocalDate.now().plusDays(2));
		RCTrade trade = new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC1, RCConstants.BUY, tradeDate, settleDate, RCConstants.EURO,
				99.105, 105.00, 1000000L, "XS2201877300", "MAIN_ACCT_1", null, true);
		TradeGenerator tg = new TradeGenerator();
		//System.out.println(generateAllocs(trade, 100,100,100,100,100,50,50,25,25,25,25,25,25,5,5,5,5,5,5,5,5,5,5,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10 ));
		System.out.println(tg.generateAllocs(trade, 100, 5 ));
		System.out.println(tg.generateAllocs(trade, 50, 2 ));
		System.out.println(tg.generateAllocs(trade, 25, 6 ));
		System.out.println(tg.generateAllocs(trade, 5, 10 ));
		System.out.println(tg.generateAllocs(trade, 10, 20 ));
	}

}
