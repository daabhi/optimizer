package com.eclipsetrading.javatest.optimizer.api;

import java.util.*;

public class ScenarioOptimizerImpl implements ScenarioOptimizer {
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
        return optimizedScenarios;
    }
}
