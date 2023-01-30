package com.eclipsetrading.javatest.optimizer.api;

import java.util.*;

public class ScenarioMetadata {
    Map<String, Integer> symbolOriginalScenarioCost         = new HashMap<>();
    Map<String, Set<Double>> symbolAccumulatedRelativeBumps = new HashMap<>();
    Map<String, Integer> symbolMaxFreq                      = new HashMap<>();
    Map<String, Integer> symbolOptimizedScenariosCost       = new HashMap<>();
    Map<String, List<Scenario>> originalSymbolScenarios     = new HashMap<>();

    public void populate(Collection<Scenario> originalScenarios){
        originalScenarios.forEach(originalScenario -> {
            String underlyingAsset = originalScenario.getUnderlyingAsset();

            if (symbolOriginalScenarioCost.containsKey(originalScenario.getUnderlyingAsset())) {
                int currentCost = symbolOriginalScenarioCost.get(originalScenario.getUnderlyingAsset());
                symbolOriginalScenarioCost.put(originalScenario.getUnderlyingAsset(), currentCost + originalScenario.calculateCost());
            } else {
                symbolOriginalScenarioCost.put(originalScenario.getUnderlyingAsset(), originalScenario.calculateCost());
            }
            if (symbolAccumulatedRelativeBumps.containsKey(underlyingAsset)) {
                symbolAccumulatedRelativeBumps.get(underlyingAsset).addAll(originalScenario.getRelativeBumps());
            } else {
                symbolAccumulatedRelativeBumps.put(underlyingAsset, new TreeSet<>(originalScenario.getRelativeBumps()));
            }
            if (symbolMaxFreq.containsKey(underlyingAsset)) {
                symbolMaxFreq.put(underlyingAsset, Math.max(symbolMaxFreq.get(underlyingAsset), originalScenario.getFrequency()));
            } else {
                symbolMaxFreq.put(underlyingAsset, originalScenario.getFrequency());
            }
            if (originalSymbolScenarios.containsKey(underlyingAsset)){
                originalSymbolScenarios.get(underlyingAsset).add(originalScenario);
            }else{
                List<Scenario> scenarios = new ArrayList<>();
                scenarios.add(originalScenario);
                originalSymbolScenarios.put(underlyingAsset, scenarios);
            }
        });
        symbolAccumulatedRelativeBumps.forEach((symbol, v) -> symbolOptimizedScenariosCost.put(symbol, v.size() * symbolMaxFreq.get(symbol)));
    }

    public Map<String, Integer> getOptimizedScenariosCost() {
        return symbolOptimizedScenariosCost;
    }

    public Integer getOriginalScenarioCost(String symbol) {
        return symbolOriginalScenarioCost.get(symbol);
    }

    public List<Double> getRelativeBumps(String symbol) {
        return new ArrayList<>(symbolAccumulatedRelativeBumps.get(symbol));
    }

    public List<Scenario> getOriginalScenarios(String symbol) {
        return originalSymbolScenarios.get(symbol);
    }

    public int getMaxFreq(String underlyingAsset) {
        return symbolMaxFreq.get(underlyingAsset);
    }
}
