package com.eclipsetrading.javatest.optimizer.api;

import com.eclipsetrading.javatest.optimizer.api.core.OptimizedScenarioGenerator;
import com.eclipsetrading.javatest.optimizer.api.core.ScenarioPerUnderlyingOptimizer;
import com.eclipsetrading.javatest.optimizer.api.interfaces.Scenario;
import com.eclipsetrading.javatest.optimizer.api.interfaces.ScenarioOptimizer;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final AtomicInteger runId = new AtomicInteger(0);
    @Override
    public Collection<Scenario> optimize(Collection<Scenario> originalScenarios) {
        runId.incrementAndGet();
        ScenarioPerUnderlyingOptimizer scenarioPerUnderlyingOptimizer = new ScenarioPerUnderlyingOptimizer();
        Map<String, Integer> optimizedCosts                           = scenarioPerUnderlyingOptimizer.optimize(originalScenarios);

        OptimizedScenarioGenerator optimizedScenarioGenerator         = new OptimizedScenarioGenerator(runId,scenarioPerUnderlyingOptimizer);
        return optimizedScenarioGenerator.generate(originalScenarios, optimizedCosts);
    }
}
