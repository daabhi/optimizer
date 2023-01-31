package com.eclipsetrading.javatest.optimizer.api.core;

import com.eclipsetrading.javatest.optimizer.api.interfaces.IOptimizedScenarioCostCalculator;
import com.eclipsetrading.javatest.optimizer.api.interfaces.Scenario;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The data structures are pre-allocated with 1 mill to ensure it can operate smoothly under high load until size by avoiding rehashing. (Using more memory at startup for higher performance)
 */
public class OptimizedScenarioCostCalculator implements IOptimizedScenarioCostCalculator {
    private final Map<String, Set<Double>> relativeBumps = new HashMap<>(1000000);
    private final Map<String, Integer> maxFrequencies = new HashMap<>(1000000);
    private final Map<String, Integer> optimizedCosts = new HashMap<>(1000000);

    @Override
    public Map<String,List<Double>> breakDownAndAccumulateRelativeBumpsPerUnderlying(Collection<Scenario> scenarios) {
        scenarios.forEach(s -> relativeBumps.computeIfAbsent(s.getUnderlyingAsset(), k->new TreeSet<>(s.getRelativeBumps())).addAll(s.getRelativeBumps()));
        return relativeBumps.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> new ArrayList<>(entry.getValue()), (a, b) -> b));
    }

    @Override
    public Map<String,Integer> breakDownMaxFrequenciesPerUnderlying(Collection<Scenario> scenarios) {
        scenarios.forEach(s -> maxFrequencies.merge(s.getUnderlyingAsset(), s.getFrequency() , Math::max));
        return maxFrequencies;
    }

    @Override
    public Map<String, Integer> calculateOptimizedCosts(Map<String, List<Double>> relativeBumps, Map<String, Integer> maxFrequencies) {
        relativeBumps.forEach((symbol, v) -> optimizedCosts.put(symbol, v.size() * maxFrequencies.get(symbol)));
        return optimizedCosts;
    }
}
