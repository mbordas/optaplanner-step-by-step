# Job scheduling step 1

## Goals
* Define some tasks to be done, each with a charge (time needed to be done) and a due date
* Use optaplanner to compute an optimal schedule
* Use the Drools language for a better score calculation

## Limitations
* All the tasks are performed one after the other by one worker
* The worker can start a new task no matter which one is already done (no task dependencies)

## Solving the problem

### Model

The Schedule class defines the problem. It also defines a possible solution, as it contains tasks and allocations. The Schedule class is marked with the @PlanningSolution annotation. Its tasks are marked with @ProblemFactCollectionProperty because it can not be changed. The solution part of the Schedule class is the list of Allocation, marked with @PlanningEntityCollectionProperty.
* The @PlanningSolution class defines the problem and solutions instances.
* The @ProblemFactCollectionProperty identifies the problem's input.
* The @PlanningEntityCollectionProperty identifies what can be changed from one solution to another.


### Solver

Now that the domain classes defines what a Schedule solution can be, we have to code the rules that compute what a good solution is. This is done by coding score computation.
* The easy way: the Java class ScheduleEasyScoreCalculator
* The better way: the Drools rules file scheduleCalculatorRules.drl

In both ways, we will compute a HardSoftScore. This kind of score stores 2 subscores, one for hard constraints, one for soft constraints. The solver will satisfy hard constraints first. In hard score we will penalize forbidden configurations, like scheduling 2 tasks at the same time, or scheduling the same task many times. In soft score we will penalize task that are done late.

### Java ScheduleEasyScoreCalculator

Just code the computeScore(schedule) method. It will be called for each solution candidate.

### Drools scheduleCalculatorRules.drl

Here the score calculation is split into rules. The solver will apply the rules in a more efficient way. It will not need to compute every rules to compare solution candidates. This is why this solution makes the solver run really faster.








