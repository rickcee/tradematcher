/**
 * 
 */
package net.rickcee.tradematcher.event;

import org.springframework.context.ApplicationEvent;

import net.rickcee.matcher.deprecated.IMatchable;

/**
 * @author rickcee
 *
 */
public class TradeEvent extends ApplicationEvent {

	private static final long serialVersionUID = 4136142160912130847L;

	IMatchable trade;

	/**
	 * Constructor
	 * 
	 * @param source The source of the event
	 * @param trade  The trade object to transfer
	 */
	public TradeEvent(String source, IMatchable trade) {
		super(source);
		this.trade = trade;
	}

	/**
	 * @return the trade
	 */
	public IMatchable getTrade() {
		return trade;
	}

}
