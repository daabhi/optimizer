package com.eclipsetrading.javatest.optimizer.api.core;

import com.eclipsetrading.javatest.optimizer.api.interfaces.Scenario;

import java.util.*;

public class ScenarioPerUnderlyingOptimizer {
    private final Map<String, Integer> originalCosts = new HashMap<>(1000000);//Caches pre-allocated for performance.
    private final Map<String, Set<Double>> relativeBumps = new HashMap<>(1000000);
    private final Map<String, Integer> maxFrequencies = new HashMap<>(1000000);
    private final Map<String, Integer> optimizedCosts = new HashMap<>(1000000);
    private final Map<String, List<Scenario>> originalScenarios = new HashMap<>(1000000);

    public Map<String, Integer> optimize(Collection<Scenario> scenarios) {
        scenarios.forEach(s -> {
            originalCosts.merge(s.getUnderlyingAsset(), s.calculateCost(), Integer::sum);
            maxFrequencies.merge(s.getUnderlyingAsset(), s.getFrequency() , Math::max);
            relativeBumps.computeIfAbsent(s.getUnderlyingAsset(), k->new TreeSet<>(s.getRelativeBumps())).addAll(s.getRelativeBumps());
            originalScenarios.computeIfAbsent(s.getUnderlyingAsset(), k->new ArrayList<>()).add(s);
        });
        relativeBumps.forEach((symbol, v) -> optimizedCosts.put(symbol, v.size() * maxFrequencies.get(symbol)));
        return optimizedCosts;
    }

    public Integer getCost(String symbol) {
        return originalCosts.get(symbol);
    }

    public List<Double> getRelativeBumps(String symbol) {
        return new ArrayList<>(relativeBumps.get(symbol));
    }

    public List<Scenario> getScenarios(String symbol) {
        return originalScenarios.get(symbol);
    }

    public int getMaxFreq(String underlyingAsset) {
        return maxFrequencies.get(underlyingAsset);
    }
}
