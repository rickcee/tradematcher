/**
 * 
 */
package net.rickcee.tradematcher.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Include;
import net.rickcee.tradematcher.TradeMatcher;
import net.rickcee.tradematcher.util.RCConstants;

/**
 * @author rickcee
 *
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class RCTrade implements Comparable<RCTrade> {

	@lombok.EqualsAndHashCode.Include
	private String uuid;
	@Include
	private String entity;
	private String source;
	@Include
	private String buySell;
	private Date tradeDate;
	private Date settleDate;
	private String currency;
	private Double price;
	private Double accruedInterest;
	@Include
	private Long quantity;
	private String security;
	@Include
	private String mainAccountId;
	@Include
	private String accountId;
	private boolean isBlock;

	@JsonIgnore
	public String getMatchKey() {
		StringJoiner sj = new StringJoiner("-");
		return sj.add(entity).add(mainAccountId).add(buySell).add(RCConstants.getDate(tradeDate)).add(RCConstants.getDate(settleDate))
				.add(security).add(currency).toString();
	}

//	@Override
//	public String toString() {
//		return "Trade [uuid=" + uuid + ", quantity=" + quantity + "]";
//	}

	@Override
	public int compareTo(RCTrade o) {
		return quantity.compareTo(o.quantity);
	}

	public static void main(String args[]) {
		RCTrade trade, newAlloc;
		Date tradeDate = new Date();
		Date settleDate = java.sql.Date.valueOf(LocalDate.now().plusDays(2));
		TradeMatcher matcher = new TradeMatcher();
		
		trade = new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC1, RCConstants.BUY, tradeDate, settleDate, RCConstants.EURO,
				99.105, 105.00, 1000000L, "XS2201877300", null, "ACCT_1", true);
		System.out.println(trade.getMatchKey());
		matcher.addTradeToMap(trade);
		//System.out.println(matcher.getMatchingMap());
		
		trade = new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC1, RCConstants.BUY, tradeDate, settleDate, RCConstants.EURO,
				99.105, 1044.00, 250000L, "XS2201877300", null, "ACCT_1", true);
		System.out.println(trade.getMatchKey());
		matcher.addTradeToMap(trade);
		
		trade = new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC1, RCConstants.BUY, tradeDate, settleDate, RCConstants.EURO,
				99.105, 1044.00, 250000L, "XS2201877300", null, "ACCT_1", true);
		System.out.println(trade.getMatchKey());
		matcher.addTradeToMap(trade);
		
		trade = new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC1, RCConstants.BUY, tradeDate, settleDate, RCConstants.EURO,
				99.105, 1044.00, 250000L, "XS2201877300", null, "ACCT_1", true);
		System.out.println(trade.getMatchKey());
		matcher.addTradeToMap(trade);
		
		trade = new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC1, RCConstants.BUY, tradeDate, settleDate, RCConstants.EURO,
				99.105, 1044.00, 250000L, "XS2201877300", null, "ACCT_1", true);
		System.out.println(trade.getMatchKey());
		matcher.addTradeToMap(trade);
		//System.out.println(matcher.getMatchingMap());
		
		trade = new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC2, RCConstants.SELL, tradeDate, settleDate, RCConstants.EURO,
				102.33, 1050.00, 500000L, "DE000DFL6PS2", null, "ACCT_2", true);
		System.out.println(trade.getMatchKey());
		matcher.addTradeToMap(trade);
		//System.out.println(matcher.getMatchingMap());

		trade = new RCTrade(UUID.randomUUID().toString(), RCConstants.LE2, RCConstants.SRC1, RCConstants.BUY, tradeDate, settleDate, RCConstants.USD,
				99.998, 1250.29, 250000L, "US7562071065", null, "ACCT_3", true);
		System.out.println(trade.getMatchKey());
		matcher.addTradeToMap(trade);
		//System.out.println(matcher.getMatchingMap());

		trade = new RCTrade(UUID.randomUUID().toString(), RCConstants.LE2, RCConstants.SRC2, RCConstants.SELL, tradeDate, settleDate, RCConstants.USD,
				98.21, 13697.53, 5000000L, "US56845T3059", null, "ACCT_4", true);
		System.out.println(trade.getMatchKey());
		matcher.addTradeToMap(trade);
		//System.out.println(matcher.getMatchingMap());

		trade = new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC1, RCConstants.BUY, tradeDate, settleDate, RCConstants.EURO,
				99.105, 1149.00, 750000L, "XS2201877300", null, "ACCT_1", true);
		matcher.findMatchingTrade(trade);
		
		trade = new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC1, RCConstants.BUY, tradeDate, settleDate, RCConstants.EURO,
				99.105, 1149.00, 1000000L, "XS2201877300", null, "ACCT_1", true);
		matcher.findMatchingTrade(trade);
		
		trade = new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC1, RCConstants.BUY, tradeDate, settleDate, RCConstants.EURO,
				99.105, 1149.00, 500000L, "XS2201877300", null, "ACCT_1", true);
		matcher.findMatchingTrade(trade);
		
		newAlloc = new RCTrade(UUID.randomUUID().toString(), RCConstants.LE1, RCConstants.SRC1, RCConstants.BUY, tradeDate, settleDate, RCConstants.EURO,
				99.105, 1044.00, 250000L, "XS2201877300", null, "ACCT_1", true);
		System.out.println(newAlloc.getMatchKey());
		matcher.addTradeToMap(newAlloc);
		
		matcher.findMatchingTrade(trade);
		
		matcher.findMatchingTrade(trade);
	}

}
