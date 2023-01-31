package com.eclipsetrading.javatest.optimizer.api.pojo;

import com.eclipsetrading.javatest.optimizer.api.interfaces.Scenario;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class OptimizedScenarioMetadata {
    private final AtomicInteger runId;
    private final Map<String, Integer> originalCosts;
    private final Map<String, List<Scenario>> originalScenariosBreakdown;
    private final Map<String, Integer> optimizedCosts;
    private final Map<String, Integer> maxFrequencies;
    private final Map<String, List<Double>> relativeBumps;
    public OptimizedScenarioMetadata(AtomicInteger runId, Map<String, Integer> originalCosts, Map<String, List<Scenario>> originalScenariosBreakdown, Map<String, Integer> optimizedCosts, Map<String, Integer> maxFrequencies, Map<String, List<Double>> relativeBumps) {
        this.runId                      = runId;
        this.originalCosts              = originalCosts;
        this.originalScenariosBreakdown = originalScenariosBreakdown;
        this.optimizedCosts             = optimizedCosts;
        this.maxFrequencies             = maxFrequencies;
        this.relativeBumps              = relativeBumps;
    }
    public AtomicInteger getRunId() {
        return runId;
    }

    public Map<String, Integer> getOriginalCosts() {
        return originalCosts;
    }

    public Map<String, List<Scenario>> getOriginalScenariosBreakdown() {
        return originalScenariosBreakdown;
    }

    public Map<String, Integer> getOptimizedCosts() {
        return optimizedCosts;
    }

    public Map<String, Integer> getMaxFrequencies() {
        return maxFrequencies;
    }

    public Map<String, List<Double>> getRelativeBumps() {
        return relativeBumps;
    }
}
