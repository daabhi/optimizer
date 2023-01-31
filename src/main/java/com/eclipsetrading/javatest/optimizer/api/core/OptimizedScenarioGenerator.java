package com.eclipsetrading.javatest.optimizer.api.core;

import com.eclipsetrading.javatest.optimizer.api.interfaces.IOptimizedScenarioGenerator;
import com.eclipsetrading.javatest.optimizer.api.interfaces.Scenario;
import com.eclipsetrading.javatest.optimizer.api.pojo.OptimizedScenarioMetadata;
import com.eclipsetrading.javatest.optimizer.api.pojo.ScenarioImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class OptimizedScenarioGenerator implements IOptimizedScenarioGenerator {
    private static final Logger logger = LogManager.getLogger(OptimizedScenarioGenerator.class.getSimpleName());
    private final Map<String, Integer> underlyingCostSavings = new HashMap<>(10000);
    @Override
    public Collection<Scenario> generate(OptimizedScenarioMetadata metadata) {
        long startTime                    = System.currentTimeMillis();
        List<Scenario> optimizedScenarios = new ArrayList<>();

        metadata.getOptimizedCosts().forEach((underlyingAsset, optimizedCost) -> {
            int originalCost = metadata.getOriginalCosts().get(underlyingAsset);
            if (optimizedCost <= originalCost) {
                underlyingCostSavings.put(underlyingAsset,originalCost-optimizedCost);
                optimizedScenarios.add(new ScenarioImpl(underlyingAsset, metadata.getRelativeBumps().get(underlyingAsset), metadata.getMaxFrequencies().get(underlyingAsset)));
            } else {
                underlyingCostSavings.put(underlyingAsset, 0);
                optimizedScenarios.addAll(metadata.getOriginalScenariosBreakdown().get(underlyingAsset));
            }
        });
        log(startTime, metadata.getRunId());
        return optimizedScenarios;
    }

    private void log(long startTime, AtomicInteger runId) {
        double durationInMillis = Math.round((System.currentTimeMillis()- startTime));
        if (logger.isDebugEnabled()) {
            logger.debug(" RunId="+runId.get()+" CostSavings="+ underlyingCostSavings.toString()+ " DurationInMillis="+ durationInMillis);
        }
    }


}
