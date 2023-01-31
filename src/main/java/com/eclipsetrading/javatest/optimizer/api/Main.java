package com.eclipsetrading.javatest.optimizer.api;

import com.eclipsetrading.javatest.optimizer.api.interfaces.Scenario;
import com.eclipsetrading.javatest.optimizer.api.interfaces.ScenarioOptimizer;
import com.eclipsetrading.javatest.optimizer.api.pojo.ScenarioImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private static final ScenarioOptimizer scenarioOptimizer = new ScenarioOptimizerImpl();
    private static final Random random = new Random();

    public static void main(String[] args) {
        List<Scenario> scenarios = new ArrayList<>();
        scheduledExecutorService.scheduleAtFixedRate(()->{
            for(int i = 0;i<=100; i++){
                scenarios.add(new ScenarioImpl("A",Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5+random.nextInt(30)/10),1+random.nextInt(6)));
            }
            scenarioOptimizer.optimize(scenarios);
        },0,1, TimeUnit.SECONDS);

    }
}
