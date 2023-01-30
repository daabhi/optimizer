package com.eclipsetrading.javatest.optimizer.api;

import java.util.*;

public class UnderlyingScenarioMetadata {
    private final Map<String, Integer> originalScenarioCostMap        = new HashMap<>();
    private final Map<String, Set<Double>> accumulatedRelativeBumpMap = new HashMap<>();
    private final Map<String, Integer> originalScenarioMaxFreqMap = new HashMap<>();
    private final Map<String, Integer> optimizedScenariosCostMap      = new HashMap<>();
    private final Map<String, List<Scenario>> originalScenarioMap     = new HashMap<>();

    public void populate(Collection<Scenario> originalScenarios){
        originalScenarios.forEach(originalScenario -> {
            String underlyingAsset             = originalScenario.getUnderlyingAsset();
            Set<Double> originalRelativeBumps  = new TreeSet<>(originalScenario.getRelativeBumps());

            originalScenarioCostMap   .merge(underlyingAsset, originalScenario.calculateCost(), Integer::sum);
            originalScenarioMaxFreqMap.merge(underlyingAsset, originalScenario.getFrequency() , Math::max);

            accumulatedRelativeBumpMap.computeIfAbsent(underlyingAsset, k->originalRelativeBumps).addAll(originalRelativeBumps);
            originalScenarioMap.computeIfAbsent(underlyingAsset, k->new ArrayList<>()).add(originalScenario);
        });
        accumulatedRelativeBumpMap.forEach((symbol, v) -> optimizedScenariosCostMap.put(symbol, v.size() * originalScenarioMaxFreqMap.get(symbol)));
    }

    public Map<String, Integer> getOptimizedScenariosCost() {
        return optimizedScenariosCostMap;
    }

    public Integer getOriginalScenarioCost(String symbol) {
        return originalScenarioCostMap.get(symbol);
    }

    public List<Double> getRelativeBumps(String symbol) {
        return new ArrayList<>(accumulatedRelativeBumpMap.get(symbol));
    }

    public List<Scenario> getOriginalScenarios(String symbol) {
        return originalScenarioMap.get(symbol);
    }

    public int getMaxFreq(String underlyingAsset) {
        return originalScenarioMaxFreqMap.get(underlyingAsset);
    }
}
