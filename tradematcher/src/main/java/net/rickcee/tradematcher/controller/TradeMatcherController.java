package net.rickcee.tradematcher.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.rickcee.tradematcher.Automatcher;
import net.rickcee.tradematcher.IMatchable;
import net.rickcee.tradematcher.data.TradePopulation;
import net.rickcee.tradematcher.event.AllocationTradePublisher;
import net.rickcee.tradematcher.event.BlockTradePublisher;
import net.rickcee.tradematcher.model.Trade;

@RestController
@RequestMapping("/v1/")
public class TradeMatcherController {
	
	@Autowired
	private Automatcher matcher;
    @Autowired
    private TaskExecutor taskExecutor;	
	@Autowired
	private BlockTradePublisher blockPublisher;
	@Autowired
	private AllocationTradePublisher allocPublisher;
	
	@RequestMapping(method = RequestMethod.GET, path = "/HealthCheck", produces = { "application/json" })
	public Object healthCheck() {
		HashMap<String, String> result = new HashMap<>();
		result.put("result", "OK");
		return result;
	}

    @RequestMapping(path = "/TestCase/AllAtOnce/BlockFirst", method = RequestMethod.GET, produces = "application/json")
    public Object testAllAtOnce() {
    	
    	for(IMatchable block : TradePopulation.BLOCK_TRADES) {
    		blockPublisher.sendBlockTradeEvent(block);
    	}
    	
    	for(IMatchable alloc : TradePopulation.ALLOC_TRADES) {
    		allocPublisher.sendAllocTradeEvent(alloc);
    	}
    	
		HashMap<String, Object> result = new HashMap<>();
		result.put("result", "OK");
		result.put("Unmatched Allocations", matcher.getAllocCacheByKey());
		result.put("Unmatched Blocks", matcher.getBlockCacheByKey());
		result.put("Matched Blocks", matcher.getMatchedBlockTrades());
		return result;
    }

    @RequestMapping(path = "/TestCase/AllAtOnce/AllocFirst", method = RequestMethod.GET, produces = "application/json")
    public Object testAllAtOnceAllocFirst() {
    	
    	for(IMatchable alloc : TradePopulation.ALLOC_TRADES) {
    		allocPublisher.sendAllocTradeEvent(alloc);
    	}
    	
    	for(IMatchable block : TradePopulation.BLOCK_TRADES) {
    		blockPublisher.sendBlockTradeEvent(block);
    	}
    	
		HashMap<String, Object> result = new HashMap<>();
		result.put("result", "OK");
		result.put("Unmatched Allocations", matcher.getAllocCacheByKey());
		result.put("Unmatched Blocks", matcher.getBlockCacheByKey());
		result.put("Matched Blocks", matcher.getMatchedBlockTrades());
		return result;
    }

    @RequestMapping(path = "/TestCase/AllAtOnce", method = RequestMethod.GET, produces = "application/json")
    public Object testAllAtOnceThread() {
    	
		for (IMatchable alloc : TradePopulation.ALLOC_TRADES) {
			taskExecutor.execute(new Runnable() {

				@Override
				public void run() {
					allocPublisher.sendAllocTradeEvent(alloc);
				}
			});
		}
    	
		for (IMatchable block : TradePopulation.BLOCK_TRADES) {
			taskExecutor.execute(new Runnable() {

				@Override
				public void run() {
					blockPublisher.sendBlockTradeEvent(block);
				}
			});
		}

		HashMap<String, Object> result = new HashMap<>();
		result.put("result", "OK");
		return result;
    }

    @RequestMapping(path = "/TestCase/1", method = RequestMethod.GET, produces = "application/json")
    public Object testCase1() throws Exception {


		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				blockPublisher.sendBlockTradeEvent(
						new Trade(10001L, "S", 98.997, 0.0, 75000000L, "912810SF6", "BLOCK_ACCT_1", "-"));
			}
		});
    	
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				allocPublisher.sendAllocTradeEvent(
						new Trade(10100L, "S", 98.997, 367.0, 10000000L, "912810SF6", "BLOCK_ACCT_1", "10001-100"));
			}
		});
    	
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				allocPublisher.sendAllocTradeEvent(
						new Trade(10101L, "S", 98.997, 367.0, 7500000L, "912810SF6", "BLOCK_ACCT_1", "10001-101"));
			}
		});
    	
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				allocPublisher.sendAllocTradeEvent(
						new Trade(10102L, "S", 98.997, 367.0, 7500000L, "912810SF6", "BLOCK_ACCT_1", "10001-102"));
			}
		});
		
    	
//		for (int i = 1; i <= 50; i++) {
//			System.out.println(i);
//			final int j = i;
//			taskExecutor.execute(new Runnable() {
//				@Override
//				public void run() {
//					allocPublisher.sendAllocTradeEvent(
//							new Trade((500L + j), "S", 98.997, 367.0, 1000000L, "912810SF6", "BLOCK_ACCT_1", "10001-" + j));
//				}
//			});
//			Thread.sleep(50);
//		}
		
		for (int i = 1; i <= 50; i++) {
			final int j = i;
			allocPublisher.sendAllocTradeEvent(
					new Trade((500L + j), "S", 98.997, 367.0, 1000000L, "912810SF6", "BLOCK_ACCT_1", "10001-" + j));
			Thread.sleep(50);
		}
		
		HashMap<String, Object> result = new HashMap<>();
		result.put("result", "OK");
		return result;
    }

    @RequestMapping(path = "/TestCase/2", method = RequestMethod.GET, produces = "application/json")
    public Object testCase2() throws Exception {

    	
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				allocPublisher.sendAllocTradeEvent(
						new Trade(10102L, "S", 98.997, 367.0, 85000000L, "912810SF6", "BLOCK_ACCT_1", "10001-102"));
			}
		});
		
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				blockPublisher.sendBlockTradeEvent(
						new Trade(10001L, "S", 98.997, 0.0, 75000000L, "912810SF6", "BLOCK_ACCT_1", "-"));
			}
		});
    	
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				allocPublisher.sendAllocTradeEvent(
						new Trade(10100L, "S", 98.997, 367.0, 10000000L, "912810SF6", "BLOCK_ACCT_1", "10001-100"));
			}
		});
    	
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				allocPublisher.sendAllocTradeEvent(
						new Trade(10101L, "S", 98.997, 367.0, 7500000L, "912810SF6", "BLOCK_ACCT_1", "10001-101"));
			}
		});
    	
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				allocPublisher.sendAllocTradeEvent(
						new Trade(10102L, "S", 98.997, 367.0, 7500000L, "912810SF6", "BLOCK_ACCT_1", "10001-102"));
			}
		});
		
		for (int i = 1; i <= 50; i++) {
			final int j = i;
			allocPublisher.sendAllocTradeEvent(
					new Trade((500L + j), "S", 98.997, 367.0, 1000000L, "912810SF6", "BLOCK_ACCT_1", "10001-" + j));
			Thread.sleep(50);
		}
    	
//		taskExecutor.execute(new Runnable() {
//			@Override
//			public void run() {
//				allocPublisher.sendAllocTradeEvent(
//						new Trade(10102L, "S", 98.997, 367.0, 7500000L, "912810SF6", "BLOCK_ACCT_1", "10001-103"));
//			}
//		});
		
		HashMap<String, Object> result = new HashMap<>();
		result.put("result", "OK");
		return result;
    }

    @RequestMapping(path = "/TestCase/2a", method = RequestMethod.GET, produces = "application/json")
    public Object testCase2Bis1() throws Exception {
    	
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				allocPublisher.sendAllocTradeEvent(
						new Trade(10102L, "S", 98.997, 367.0, 7500000L, "912810SF6", "BLOCK_ACCT_1", "10001-103"));
			}
		});
		
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				blockPublisher.sendBlockTradeEvent(
						new Trade(10001L, "S", 98.997, 0.0, 85000000L, "912810SF6", "BLOCK_ACCT_1", "-"));
			}
		});
		
		HashMap<String, Object> result = new HashMap<>();
		result.put("result", "OK");
		return result;

    }
    	
    @RequestMapping(path = "/Matcher/Status", method = RequestMethod.GET, produces = "application/json")
    public Object matcherStatus() {

		HashMap<String, Object> result = new HashMap<>();
		result.put("Unmatched Allocations", matcher.getAllocCacheByKey());
		result.put("Unmatched Blocks", matcher.getBlockCacheByKey());
		result.put("Matched Blocks", matcher.getMatchedBlockTrades());
		return result;
    	
    }
}