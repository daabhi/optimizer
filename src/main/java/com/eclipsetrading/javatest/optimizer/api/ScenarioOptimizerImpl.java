package com.eclipsetrading.javatest.optimizer.api;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;

/***
 * ScenarioOptimizerImpl calculates the optimizedCost for each underlying and
 * only if it is less than equal to original cost(optimized gets preference in equal case but can be changed if desired)
 * then it puts the optimized scenarios for that underying in the final collection
 * else it uses the original normal scenarios for that underlying which have a lower cost compared to the optimized ones.
 *
 * Suggestions and Improvements:
 *
 * 1. The application could print the cost benefit every minute per underlying(originalCost-optimizedCost), it is not implemented now but can be easily done.
 * 2. The application could become lazy and instead of eagerly optimizing every collection as it comes, it could put them in a queue and then run to optimize logic every few timeInterval millisecs.
 *    Batching such as above in point 2 would improve the net throughput of calculation at the expense of slight increase of latency.
 * 3. The application could maintain a cache of recently optimized underlying and scenarios and maintain some LRU mechanism for the cache.
 */
public class ScenarioOptimizerImpl implements ScenarioOptimizer {
    private static final Logger logger = LogManager.getLogger(ScenarioOptimizerImpl.class.getSimpleName());

    @Override
    public Collection<Scenario> optimize(Collection<Scenario> originalScenarios) {
        ScenarioPerUnderlying scenarioPerUnderlying = new ScenarioPerUnderlying(originalScenarios);
        List<Scenario> optimizedScenarios = new ArrayList<>();
        scenarioPerUnderlying.getOptimizedScenariosCost().forEach((underlyingAsset, optimizedCost) -> {
            int originalCost = scenarioPerUnderlying.getOriginalScenarioCost(underlyingAsset);
            if (optimizedCost <= originalCost) {
                List<Double> relativeBumps = scenarioPerUnderlying.getAccumulatedRelativeBumps(underlyingAsset);
                int maxFreq                = scenarioPerUnderlying.getOriginalScenarioMaxFreq(underlyingAsset);
                Scenario scenario          = new ScenarioImpl(underlyingAsset, relativeBumps, maxFreq);
                optimizedScenarios.add(scenario);
            } else {
                optimizedScenarios.addAll(scenarioPerUnderlying.getOriginalScenarios(underlyingAsset));
            }
        });
        if (logger.isDebugEnabled()) {//Added log just incase for future we need to log some scenarios to investigate any issue
            logger.debug("Original Scenarios =" + originalScenarios.size() + ", OptimizedScenarios=" + optimizedScenarios.size());
        }
        return optimizedScenarios;
    }
}
