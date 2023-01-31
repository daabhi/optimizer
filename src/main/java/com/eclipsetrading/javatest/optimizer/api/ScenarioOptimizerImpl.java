package com.eclipsetrading.javatest.optimizer.api;

import com.eclipsetrading.javatest.optimizer.api.interfaces.*;
import com.eclipsetrading.javatest.optimizer.api.core.OptimizedScenarioGenerator;
import com.eclipsetrading.javatest.optimizer.api.core.OptimizedScenarioCostCalculator;
import com.eclipsetrading.javatest.optimizer.api.core.OriginalScenarioCostCalculator;
import com.eclipsetrading.javatest.optimizer.api.pojo.OptimizedScenarioMetadata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * ScenarioOptimizerImpl uses the ScenarioPerUnderlyingOptimizer and OptimizedScenarioGenerator
 * 1) ScenarioPerUnderlyingOptimizer is used to generate the optimized calculations broken down at a underlying level.
 * 2) OptimizedScenarioGenerator does the comparison with
 * only if it is less than equal to original cost(optimized gets preference in equal case but can be changed if desired)
 * then it puts the optimized scenarios for that underying in the final collection
 * else it uses the original normal scenarios for that underlying which have a lower cost compared to the optimized ones.
 * The application p
 *
 * Suggestions and Improvements:
 *
 * 1. The application could become lazy and instead of eagerly optimizing every collection as it comes synchronously, it could put them in a queue
 *    and then run to optimize logic every few timeInterval millisecond asynchronously.
 *    Batching such as above would improve the net throughput of calculation at the expense of slight increase of latency per request.
 * 2. The application could maintain a cache of recently optimized underlying and scenarios and maintain some LRU mechanism for the cache.
 * 3. Ideally this can be a spring boot application where the object dependencies can be automatically injected but have left it for this exercise.
 */
public class ScenarioOptimizerImpl implements ScenarioOptimizer {
    private final AtomicInteger runId = new AtomicInteger(0);
    private final IOriginalScenarioCostCalculator originalScenarioCostCalculator   = new OriginalScenarioCostCalculator();
    private final IOptimizedScenarioCostCalculator optimizedScenarioCostCalculator = new OptimizedScenarioCostCalculator();
    private final IOptimizedScenarioGenerator optimizedScenarioGenerator = new OptimizedScenarioGenerator();

    @Override
    public Collection<Scenario> optimize(Collection<Scenario> originalScenarios) {
        runId.incrementAndGet();

        Map<String, Integer> originalCosts                     = originalScenarioCostCalculator.calculateOriginalCost(originalScenarios);
        Map<String, List<Scenario>> originalScenariosBreakdown = originalScenarioCostCalculator.breakdownScenariosPerUnderlying(originalScenarios);

        Map<String, List<Double>> relativeBumps = optimizedScenarioCostCalculator.breakDownAndAccumulateRelativeBumpsPerUnderlying(originalScenarios);
        Map<String, Integer> maxFrequencies     = optimizedScenarioCostCalculator.breakDownMaxFrequenciesPerUnderlying(originalScenarios);
        Map<String, Integer> optimizedCosts     = optimizedScenarioCostCalculator.calculateOptimizedCosts(relativeBumps, maxFrequencies);

        OptimizedScenarioMetadata optimizedScenarioMetadata = new OptimizedScenarioMetadata(runId, originalCosts, originalScenariosBreakdown, optimizedCosts, maxFrequencies, relativeBumps);

        return optimizedScenarioGenerator.generate(optimizedScenarioMetadata);
    }
}
