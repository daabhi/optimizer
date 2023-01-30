package com.eclipsetrading.javatest.optimizer.api;

import java.util.*;

public class ScenarioOptimizerImpl implements ScenarioOptimizer {
    @Override
    public Collection<Scenario> optimize(Collection<Scenario> originalScenarios) {
        UnderlyingScenarioMetadata underlyingScenarioMetadata = new UnderlyingScenarioMetadata();
        underlyingScenarioMetadata.populate(originalScenarios);

        List<Scenario> optimizedScenarios = new ArrayList<>();
        underlyingScenarioMetadata.getOptimizedScenariosCost().forEach((underlyingAsset, optimizedCost) -> {
            int originalCost = underlyingScenarioMetadata.getOriginalScenarioCost(underlyingAsset);
            if (optimizedCost <= originalCost) {
                Scenario scenario = new ScenarioImpl(underlyingAsset, underlyingScenarioMetadata.getRelativeBumps(underlyingAsset), underlyingScenarioMetadata.getMaxFreq(underlyingAsset));
                optimizedScenarios.add(scenario);
            } else {
                optimizedScenarios.addAll(underlyingScenarioMetadata.getOriginalScenarios(underlyingAsset));
            }
        });
        return optimizedScenarios;
    }
}
