package com.eclipsetrading.javatest.optimizer.api;

import java.util.*;

public class ScenarioOptimizerImpl implements ScenarioOptimizer {
    @Override
    public Collection<Scenario> optimize(Collection<Scenario> originalScenarios) {
        ScenarioMetadata scenarioMetadata = new ScenarioMetadata();
        scenarioMetadata.populate(originalScenarios);

        List<Scenario> optimizedScenarios = new ArrayList<>();
        scenarioMetadata.getOptimizedScenariosCost().forEach((underlyingAsset, optimizedCost) -> {
            int originalCost = scenarioMetadata.getOriginalScenarioCost(underlyingAsset);
            if (optimizedCost <= originalCost) {
                Scenario scenario = new ScenarioImpl(underlyingAsset, scenarioMetadata.getRelativeBumps(underlyingAsset), scenarioMetadata.getMaxFreq(underlyingAsset));
                optimizedScenarios.add(scenario);
            } else {
                optimizedScenarios.addAll(scenarioMetadata.getOriginalScenarios(underlyingAsset));
            }
        });
        return optimizedScenarios;
    }
}
