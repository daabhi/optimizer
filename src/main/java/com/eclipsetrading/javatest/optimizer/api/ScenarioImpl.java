package com.eclipsetrading.javatest.optimizer.api;

import java.util.List;

public class ScenarioImpl implements Scenario{
    private final String underlyingAsset;
    private final List<Double> relativeBumps;
    private final int frequency;

    public ScenarioImpl(final String underlyingAsset, final List<Double> relativeBumps, final int frequency){
        this.underlyingAsset = underlyingAsset;
        this.relativeBumps   = relativeBumps;
        this.frequency       = frequency;
    }

    @Override
    public List<Double> getRelativeBumps() {
        return relativeBumps;
    }

    @Override
    public String getUnderlyingAsset() {
        return underlyingAsset;
    }

    @Override
    public int getFrequency() {
        return frequency;
    }

    @Override
    public String toString() {
        return "{" +
                "underlyingAsset='" + underlyingAsset + '\'' +
                ", relativeBumps=" + relativeBumps +
                ", frequency=" + frequency +
                '}'+"\n";
    }
}
