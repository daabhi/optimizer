package com.eclipsetrading.javatest.optimizer.api.interfaces;

import com.eclipsetrading.javatest.optimizer.api.interfaces.Scenario;
import com.eclipsetrading.javatest.optimizer.api.pojo.OptimizedScenarioMetadata;

import java.util.Collection;

public interface IOptimizedScenarioGenerator {
    /***
     * This generator receives all the metadata information as below in order to calculate
     * whether it is worth optimizing or whether input is already optimized to return a valid optimized result.
     * 1) RelativeBumps
     * 2) OptimizedCosts
     * 3) MaxFreq
     * 4) OriginalScenariosBreakdown
     * 5) OriginalScenarioCosts
     *
     * Additionally it also stores the cost saving per run per underlying which can be used for reference purposes if needed in future.
     * @param metadata
     * @return
     */
    Collection<Scenario> generate(OptimizedScenarioMetadata metadata);
}
