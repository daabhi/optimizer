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

    /**
     * Sample log
     * 2023-01-31 22:07:16,885 [pool-1-thread-1] DEBUG (OptimizedScenarioGenerator.java:38) -  RunId=1 CostSavings={A0=378, A1=253, A2=318, A3=213, A4=348} DurationInMillis=17.0
     * 2023-01-31 22:07:17,698 [pool-1-thread-1] DEBUG (OptimizedScenarioGenerator.java:38) -  RunId=2 CostSavings={A0=1083, A1=993, A2=948, A3=898, A4=1103} DurationInMillis=32.0
     * 2023-01-31 22:07:18,672 [pool-1-thread-1] DEBUG (OptimizedScenarioGenerator.java:38) -  RunId=3 CostSavings={A0=2073, A1=2123, A2=2003, A3=1973, A4=2103} DurationInMillis=3.0
     * 2023-01-31 22:07:19,664 [pool-1-thread-1] DEBUG (OptimizedScenarioGenerator.java:38) -  RunId=4 CostSavings={A0=3318, A1=3633, A2=3423, A3=3388, A4=3523} DurationInMillis=0.0
     * 2023-01-31 22:07:20,665 [pool-1-thread-1] DEBUG (OptimizedScenarioGenerator.java:38) -  RunId=5 CostSavings={A0=4898, A1=5493, A2=5193, A3=5248, A4=5288} DurationInMillis=0.0
     * We are trying to record the cost savings here for every underlying using the optimization algorithm(logs above for easy reference in case needed for future)
     * @param args
     */
    public static void main(String[] args) {
        List<Scenario> scenarios = new ArrayList<>();
        scheduledExecutorService.scheduleAtFixedRate(()->{
            for(int i = 0;i<=100; i++){
                scenarios.add(new ScenarioImpl("A"+random.nextInt(5),Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5+random.nextInt(30)/10),1+random.nextInt(6)));
            }
            scenarioOptimizer.optimize(scenarios);
        },0,1, TimeUnit.SECONDS);

    }
}
