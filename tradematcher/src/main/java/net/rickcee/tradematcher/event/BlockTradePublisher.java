/**
 * 
 */
package net.rickcee.tradematcher.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import net.rickcee.tradematcher.IMatchable;

/**
 * @author rickcee
 *
 */
@Component
public class BlockTradePublisher {

	@Autowired
	private ApplicationEventPublisher evtPublisher;

	public void sendBlockTradeEvent(final IMatchable blockTrade) {
		evtPublisher.publishEvent(new TradeEvent("RCNET_BLOCK_FEED", blockTrade));
	}
}
