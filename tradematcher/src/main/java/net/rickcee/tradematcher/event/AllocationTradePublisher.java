/**
 * 
 */
package net.rickcee.tradematcher.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import net.rickcee.matcher.deprecated.IMatchable;

/**
 * @author rickcee
 *
 */
@Component
public class AllocationTradePublisher {

	@Autowired
	private ApplicationEventPublisher evtPublisher;

	public void sendAllocTradeEvent(final IMatchable blockTrade) {
		evtPublisher.publishEvent(new TradeEvent("RCNET_ALLOC_FEED", blockTrade));
	}

}
