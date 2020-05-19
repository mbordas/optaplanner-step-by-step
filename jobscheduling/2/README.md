# Job scheduling step 2

## Goals
* Add some task dependencies: some tasks must be done before other start
* Add workers (resources that can do tasks in parallel)
* Add capabilities requirements to tasks
* Getting the schedule with a minimal global makespan

## Solving the problem

### Model

We adds a dependencies attribute to the Task class. It stores every Task that must be done before this one starts.
We create the new class Worker. Capabilities are stored in strings both in Task class (requirements) and in Worker class (abilities).


### Drools scheduleCalculatorRules.drl

A new rule "taskDependencies" is added. It finds out any couple of Allocations where one depends on the other and where dependency order is broken (dependency task ends after the dependent task starts).
A new rule "capabilities" is added. It lowers hard score when a worker is assigned to a task it can't handle because it does'nt have one or more required capabilities.
A new rule "globalMakespan" measures the global duration of the schedule (the duration between the first task start end the last task ends) and lowers the soft score with that amount.
