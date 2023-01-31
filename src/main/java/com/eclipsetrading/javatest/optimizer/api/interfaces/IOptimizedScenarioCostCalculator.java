package com.eclipsetrading.javatest.optimizer.api.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IOptimizedScenarioCostCalculator {
    /**
     * Breaks down the scenarios per underlying and accumulates all the relative bumps per underlying to make sure no bump is missed
     * @param scenarios
     * @return
     */
    Map<String, List<Double>> breakDownAndAccumulateRelativeBumpsPerUnderlying(Collection<Scenario> scenarios);

    /***
     * Breaks down the scenarios to get the max frequency for each underlying amongst all the supplied scenarios
     * @param scenarios
     * @return
     */
    Map<String, Integer> breakDownMaxFrequenciesPerUnderlying(Collection<Scenario> scenarios);

    /**
     * Calculates the optimized costs per underlying based on the relative bumps and max frequencies computed.
     * @param relativeBumps
     * @param maxFrequencies
     * @return
     */
    Map<String, Integer> calculateOptimizedCosts(Map<String, List<Double>> relativeBumps, Map<String, Integer> maxFrequencies);
}
