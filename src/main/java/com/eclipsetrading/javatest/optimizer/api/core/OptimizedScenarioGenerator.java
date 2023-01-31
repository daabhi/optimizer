package com.eclipsetrading.javatest.optimizer.api.core;

import com.eclipsetrading.javatest.optimizer.api.interfaces.Scenario;
import com.eclipsetrading.javatest.optimizer.api.pojo.ScenarioImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class OptimizedScenarioGenerator {
    private final AtomicInteger runId;
    private static final Logger logger = LogManager.getLogger(OptimizedScenarioGenerator.class.getSimpleName());
    private final ScenarioPerUnderlyingOptimizer scenarioPerUnderlyingOptimizer;
    public OptimizedScenarioGenerator(AtomicInteger runId, ScenarioPerUnderlyingOptimizer scenarioPerUnderlyingOptimizer) {
        this.scenarioPerUnderlyingOptimizer = scenarioPerUnderlyingOptimizer;
        this.runId = runId;
    }
    public Collection<Scenario> generate(Collection<Scenario> originalScenarios, Map<String, Integer> optimizedCosts) {
        long startTime = System.currentTimeMillis();
        List<Scenario> optimizedScenarios                             = new ArrayList<>();

        optimizedCosts.forEach((underlyingAsset, optimizedCost) -> {
            int originalCost = scenarioPerUnderlyingOptimizer.getCost(underlyingAsset);
            if (optimizedCost <= originalCost) {
                optimizedScenarios.add(createScenario(scenarioPerUnderlyingOptimizer, underlyingAsset));
            } else {
                optimizedScenarios.addAll(scenarioPerUnderlyingOptimizer.getScenarios(underlyingAsset));
            }
        });
        double durationInMillis = Math.round((System.currentTimeMillis()-startTime));
        if (logger.isDebugEnabled()) {
            logger.debug(" RunId="+runId.get()+" DurationInMillis="+ durationInMillis+" OriginalScenarios size="+originalScenarios.size()+" OptimizedScenarios size=" + optimizedScenarios.size());
        }
        return optimizedScenarios;
    }
    private Scenario createScenario(ScenarioPerUnderlyingOptimizer scenarioPerUnderlyingOptimizer, String underlyingAsset) {
        List<Double> relativeBumps = scenarioPerUnderlyingOptimizer.getRelativeBumps(underlyingAsset);
        int maxFreq                = scenarioPerUnderlyingOptimizer.getMaxFreq(underlyingAsset);
        return new ScenarioImpl(underlyingAsset, relativeBumps, maxFreq);
    }
}
