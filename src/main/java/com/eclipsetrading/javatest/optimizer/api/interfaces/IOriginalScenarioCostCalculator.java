package com.eclipsetrading.javatest.optimizer.api.interfaces;

import com.eclipsetrading.javatest.optimizer.api.interfaces.Scenario;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IOriginalScenarioCostCalculator {
    /**
     * Calculates the original cost breaking down the scenarios by underlying.
     * @param scenarios
     * @return
     */
    Map<String, Integer> calculateOriginalCost(Collection<Scenario> scenarios);

    /**
     * Breaks down the original scenarios by underlying for easy reference when input is already optimized for a particular underlying.
     * @param scenarios
     * @return
     */
    Map<String, List<Scenario>> breakdownScenariosPerUnderlying(Collection<Scenario> scenarios);
}
