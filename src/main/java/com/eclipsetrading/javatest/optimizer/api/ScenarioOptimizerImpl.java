package com.eclipsetrading.javatest.optimizer.api;

import java.util.*;

/***
 * ScenarioOptimizerImpl calculates the optimizedCost for each underlying and
 * only if it is less than equal to original cost(optimized gets preference in equal case but can be changed if desired)
 * then it puts the optimized scenarios for that underying in the final collection
 * else it uses the original normal scenarios for that underlying which have a lower cost compared to the optimized ones.
 */
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
