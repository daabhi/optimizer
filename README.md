# Eclipse Trading Java Programming Challenge

## General requirements

Maven projects are provided containing interfaces. These interfaces contain JavaDoc with the requirements. You should
not change these interfaces.

*It is very important to carefully read and fully understand the JavaDoc requirements before answering.*

For your answers, you may only use the Java 11 language and J2SE APIs. You may use third party libraries in your unit
tests, but not your solution itself.

Simplicity and correctness are valued more than performance and extensibility. However your solution should not be
needlessly inefficient.

We expect your code to be unit tested against the requirements. Use the standard Maven directory for your test classes.

There are no trick questions - if a problem seems straightforward to solve it probably is.

Implement your solution within this directory structure and return it as a .tar.gz file to your HR contact. Note you
can create a .tar.gz file for submission by running `mvn clean verify`. The file will be generated in the target
directory.

## Scenario optimizer

We manage our exposure to price fluctuations by running scenarios on our positions regularly throughout the day. Running a scenario involves bumping the prices up and down as required and then calculating the changes in P&L.

For simplicity, a scenario is defined by the underlying asset, the list of bumps (size n) and the frequency of recalculation (f). The cost of scenario is defined as n * f.

Running scenarios is costly. However, scenarios of the same underlying asset may be combined to reduce the overall cost. For example, a scenario with bumps of 0.01, 0.05 and 0.1 running twice per minute has a cost of 6. A scenario with 0.05, 0.1 and 0.15 bumps of 3 times per minute has a cost of 9. Combining them to a single scenario of 0.01, 0.05, 0.1 and 0.15 of 3 times per minute gives a cost of 12. This gives a saving of 3 (reduction from the total of 15 to 12), even though the bumps in the first scenario are being evaluated one more time per minute.

You are asked to implement the optimizer with a practical algorithm by implementing the `ScenarioOptimizer` and `Scenario` interfaces.

Please refer to the `ScenarioOptimizer` interface for a detailed list of requirements and examples.

You should state any assumptions you have made in your implementation.
