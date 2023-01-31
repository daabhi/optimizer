package com.eclipsetrading.javatest.optimizer.api.core;

import com.eclipsetrading.javatest.optimizer.api.interfaces.IOriginalScenarioCostCalculator;
import com.eclipsetrading.javatest.optimizer.api.interfaces.Scenario;

import java.util.*;

public class OriginalScenarioCostCalculator implements IOriginalScenarioCostCalculator {
    private final Map<String, Integer> originalCosts = new HashMap<>(1000000);
    private final Map<String, List<Scenario>> breakDownScenariosPerUnderlying = new HashMap<>(1000000);

    @Override
    public Map<String, Integer> calculateOriginalCost(Collection<Scenario> scenarios) {
        scenarios.forEach(s -> originalCosts.merge(s.getUnderlyingAsset(), s.calculateCost(), Integer::sum));
        return originalCosts;
    }
    @Override
    public Map<String, List<Scenario>> breakdownScenariosPerUnderlying(Collection<Scenario> scenarios) {
        scenarios.forEach(s -> breakDownScenariosPerUnderlying.computeIfAbsent(s.getUnderlyingAsset(), k->new ArrayList<>()).add(s));
        return breakDownScenariosPerUnderlying;
    }
}
