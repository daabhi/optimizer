import com.eclipsetrading.javatest.optimizer.api.Scenario;
import com.eclipsetrading.javatest.optimizer.api.ScenarioImpl;
import com.eclipsetrading.javatest.optimizer.api.ScenarioOptimizer;
import com.eclipsetrading.javatest.optimizer.api.ScenarioOptimizerImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class ScenarioOptimizerTest {

    private final ScenarioOptimizer scenarioOptimizer = new ScenarioOptimizerImpl();

    @Test
    /**
     *  * S1: U1 - bumps=0.1,0.2,0.3,0.4,0.5 freq=2<br>
     *  * S2: U1 - bumps=0.1,0.2,0.3,0.4,0.6 freq=2<br>
     *  * Cost = 5*2 + 5*2 = 20</td>
     *  * <td>U1 - bumps=0.1,0.2,0.3,0.4,0.5,0.6 freq=2<br>
     *  * Cost = 6*2 = 12</td>
     **/
    public void testBasicWhenOptimizedIsMoreEfficientThanOriginalForSameAssetHavingSameFreq() {
        final Scenario sc1 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5), 2);
        final Scenario sc2 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.6), 2);

        Collection<Scenario> optimizedScenarios = scenarioOptimizer.optimize(Arrays.asList(sc1, sc2));
        Assertions.assertEquals("[{underlyingAsset='U1', relativeBumps=[0.1, 0.2, 0.3, 0.4, 0.5, 0.6], frequency=2}\n]", optimizedScenarios.toString());
        Assertions.assertEquals(12, optimizedScenarios.stream().mapToInt(Scenario::calculateCost).sum());
    }


    @Test
    /**
     *  * S1: U1 - bumps=0.1,0.2,0.3,0.4,0.5 freq=2<br>
     *  * S2: U1 - bumps=0.1,0.2,0.3,0.4,0.6 freq=4<br>
     *  * Cost = 5*2 + 5*4 = 30</td>
     *  * <td>U1 - bumps=0.1,0.2,0.3,0.4,0.5,0.6 freq=4<br>
     *  * Cost = 6*4 = 24</td>
     *  * <td>Frequency becomes 4 to fulfill requirement of S2</td>
     */
    public void testWhenOptimizedIsMoreEfficientThanOriginalHavingDifferentFrequencies(){
        final Scenario sc1 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5), 2);
        final Scenario sc2 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.6), 4);
        Collection<Scenario> optimizedScenarios = scenarioOptimizer.optimize(Arrays.asList(sc1, sc2));
        Assertions.assertEquals("[{underlyingAsset='U1', relativeBumps=[0.1, 0.2, 0.3, 0.4, 0.5, 0.6], frequency=4}\n" + "]", optimizedScenarios.toString());
        Assertions.assertEquals(24, optimizedScenarios.stream().mapToInt(Scenario::calculateCost).sum());
    }

    @Test
    /**
     * * <td>S1: U1 - bumps=0.1,0.2,0.3,0.4,0.5 freq=2<br>
     *  * S2: U1 - bumps=0.5,0.6,0.7,0.8,0.9 freq=4<br>
     *  * Cost = 5*2 + 5*4 = 30</td>
     *  * <td>U1 - bumps=0.1,0.2,0.3,0.4,0.5 freq=2<br>
     *  * U1 - bumps=0.5,0.6,0.7,0.8,0.9 freq=4<br>
     *  * Cost = 5*2 + 5*4 = 30</td>
     *  * <td>Input is optimal as combined scenario is more costly, eg<br>
     */
    public void testWhenInputIsAlreadyOptimized(){
        final Scenario sc1 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5), 2);
        final Scenario sc2 = new ScenarioImpl("U1", Arrays.asList(0.5, 0.6, 0.7, 0.8, 0.9), 4);
        Collection<Scenario> optimizedScenarios = scenarioOptimizer.optimize(Arrays.asList(sc1, sc2));
        Assertions.assertEquals("[{underlyingAsset='U1', relativeBumps=[0.1, 0.2, 0.3, 0.4, 0.5], frequency=2}\n" +
                                        ", {underlyingAsset='U1', relativeBumps=[0.5, 0.6, 0.7, 0.8, 0.9], frequency=4}\n" +"]", optimizedScenarios.toString());
        Assertions.assertEquals(30, optimizedScenarios.stream().mapToInt(Scenario::calculateCost).sum());

    }
    @Test
    /**
     *  <td>S1: U1 - bumps=0.1,0.2,0.3,0.4,0.5 freq=2<br>
     *  * S2: U2 - bumps=0.1,0.2,0.3,0.4,0.6 freq=2<br>
     *  * Cost = 5*2 + 5*2 = 20</td>
     *  * <td>U1 - bumps=0.1,0.2,0.3,0.4,0.5 freq=2<br>
     *  * U2 - bumps=0.1,0.2,0.3,0.4,0.6 freq=2<br>
     *  * Cost = 5*2 + 5*2 = 20</td>
     *  * <td>Scenarios from different underlying assets cannot be combined</td>
     */
    public void testScenariosCantBeCombinedForDifferentAssets(){
        final Scenario sc1 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5), 2);
        final Scenario sc2 = new ScenarioImpl("U2", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.6), 2);
        Collection<Scenario> optimizedScenarios = scenarioOptimizer.optimize(Arrays.asList(sc1, sc2));
        Assertions.assertEquals("[{underlyingAsset='U1', relativeBumps=[0.1, 0.2, 0.3, 0.4, 0.5], frequency=2}\n" +
                ", {underlyingAsset='U2', relativeBumps=[0.1, 0.2, 0.3, 0.4, 0.6], frequency=2}\n" +"]", optimizedScenarios.toString());
        Assertions.assertEquals(20, optimizedScenarios.stream().mapToInt(Scenario::calculateCost).sum());
    }


    @Test
    /**
     *  * On the flip side, scenario with bumps 0.1, 0.2, 0.3, 0.4 and frequency 5 can <b><i>NOT</i></b> be an optimal to scenario with bump 0.1 and 0.2, frequency 10 due to the decrease in frequency.
     */
    public void testFlipSideEdgeCasesIncreasedFreqIsHonoured(){
        final Scenario sc1 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4), 5);
        final Scenario sc2 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2), 10);

        Collection<Scenario> optimizedScenarios = scenarioOptimizer.optimize(Arrays.asList(sc1, sc2));
        Assertions.assertEquals("[{underlyingAsset='U1', relativeBumps=[0.1, 0.2, 0.3, 0.4], frequency=10}\n" + "]", optimizedScenarios.toString());
        Assertions.assertEquals(40, optimizedScenarios.stream().mapToInt(Scenario::calculateCost).sum());
    }

    @Test
    /**
     ** * Likewise, it can <b><i>NOT</i></b> be an optimal to scenario with bump 0.1, 0.2, 0.3, 0.4, 0.5, frequency 5 due to missing of bump 0.5.
     */
    public void testFlipSideEdgeCasesNoOfBumpsIsHonoured(){
        final Scenario sc1 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4), 5);
        final Scenario sc2 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5), 5);
        Collection<Scenario> optimizedScenarios = scenarioOptimizer.optimize(Arrays.asList(sc1, sc2));
        Assertions.assertEquals("[{underlyingAsset='U1', relativeBumps=[0.1, 0.2, 0.3, 0.4, 0.5], frequency=5}\n" + "]", optimizedScenarios.toString());
        Assertions.assertEquals(25, optimizedScenarios.stream().mapToInt(Scenario::calculateCost).sum());
    }
    @Test
    /**
     * Test random more scenarios merge honouring all the cases
     */
    public void testBasicOptimizedScenariosMerged() {
        final Scenario sc1 = new ScenarioImpl("A", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5), 2);
        final Scenario sc2 = new ScenarioImpl("A", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.6), 3);
        final Scenario sc3 = new ScenarioImpl("A", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.6), 4);
        final Scenario sc4 = new ScenarioImpl("B", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5), 1);
        final Scenario sc5 = new ScenarioImpl("B", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.6), 2);
        final Scenario sc6 = new ScenarioImpl("B", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.6), 3);

        Collection<Scenario> optimizedScenarios = scenarioOptimizer.optimize(Arrays.asList(sc1, sc2, sc3, sc4, sc5, sc6));
        Assertions.assertEquals("[{underlyingAsset='A', relativeBumps=[0.1, 0.2, 0.3, 0.4, 0.5, 0.6], frequency=4}\n" +
                                        ", {underlyingAsset='B', relativeBumps=[0.1, 0.2, 0.3, 0.4, 0.5, 0.6], frequency=3}\n]", optimizedScenarios.toString());
        Assertions.assertEquals(24+18, optimizedScenarios.stream().mapToInt(Scenario::calculateCost).sum());
    }

    @Test
    /**
     *<td>S1: U1 - bumps=0.1,0.2,0.3,0.4,0.5,0.6 freq=1<br>
     *  * S2: U1 - bumps=0.1,0.2,0.3,0.4,0.6,0.7 freq=2<br>
     *  * S3: U1 - bumps=0.1,0.2,0.3,0.4,0.6,0.8 freq=3<br>
     *  * S4: U1 - bumps=0.1,0.2,0.3,0.4,0.6,0.9 freq=4<br>
     *  * S5: U1 - bumps=0.1,0.2,0.3,0.4,0.6,1.0 freq=5<br>
     *  * Cost = 6*1 + 6*2 + 6*3 + 6*4 + 6*5 = 90</td>
     *  * <td>U1 - bumps=0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0 freq=5<br>
     *  * Cost = 10*9 = 50</td>
     *  * <td>Scenarios are optimized with highest frequency</td>
     */
    public void testScenarios(){
        final Scenario sc1 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5,0.6), 1);
        final Scenario sc2 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.6,0.7), 2);
        final Scenario sc3 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.6,0.8), 3);
        final Scenario sc4 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.6,0.9), 4);
        final Scenario sc5 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.6,1.0), 5);
        Collection<Scenario> optimizedScenarios = scenarioOptimizer.optimize(Arrays.asList(sc1, sc2, sc3, sc4,sc5));
        Assertions.assertEquals("[{underlyingAsset='U1', relativeBumps=[0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0], frequency=5}\n]", optimizedScenarios.toString());
        Assertions.assertEquals(50, optimizedScenarios.stream().mapToInt(Scenario::calculateCost).sum());
    }

    @Test
    public void testPerformance(){
        List<Scenario> scenarios = new ArrayList<>();
        IntStream.range(0,1000000).forEach(a->scenarios.add(new ScenarioImpl("U1",Arrays.asList(0.1, 0.2, 0.3+a/10),1+a)));
        long startTime = System.nanoTime();
        Collection<Scenario> optimizedScenarios = scenarioOptimizer.optimize(scenarios);
        double durationInSecs = (System.nanoTime() - startTime)/1e9;

        Assertions.assertEquals(1057913696, optimizedScenarios.stream().mapToInt(Scenario::calculateCost).sum());
        Assertions.assertEquals(1,durationInSecs,0.5,"Took roughly 1-1.5 secs to optimize 1 mill scenarios");


        scenarios.clear();
        optimizedScenarios.clear();
        IntStream.rangeClosed(1, 10)
                .forEach(finalI -> IntStream.range(0, 100_000)
                        .forEach(a -> scenarios.add(new ScenarioImpl("U" + finalI, Arrays.asList(0.1, 0.2, 0.3 + a / 10), 1 + a))));
        startTime = System.nanoTime();
        optimizedScenarios = scenarioOptimizer.optimize(scenarios);
        durationInSecs = (System.nanoTime() - startTime)/1e9;

        Assertions.assertEquals(1412065408, optimizedScenarios.stream().mapToInt(Scenario::calculateCost).sum());
        Assertions.assertEquals(1, durationInSecs,0.5,"Took roughly 1-1.5 secs to optimize 100k scenarios for 10 underlyings");
    }

    @Test
    public void testOneUnderlyingInputAlreadyOptimizedAndOtherUnderlyingNeedsToBeOptimized(){
        final Scenario sc1 = new ScenarioImpl("U1", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5,0.6), 1);
        final Scenario sc2 = new ScenarioImpl("U1", Arrays.asList(0.6,0.7), 2);

        final Scenario sc3 = new ScenarioImpl("U2", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.6,0.7), 3);
        final Scenario sc4 = new ScenarioImpl("U2", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.6,0.8), 4);
        final Scenario sc5 = new ScenarioImpl("U2", Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.6,0.9), 5);
        Collection<Scenario> optimizedScenarios = scenarioOptimizer.optimize(Arrays.asList(sc1, sc2, sc3, sc4,sc5));
        //For underlying U1 input is already optimized and thus it stays like that, whereas for U2 input has been optimized accordingly
        Assertions.assertEquals("[{underlyingAsset='U1', relativeBumps=[0.1, 0.2, 0.3, 0.4, 0.5, 0.6], frequency=1}\n" +
                ", {underlyingAsset='U1', relativeBumps=[0.6, 0.7], frequency=2}\n" +
                ", {underlyingAsset='U2', relativeBumps=[0.1, 0.2, 0.3, 0.4, 0.6, 0.7, 0.8, 0.9], frequency=5}\n" +
                "]", optimizedScenarios.toString());
        Assertions.assertEquals(50, optimizedScenarios.stream().mapToInt(Scenario::calculateCost).sum());


    }

}
