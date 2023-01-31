package com.eclipsetrading.javatest.optimizer.api;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;

/***
 * ScenarioOptimizerImpl calculates the optimizedCost for each underlying and
 * only if it is less than equal to original cost(optimized gets preference in equal case but can be changed if desired)
 * then it puts the optimized scenarios for that underying in the final collection
 * else it uses the original normal scenarios for that underlying which have a lower cost compared to the optimized ones.
 */
public class ScenarioOptimizerImpl implements ScenarioOptimizer {
    private static final Logger logger = LogManager.getLogger(ScenarioOptimizerImpl.class.getSimpleName());

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
        if (logger.isDebugEnabled()) {//Added log just incase for future we need to log some scenarios to investigate any issue.
            logger.debug("Original Scenarios =" + originalScenarios.size() + ", OptimizedScenarios=" + optimizedScenarios.size());
        }
        return optimizedScenarios;
    }
}
