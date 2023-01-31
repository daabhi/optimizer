package com.eclipsetrading.javatest.optimizer.api;

import java.util.*;

public class ScenarioPerUnderlying {
    private final Map<String, Integer> originalScenarioCostMap        = new HashMap<>(1000000);
    private final Map<String, Set<Double>> accumulatedRelativeBumpMap = new HashMap<>(1000000);
    private final Map<String, Integer> originalScenarioMaxFreqMap     = new HashMap<>(1000000);
    private final Map<String, Integer> optimizedScenariosCostMap      = new HashMap<>(1000000);
    private final Map<String, List<Scenario>> originalScenarioMap     = new HashMap<>(1000000);

    public ScenarioPerUnderlying(Collection<Scenario> scenarios){
        scenarios.forEach(s -> {
            String underlyingAsset             = s.getUnderlyingAsset();

            originalScenarioCostMap   .merge(underlyingAsset, s.calculateCost(), Integer::sum);
            originalScenarioMaxFreqMap.merge(underlyingAsset, s.getFrequency() , Math::max);

            accumulatedRelativeBumpMap.computeIfAbsent(underlyingAsset, k->new TreeSet<>(s.getRelativeBumps())).addAll(s.getRelativeBumps());
            originalScenarioMap       .computeIfAbsent(underlyingAsset, k->new ArrayList<>()).add(s);
        });
        accumulatedRelativeBumpMap.forEach((symbol, v) -> optimizedScenariosCostMap.put(symbol, v.size() * originalScenarioMaxFreqMap.get(symbol)));
    }

    public Map<String, Integer> getOptimizedScenariosCost() {
        return optimizedScenariosCostMap;
    }

    public Integer getOriginalScenarioCost(String symbol) {
        return originalScenarioCostMap.get(symbol);
    }

    public List<Double> getAccumulatedRelativeBumps(String symbol) {
        return new ArrayList<>(accumulatedRelativeBumpMap.get(symbol));
    }

    public List<Scenario> getOriginalScenarios(String symbol) {
        return originalScenarioMap.get(symbol);
    }

    public int getOriginalScenarioMaxFreq(String underlyingAsset) {
        return originalScenarioMaxFreqMap.get(underlyingAsset);
    }
}
