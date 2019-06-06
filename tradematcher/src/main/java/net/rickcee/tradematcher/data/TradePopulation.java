/**
 * 
 */
package net.rickcee.tradematcher.data;

import java.util.ArrayList;

import net.rickcee.tradematcher.model.Trade;

/**
 * @author rickcee
 *
 */
public class TradePopulation {

	public static ArrayList<Trade> BLOCK_TRADES = new ArrayList<>();

	public static ArrayList<Trade> ALLOC_TRADES = new ArrayList<>();
	
	static {
		BLOCK_TRADES.add(new Trade(1000L, "B", 98.123, 0.0, 3500000L, "912810SH2", "BLACCT01", "-"));
		BLOCK_TRADES.add(new Trade(2000L, "B", 98.123, 0.0, 3500000L, "912810SH2", "BLACCT01", "-"));
		BLOCK_TRADES.add(new Trade(3000L, "B", 98.123, 0.0, 3000000L, "912810SH2", "BLACCT01", "-"));
		BLOCK_TRADES.add(new Trade(1001L, "S", 99.998, 0.0, 2000000L, "912810SF6", "BLACCT02", "-"));
		BLOCK_TRADES.add(new Trade(1002L, "S", 98.997, 0.0, 75000000L, "912810SF6", "BLACCT03", "-"));
		BLOCK_TRADES.add(new Trade(1003L, "B", 99.991, 0.0, 4000000L, "912810SE9", "BLACCT04", "-"));
		
		
		ALLOC_TRADES.add(new Trade(1L, "B", 98.123, 334.0, 1500000L, "912810SH2", "BLACCT01", "01-001"));
		ALLOC_TRADES.add(new Trade(2L, "B", 98.123, 123.0, 1000000L, "912810SH2", "BLACCT01", "01-002"));
		ALLOC_TRADES.add(new Trade(4L, "B", 98.123, 356.0, 3500000L, "912810SH2", "BLACCT01", "01-003"));
		ALLOC_TRADES.add(new Trade(7L, "B", 98.123, 2754.0, 500000L, "912810SH2", "BLACCT01", "01-004"));
		
		ALLOC_TRADES.add(new Trade(3L, "S", 99.998, 1765.0, 1000000L, "912810SF6", "BLACCT02", "02-001"));
		ALLOC_TRADES.add(new Trade(5L, "S", 99.998, 2765.0, 1000000L, "912810SF6", "BLACCT02", "02-002"));
		
		ALLOC_TRADES.add(new Trade(9L, "S", 98.997, 367.0, 75000000L, "912810SF6", "BLACCT03", "03-001"));
		
		ALLOC_TRADES.add(new Trade(6L, "B", 99.991, 2364.0, 1500000L, "912810SE9", "BLACCT04", "04-001"));
		ALLOC_TRADES.add(new Trade(8L, "B", 99.991, 754.0, 2500000L, "912810SE9", "BLACCT04", "04-002"));
	}

}
