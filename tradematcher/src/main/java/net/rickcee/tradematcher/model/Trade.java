/**
 * 
 */
package net.rickcee.tradematcher.model;

import java.util.StringJoiner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.rickcee.tradematcher.IMatchable;

/**
 * @author rickcee
 *
 */
@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class Trade implements IMatchable, Comparable<Trade> {
	private Long id;
	private String buySell;
	private Double price;
	private Double accruedInterest;
	private Long quantity;
	private String cusip;
	private String blockAccountId;
	private String subAccountId;
	
	@Override
	public String getMatchKey() {
		StringJoiner sj = new StringJoiner("-");
		return sj.add(buySell).add(cusip).add(blockAccountId).add(price.toString()).toString();
	}

	@Override
	public String getUID() {
		return id.toString();
	}

	@Override
	public String toString() {
		return "Trade [id=" + id + ", quantity=" + quantity + "]";
	}

	@Override
	public int compareTo(Trade o) {
		return quantity.compareTo(o.quantity);
	}

}
